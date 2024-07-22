package com.techprimers.ratelimiter.config;

//import com.techprimers.ratelimiter.keyResolver.CustomAddressResolver;
//import com.techprimers.ratelimiter.keyResolver.CustomProxyAddressResolver;
import com.techprimers.ratelimiter.filters.MyRateLimiterFilter;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

import java.time.Duration;

import java.util.Optional;

@Configuration
public class GatewayConfig {

//    @Autowired
//    private KeyResolver userKeyResolver;

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder routeLocatorBuilder, MyRateLimiterFilter myRateLimiterFilter) {
        System.out.println("GatewayConfig myRoutes");
        return routeLocatorBuilder.routes()
                .route(p -> p
                        .path("/auth/hello")
                        .filters(f -> f.requestRateLimiter()
                                        .configure(c -> c.setRateLimiter(redisRateLimiter1020())
                                                .setKeyResolver(userKeyResolver())
                        ))
//                        .filters(f -> f.filter(myRateLimiterFilter))
                        .uri("http://localhost:8081"))
//                .route(p ->p
//                        .path("/v1/country/coastline")
//                        .uri("http://localhost:8081"))
//                .route(p ->p
//                        .path("/v1/books/all")
//                        .filters(f->f.requestRateLimiter().configure(c->c.setRateLimiter(redisRateLimiter())))
//                        .uri("http://localhost:8082"))
//                .route(p ->p
//                        .path("/v1/books/all/delayed")
//                        .filters(f -> f.circuitBreaker(c->c.setName("codedTribeCB").setFallbackUri("/defaultFallback")))
//                        .uri("http://localhost:8082"))
                .build();
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        System.out.println("GatewayConfig defaultCustomizer");
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .timeLimiterConfig(TimeLimiterConfig.custom()
                        .timeoutDuration(Duration.ofSeconds(2)).build()).build());
    }

    @Bean
    @Primary
    public RedisRateLimiter redisRateLimiter1020() {
        System.out.println("GatewayConfig redisRateLimiter1020");
        return new RedisRateLimiter(10, 20);
    }

    @Bean
    public RedisRateLimiter redisRateLimiter2040() {
        return new RedisRateLimiter(20, 40);
    }

    @Bean
    public KeyResolver userKeyResolver() {
//        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
        return exchange -> {
            // Extracting the client IP address from request
            String ip = Optional.ofNullable(exchange.getRequest().getRemoteAddress())
                    .map(address -> address.getAddress().getHostAddress())
                    .orElse("unknown");
            System.out.println(ip);
            // Use custom key resolver to resolve IP-based keys
            return Mono.just(ip);
        };
    }

//    @Bean
//    public KeyResolver userKeyResolver() {
////        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
//        return exchange -> userKeyResolver.resolve(exchange);
//    }
}
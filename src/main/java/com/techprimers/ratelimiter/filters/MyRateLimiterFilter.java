package com.techprimers.ratelimiter.filters;

import com.techprimers.ratelimiter.keyResolver.CustomAddressResolver;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
public class MyRateLimiterFilter implements GatewayFilter {

    @Autowired
    private KeyResolver userKeyResolver;

//    private final CustomAddressResolver customAddressResolver;
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public MyRateLimiterFilter(ReactiveRedisTemplate<String, String> redisTemplate, KeyResolver userKeyResolver) {
        this.redisTemplate = redisTemplate;
        this.userKeyResolver = userKeyResolver;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("MyRateLimiterFilter filter");
        return userKeyResolver.resolve(exchange)
                .flatMap(key -> {
                    System.out.println("key: " + key);
                    return redisTemplate.opsForValue().increment(key)
                            .flatMap(tokens -> {
                                System.out.println("MyRateLimiterFilter filter flatMap 1, " + tokens);
                                // key expire after 3 s
                                if (tokens == 1) return redisTemplate.expire(key, Duration.ofMillis(3000)).thenReturn(tokens);
                                System.out.println("MyRateLimiterFilter filter flatMap else");
                                return Mono.just(tokens);
                            })
                            .flatMap(tokens -> {
                                System.out.println("MyRateLimiterFilter filter flatMap 2, " + tokens);
                                int maxReqPerSec = 5;

                                // Implement rate limit based on user here, in this case "key" returns the user ID from JWT.
                                // You can inject the user from anywhere like user-service or read from any properties file and set the limit !

                                if (key.equals("redisRateLimiter1020")) {
                                    System.out.println("MyRateLimiterFilter filter flatMap 2 redisRateLimiter1020");
                                    maxReqPerSec = 20;
                                } else if (key.equals("redisRateLimiter2040")) {
                                    System.out.println("MyRateLimiterFilter filter flatMap 2 redisRateLimiter2040");
                                    maxReqPerSec = 40;
                                }


                                if (tokens <= maxReqPerSec) {
                                    System.out.println("MyRateLimiterFilter filter flatMap 2 tokens <= maxReqPerSec");
                                    return chain.filter(exchange);
                                } else {
                                    System.out.println("MyRateLimiterFilter filter flatMap 2 else");
                                    log.info("Exceeded request limit for the user: "
                                            + key + ", current number of request: "
                                            + tokens);
                                    exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                                    return exchange.getResponse().setComplete();
                                }
                            });
                });
    }

}
package com.techprimers.ratelimiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class RatelimiterApplication {

	public static void main(String[] args) {
		SpringApplication.run(RatelimiterApplication.class, args);
	}

//	@Bean
//	public KeyResolver keyResolver() {
//		return exchange -> {
//			String ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
//			System.out.println(ip);
//			return Mono.just(ip);
//		};
//	}
}

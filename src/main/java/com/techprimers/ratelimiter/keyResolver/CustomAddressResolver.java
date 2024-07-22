package com.techprimers.ratelimiter.keyResolver;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

//@Primary
@Component
public class CustomAddressResolver implements KeyResolver {

    // Resolve method to determine the client address for rate limiting
    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {

        // Retrieve the remote address from the exchange's request
        System.out.println("exchange.getRequest(): " + exchange.getRequest());
        System.out.println("exchange.getRequest().getRemoteAddress(): " + exchange.getRequest().getRemoteAddress());
        System.out.println("exchange.getRequest().getRemoteAddress().getAddress(): " + exchange.getRequest().getRemoteAddress().getAddress());
        System.out.println("exchange.getRequest().getRemoteAddress().getAddress().getHostAddress(): " + exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());

        Mono<String> clientAddress = Optional.ofNullable(exchange.getRequest().getRemoteAddress())
                // Map the remote address to the host address
                .map(address -> address.getAddress().getHostAddress())
                // Wrap the host address into a Mono (asynchronous value) if present
                .map(Mono::just)
                // If the remote address is not available, return an empty Mono
                .orElseGet(Mono::empty);

        System.out.println("Client Address: " + clientAddress);

        return clientAddress;
    }
}
package com.mediconnect.gateway.filter;

import com.mediconnect.gateway.config.JwtUtil;
import com.mediconnect.gateway.config.RouterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {

    private final RouterValidator routerValidator;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationFilter(RouterValidator routerValidator, JwtUtil jwtUtil) {
        this.routerValidator = routerValidator;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        System.out.println(request);
        if (routerValidator.isSecured.test(request)) {
            if (isAuthMissing(request)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED, "Authorization header is missing.");
            }

            final String token = getAuthHeader(request);

            if (jwtUtil.isInvalid(token)) {
                return onError(exchange, HttpStatus.FORBIDDEN, "Invalid or expired token.");
            }

            updateRequest(exchange, token);
        }

        System.out.println("forwarding the request");
        return chain.filter(exchange);
    }
    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add("Error-Message", message);
        return response.setComplete();
    }
    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Missing Authorization header"));
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return request.getHeaders().getFirst("Authorization")==null;
    }

    private void updateRequest(ServerWebExchange exchange, String token) {
        String email = jwtUtil.getAllClaimsFromToken(token).get("email", String.class);
        exchange.getRequest().mutate()
                .header("email", email)
                .build();
    }
}

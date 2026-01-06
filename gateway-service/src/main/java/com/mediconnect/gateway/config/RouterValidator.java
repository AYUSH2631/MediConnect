package com.mediconnect.gateway.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Predicate;

@Component
public class RouterValidator {

    public static final Set<String> openApiEndpoints = Set.of(
            "api/auth/register",
            "api/auth/signin"
    );
    public Predicate<ServerHttpRequest> isSecured = request ->
            openApiEndpoints.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}

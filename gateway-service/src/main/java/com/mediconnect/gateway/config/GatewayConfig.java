package com.mediconnect.gateway.config;

import com.mediconnect.gateway.filter.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@Configuration // Marks this class as a configuration class for Spring
public class GatewayConfig {

    private final AuthenticationFilter filter;

    public GatewayConfig(AuthenticationFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        System.out.println("inside routes");

        return builder.routes()
                .route("doctor-service", r -> r.path("/api/v1/doctor/**")
                        .filters(f -> f
                                .circuitBreaker(c -> c.setName("doctorCircuitBreaker").setFallbackUri("forward:/fallback/doctor"))
                                .filter(new RemoveDuplicateHeadersFilter())
                                .filter((exchange, chain) -> {
                                    if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
                                        exchange.getResponse().setStatusCode(HttpStatus.OK);
                                        return exchange.getResponse().setComplete();
                                    }
                                    return chain.filter(exchange);
                                })
                        )
                        .uri("http://doctor-service:8080"))

                .route("patient-service", r -> r.path("/api/v1/patient/**")
                        .filters(f -> f
                                .circuitBreaker(c -> c.setName("patientCircuitBreaker").setFallbackUri("forward:/fallback/patient"))
                                .filter(new RemoveDuplicateHeadersFilter())
                                .filter((exchange, chain) -> {
                                    if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
                                        exchange.getResponse().setStatusCode(HttpStatus.OK);
                                        return exchange.getResponse().setComplete();
                                    }
                                    return chain.filter(exchange);
                                })
                        )
                        .uri("http://patient-service:8080"))

                .route("appointment-service", r -> r.path("/api/v1/appointments/**")
                        .filters(f -> f
                                .circuitBreaker(c -> c.setName("appointmentServiceCircuitBreaker").setFallbackUri("forward:/fallback/appointment"))
                                .filter(new RemoveDuplicateHeadersFilter())
                                .filter((exchange, chain) -> {
                                    if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
                                        exchange.getResponse().setStatusCode(HttpStatus.OK);
                                        return exchange.getResponse().setComplete();
                                    }
                                    return chain.filter(exchange);
                                })
                        )
                        .uri("http://appointment-service:8080"))

                .route("auth-service", r -> r.path("/api/auth/**")
                        .filters(f -> f
                                .circuitBreaker(c -> c.setName("authCircuitBreaker").setFallbackUri("forward:/fallback/auth"))
                                .filter(new RemoveDuplicateHeadersFilter())
                                .filter((exchange, chain) -> {
                                    if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
                                        exchange.getResponse().setStatusCode(HttpStatus.OK);
                                        return exchange.getResponse().setComplete();
                                    }
                                    return chain.filter(exchange);
                                })
                        )
                        .uri("http://auth-service:8080"))

                .build();
    }
}

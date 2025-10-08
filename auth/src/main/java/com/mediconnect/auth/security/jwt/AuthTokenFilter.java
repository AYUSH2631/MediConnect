package com.mediconnect.auth.security.jwt;


import java.io.IOException; // Import IOException for handling input/output exceptions

import com.mediconnect.auth.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain; // Import FilterChain for handling filter chains
import jakarta.servlet.ServletException; // Import ServletException for servlet-related exceptions
import jakarta.servlet.http.HttpServletRequest; // Import HttpServletRequest for handling HTTP requests
import jakarta.servlet.http.HttpServletResponse; // Import HttpServletResponse for handling HTTP responses

import org.slf4j.Logger; // Import Logger for logging errors and information
import org.slf4j.LoggerFactory; // Import LoggerFactory for creating Logger instances
import org.springframework.beans.factory.annotation.Autowired; // Import Autowired for dependency injection
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Import for creating authentication tokens
import org.springframework.security.core.context.SecurityContextHolder; // Import for managing security context
import org.springframework.security.core.userdetails.UserDetails; // Import for user details
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // Import for authentication details
import org.springframework.util.StringUtils; // Import StringUtils for string utility methods
import org.springframework.web.filter.OncePerRequestFilter; // Import OncePerRequestFilter to ensure the filter is applied once per request


public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}


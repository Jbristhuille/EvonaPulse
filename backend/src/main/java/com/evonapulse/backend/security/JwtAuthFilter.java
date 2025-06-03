package com.evonapulse.backend.security;

import com.evonapulse.backend.entities.UserEntity;
import com.evonapulse.backend.exceptions.ApiErrorBuilder;
import com.evonapulse.backend.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    private boolean isPublicPath(String path) {
        return path.startsWith("/api/auth/login") || path.startsWith("/api/auth/register") || path.startsWith("/api/ingest/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            writeJsonError(response, "Missing Authorization header", HttpStatus.UNAUTHORIZED, path);
            return;
        }

        final String token = authHeader.substring(7);
        if (!jwtService.isTokenValid(token)) {
            writeJsonError(response, "Invalid or expired token", HttpStatus.UNAUTHORIZED, path);
            return;
        }

        final String userId = jwtService.getUserIdFromToken(token);
        UserEntity user = userRepository.findById(UUID.fromString(userId)).orElse(null);
        if (user == null) {
            writeJsonError(response, "User not found", HttpStatus.UNAUTHORIZED, path);
            return;
        }

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private void writeJsonError(HttpServletResponse response, String message, HttpStatus status, String path) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        var error = new ApiErrorBuilder(status, message, path);
        objectMapper.writeValue(response.getWriter(), error);
    }
}

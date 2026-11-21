package com.example.ubersocketservice.interceptors;

import com.example.ubersocketservice.services.JwtService;
import io.jsonwebtoken.Claims;
import org.jspecify.annotations.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;

    public JwtChannelInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Message<?> preSend(
            @NonNull Message<?> message,
            @NonNull MessageChannel channel
    ) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(
                        message,
                        StompHeaderAccessor.class
                );

        if (accessor == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String authorization =
                    accessor.getFirstNativeHeader("Authorization");

            if (authorization == null ||
                    !authorization.startsWith("Bearer ")) {
                throw new IllegalArgumentException(
                        "Missing or invalid Authorization header"
                );
            }

            String token = authorization.substring(7);

            Claims claims = jwtService.validateAndExtractClaims(token);

            String subject = claims.getSubject();

            if (subject == null || subject.isBlank()) {
                throw new IllegalArgumentException("Missing userId claim");
            }

            Long userId = Long.valueOf(subject);

            String email = claims.get("email", String.class);

            if (email == null || email.isBlank()) {
                throw new IllegalArgumentException("Missing email claim");
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    );

            authentication.setDetails(email);
            accessor.setUser(authentication);
        }

        return message;
    }
}

package com.example.demo.security;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

/**
 * Reads the signed-in user id out of the SecurityContext.
 *
 * <p>{@link JwtAuthenticationFilter} puts the user's UUID in as the principal, so controllers can
 * pull it out without a database round-trip. Public GET endpoints may be hit with no token at all —
 * use {@link #id(Authentication)} there and {@link #required(Authentication)} on write endpoints.
 */
public final class CurrentUser {

    private CurrentUser() {
    }

    /** User id, or {@code null} when the request has no valid Bearer token. */
    public static UUID id(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UUID userId)) {
            return null;
        }
        return userId;
    }

    /** User id, or 401 when the request has no valid Bearer token. */
    public static UUID required(Authentication authentication) {
        UUID userId = id(authentication);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sign in to continue");
        }
        return userId;
    }
}

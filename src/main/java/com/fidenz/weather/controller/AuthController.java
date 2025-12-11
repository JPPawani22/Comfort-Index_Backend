package com.fidenz.weather.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and user management")
public class AuthController {

    @GetMapping("/profile")
    @Operation(
            summary = "Get user profile",
            description = "Returns the authenticated user's profile information"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user profile")
    public ResponseEntity<Map<String, Object>> getUserProfile(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> profile = Map.of(
                "sub", jwt.getSubject(),
                "email", jwt.getClaimAsString("email"),
                "name", jwt.getClaimAsString("name"),
                "picture", jwt.getClaimAsString("picture"),
                "issuer", jwt.getIssuer(),
                "issuedAt", jwt.getIssuedAt(),
                "expiresAt", jwt.getExpiresAt()
        );
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/test-login")
    @Operation(
            summary = "Test authentication endpoint",
            description = "Endpoint to test if authentication is working properly"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Map<String, String>> testLogin() {
        return ResponseEntity.ok(Map.of(
                "message", "Authentication successful",
                "status", "SUCCESS"
        ));
    }
}
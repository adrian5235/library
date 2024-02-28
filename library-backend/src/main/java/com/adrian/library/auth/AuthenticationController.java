package com.adrian.library.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8081")
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationServiceImpl service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PatchMapping("/changePassword")
    ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal principal)
            throws WrongCurrentPasswordException, WrongConfirmationPasswordException {
        service.changePassword(request, principal.getName());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/resetPasswordRequest")
    ResponseEntity<?> resetPasswordRequest(@RequestParam String email) {
        service.resetPassword(email);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/resetPassword")
    ResponseEntity<?> resetPassword(@RequestParam String token, @RequestBody ResetPasswordRequest request)
            throws WrongConfirmationPasswordException {
        service.resetPassword(token, request);
        return ResponseEntity.ok().build();
    }
}

package com.adrian.library.auth;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    void changePassword(ChangePasswordRequest request, String username)
            throws WrongCurrentPasswordException, WrongConfirmationPasswordException;
    void resetPassword(String email);
    void resetPassword(String token, ResetPasswordRequest request) throws WrongConfirmationPasswordException;
}

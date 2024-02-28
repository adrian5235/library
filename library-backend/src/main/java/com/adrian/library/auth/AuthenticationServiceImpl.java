package com.adrian.library.auth;

import com.adrian.library.config.Constants;
import com.adrian.library.config.JwtServiceImpl;
import com.adrian.library.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender mailSender;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
            .name(request.getFirstName() + " " + request.getLastName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .actionPoints(Constants.actionPoints)
            .build();
        repository.save(user);
        var jwt = jwtService.generateToken(user, false);
        var id = user.getId();
        var role = user.getRole();
        var name = user.getName();
        return AuthenticationResponse.builder()
                .token(jwt)
                .userId(id)
                .role(role)
                .name(name)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwt = jwtService.generateToken(user, false);
        var id = user.getId();
        var role = user.getRole();
        var name = user.getName();
        return AuthenticationResponse.builder()
            .token(jwt)
            .userId(id)
            .role(role)
            .name(name)
            .build();
    }

    @Override
    public void changePassword(ChangePasswordRequest request, String username)
            throws WrongCurrentPasswordException, WrongConfirmationPasswordException {
        User user = repository.findByEmail(username).get();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new WrongCurrentPasswordException();
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new WrongConfirmationPasswordException();
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);
    }

    @Override
    public void resetPassword(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        String token = jwtService.generateToken(repository.findByEmail(email).get(), true);
        String link = "http://localhost:8081/password/reset?token=" + token;

        message.setFrom(Constants.email);
        message.setTo(email);
        message.setSubject("Przypomnienie hasła");
        message.setText("Kliknij w link, aby zresetować hasło: " + link);
        mailSender.send(message);
    }

    @Override
    public void resetPassword(String token, ResetPasswordRequest request) throws WrongConfirmationPasswordException {
        if (!jwtService.isTokenExpired(token)) {

            // check if the two new passwords are the same
            if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
                throw new WrongConfirmationPasswordException();
            }

            String email = jwtService.extractUsername(token);
            User user = repository.findByEmail(email).get();
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            repository.save(user);
        }
    }
}

package ru.test.task.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.test.task.config.JwtService;
import ru.test.task.dto.AuthenticationRequest;
import ru.test.task.dto.RegisterRequest;
import ru.test.task.exception.AunthenticationException;
import ru.test.task.models.User;
import ru.test.task.security.ImpUserDetails;
import ru.test.task.services.UserService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager manager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .userName(request.getUserName())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .interests(request.getInterests())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userService.saveOnlyUser(user);
        var jwtToken = jwtService.generateToken(new ImpUserDetails(user));
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        manager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
        var user = userService.findByUsername(request.getUserName()).orElseThrow(() -> new AunthenticationException("incorrect username"));
        var jwtToken = jwtService.generateToken(new ImpUserDetails(user));
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}

package com.codeup.venuecatalog_rest.aplication.service;

import com.codeup.venuecatalog_rest.infraestructura.adapters.out.jpa.Role;
import com.codeup.venuecatalog_rest.infraestructura.adapters.out.jpa.UserEntity;
import com.codeup.venuecatalog_rest.infraestructura.adapters.out.jpa.UserRepository;
import com.codeup.venuecatalog_rest.infraestructura.dto.AuthenticationRequest;
import com.codeup.venuecatalog_rest.infraestructura.dto.AuthenticationResponse;
import com.codeup.venuecatalog_rest.infraestructura.dto.RegisterRequest;
import com.codeup.venuecatalog_rest.infraestructura.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}

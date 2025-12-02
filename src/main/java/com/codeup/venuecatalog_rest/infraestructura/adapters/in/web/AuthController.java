package com.codeup.venuecatalog_rest.infraestructura.adapters.in.web;

import com.codeup.venuecatalog_rest.aplication.service.AuthService;
import com.codeup.venuecatalog_rest.infraestructura.dto.AuthenticationRequest;
import com.codeup.venuecatalog_rest.infraestructura.dto.AuthenticationResponse;
import com.codeup.venuecatalog_rest.infraestructura.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}

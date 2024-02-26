package com.sedefproject.webpage.controller;
import com.sedefproject.webpage.businnes.concretes.AuthenticationService;
import com.sedefproject.webpage.exception.NotFoundException;
import com.sedefproject.webpage.request.AuthenticationRequest;
import com.sedefproject.webpage.request.RegisterRequest;
import com.sedefproject.webpage.response.AuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth") //buradaki apiler için token gerektiğini belirtilmiş oluyoruz.
@Tag(name = "Auth controller :) ")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) throws NotFoundException {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) throws NotFoundException {
        return service.authenticate(request);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }

    @GetMapping(path = "confirm")
    public String activationLink(@RequestParam("token") String token) {
        return service.activationLink(token);
    }

    @GetMapping("/user")
    public String getUserString() {
        return "This is USER!";
    }

    @GetMapping("/admin")
    public String getAdminString() {
        return "This is ADMIN!";
    }

    @Operation(
            description = "get endpoint for auth controller",
            summary ="this is a summary for management get endpoint" ,
            responses = {
                    @ApiResponse(
                            description = "succes :) oley",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Inavalid  not lucky day",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping("/welcome")
    public String welcome() {
        return "Hello baby";
    }


}

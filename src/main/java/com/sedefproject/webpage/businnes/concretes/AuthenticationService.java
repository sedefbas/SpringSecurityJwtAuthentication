package com.sedefproject.webpage.businnes.concretes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sedefproject.webpage.email.EmailTestService;
import com.sedefproject.webpage.email.MailService;
import com.sedefproject.webpage.exception.NotFoundException;
import com.sedefproject.webpage.exception.UserNotEnabledException;
import com.sedefproject.webpage.model.User;
import com.sedefproject.webpage.repository.UserRepository;
import com.sedefproject.webpage.request.AuthenticationRequest;
import com.sedefproject.webpage.request.RegisterRequest;
import com.sedefproject.webpage.response.AuthenticationResponse;
import com.sedefproject.webpage.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Service
@Slf4j
public class AuthenticationService {
    private AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final UserRepository repository;
    private TokenService tokenService;
    private EmailTestService emailService;
    private MailService mailService;

    public AuthenticationService(AuthenticationManager authenticationManager, UserService userService,
                                 JwtService jwtService, UserRepository repository,
                                 TokenService tokenService, EmailTestService emailService, MailService mailService)
    {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
        this.repository = repository;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.mailService = mailService;
    }

    public AuthenticationResponse register(RegisterRequest request) throws NotFoundException {

       AuthenticationResponse response =new AuthenticationResponse();
       //mail dogru formatta olmassı
        if (!emailService.test(request.getEmail())) {
            throw new IllegalArgumentException("Email is not written in the correct format.");
        }
       //database kontrolü
        if(userService.getUserByEmail(request.getEmail()) !=null  ){
            throw new IllegalArgumentException("The email is already in use.");
        }
        //token işlemleri
        User savedUser = userService.addUser(request);
        String token = jwtService.generateToken(request.getEmail());
        String refreshToken = jwtService.generateRefreshToken(request.getEmail()); //yeni
        tokenService.addToken(savedUser,token);
        //mail gönderimi
        String link = "http://localhost:8080/auth/confirm?token=" + token;
        mailService.send(request.getEmail(), mailService.buildAuthEmail(request.getFirstName(), link));
        //response işlemleri
        response.setAccessToken("Bearer " + token);
        response.setUserId(savedUser.getId());
        response.setMessage("kayıt basarili");
        response.setRefreshToken("Bearer " + refreshToken);
        return response;
    }

//ilk kayıtta user etkinleştirilmemişken, enabled degeri true olacak ve etkinleşecek.
public String activationLink(String token ) {
    String userEmail = jwtService.extractUser(token);
    try {
        userService.enableUser(userEmail);
        return "confirmed";
    } catch (Exception e) {
        e.printStackTrace();
        return "error activaitonlink";
    }
}

//burada aktif giriş yapılırken üyenin aktiflik durumunu kontrol etmemiz gerekecek. onuda sonradan ekledim.
public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) {
    try {
        User user = userService.getUserByEmail(request.getEmail());
        if (!user.getIsEnabled()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthenticationResponse("Kullanıcı etkinleştirilmemiş. lütfen mailinize gelen linke tıklayınız.", null, null, null));
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        String token = jwtService.generateToken(request.getEmail());
        String refreshToken = jwtService.generateRefreshToken(request.getEmail());
        tokenService.revokeAllUserTokens(user);
        tokenService.addToken(user, token);

        AuthenticationResponse response = new AuthenticationResponse();
        response.setAccessToken("Bearer " + token);
        response.setMessage("Giriş başarılı");
        response.setUserId(user.getId());
        response.setRefreshToken("Bearer " + refreshToken);

        return ResponseEntity.ok(response);

    } catch (BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new AuthenticationResponse("kimlik bilgileri hatalı.", null, null, null));
    } catch (UserNotEnabledException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new AuthenticationResponse("Kullanıcı etkinleştirilmemiş.", null, null, null));
    }
}




    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return ;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUser(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.validateToken(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user.getEmail());
                tokenService.revokeAllUserTokens(user);
                tokenService.addToken(user, accessToken);

                AuthenticationResponse authResponse = new AuthenticationResponse();
                authResponse.setAccessToken(accessToken);
                authResponse.setRefreshToken(refreshToken);
                authResponse.setMessage("giris basarili");
                authResponse.setUserId(user.getId());

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }



}

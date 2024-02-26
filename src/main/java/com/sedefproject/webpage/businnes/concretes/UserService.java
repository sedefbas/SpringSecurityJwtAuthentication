package com.sedefproject.webpage.businnes.concretes;
import com.sedefproject.webpage.businnes.abstracts.IUser;
import com.sedefproject.webpage.email.MailService;
import com.sedefproject.webpage.exception.NotFoundException;
import com.sedefproject.webpage.model.User;
import com.sedefproject.webpage.repository.UserRepository;
import com.sedefproject.webpage.request.ChangePasswordRequest;
import com.sedefproject.webpage.request.ResetPasswordRequest;
import com.sedefproject.webpage.request.RegisterRequest;
import com.sedefproject.webpage.security.JwtService;
import com.sedefproject.webpage.security.SecurityContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class UserService implements IUser {
    private UserRepository userRepository;
    private MailService mailService;
    private BCryptPasswordEncoder passwordEncoder;

    private SecurityContextUtil securityContextUtil;

    private TokenService tokenService;
    private JwtService jwtService;

    public UserService(UserRepository userRepository, MailService mailService, BCryptPasswordEncoder passwordEncoder,
                       SecurityContextUtil securityContextUtil, TokenService tokenService, JwtService jwtService) {
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
        this.securityContextUtil = securityContextUtil;
        this.tokenService = tokenService;
        this.jwtService = jwtService;
    }

    @Override
    public User addUser(RegisterRequest request) throws NotFoundException {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAuthorities(request.getAuthorities());
        user.setAccountNonExpired(true); //süresi dolmadı
        user.setAccountNonLocked(true); // hesap kilitli değil
        user.setCredentialsNonExpired(true); //şifre süresi daha dolmadı.
        user.setIsEnabled(false);  //veri tabanına kaydettik fakat üyelik aktif değil halen.
        if (user != null) return userRepository.save(user);
        else throw new NotFoundException("user is null..");
    }

    @Override
    public User getOneUserById(int id) throws NotFoundException {
        return userRepository.findById((long) id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        return null;
    } //hata fırtlatmaman lazım burda. çünkü autServicede hata alırsın.  :) biz kodu ona göre yazdık. unutma sedef.

    @Override
    public void deleteUserById(int id) {
        userRepository.deleteById((long) id);
    }


    //sonradan user etkinleştirme özelliği için ekledim.
    public int enableUser(String email) {
        return userRepository.enableAppUser(email);
    }

    public ResponseEntity<String> changePassword(ChangePasswordRequest request) {
        try {
            String userEmail = securityContextUtil.getCurrentUserEmail();
            Optional<User> user = userRepository.findByEmail(userEmail);

            if (user.isPresent() && passwordEncoder.matches(request.getCurrentPassword(), user.get().getPassword())) {
                String newPassword = request.getNewPassword();
                String confirmPassword = request.getConfirmationPassword();
                if (newPassword.equals(confirmPassword)) {
                    String hashNewPassword = passwordEncoder.encode(newPassword);
                    user.get().setPassword(hashNewPassword); // Hashlanmış yeni şifre kullanılmalı
                    userRepository.save(user.get());
                    return ResponseEntity.ok("Password changed successfully");
                } else {
                    return ResponseEntity.badRequest().body("New password and confirmation password do not match");
                }
            } else {
                return ResponseEntity.badRequest().body("Incorrect current password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
        }
    }

    //resetleme işlemi için maile link göndereceğiz
    //todo token geçerliliğini kontrol et
    public ResponseEntity<String> forgetPasswordSend(String email) { //permitAll
        try {
            Optional<User> opUser = userRepository.findByEmail(email);
            if (opUser.isPresent()) {
                User user = opUser.get();
                String token = tokenService.findLastTokenByUserId(user.getId());
                var isvalidToken = tokenService.tokenRepository.findByToken(token)
                        .map(t -> !t.isExpired() && !t.isRevoked())
                        .orElse(false);
                if (token != null && isvalidToken) {
                    String link = "http://localhost:8080/user/reset-password?token=" + token;
                    mailService.send(email, mailService.buildResetEmail(user.getFirstName(), link));
                    return ResponseEntity.ok("Reset password email sent successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Token not found for user or token is expired: " + email);
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with email: " + email);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
        }
    }

    //linke tıklandıgında bir sayfaya yönlendirmeyi planlıyorum. o sayfada bu istek kullanılabilecek sadece.
    //şifre değişikliği gerçekleştikten sonra token geçersiz olacak.
    public ResponseEntity<String> resetPassword(String token, ResetPasswordRequest request) {
        try {
            var isvalidToken = tokenService.tokenRepository.findByToken(token)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
            if(isvalidToken){
                if (request.getNewPassword().equals(request.getConfirmationPassword())) {
                    String hashNewPassword = passwordEncoder.encode(request.getNewPassword());
                    String userName = jwtService.extractUser(token);
                    Optional<User> user = userRepository.findByEmail(userName);
                    user.get().setPassword(hashNewPassword);
                    userRepository.save(user.get());
                    tokenService.revokeAllUserTokens(user.get());
                    return ResponseEntity.ok("Password reset successfully, please login to the system.");
                } else {
                    return ResponseEntity.badRequest().body("New password and confirmation password do not match");
                }
            }else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("token is not valid");
        }
         catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
        }
    }
}
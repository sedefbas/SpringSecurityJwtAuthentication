package com.sedefproject.webpage.security;

import com.sedefproject.webpage.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
//bu sınıf oturum açıkken o kişinin kimliğini tanımlıyor. çokta complex birşeyi yok. securityContex biz zaten kaydetmiştik. kimlikleri ordan alıyor.
@Component
public class SecurityContextUtil {

    public String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User) {
            return ((User) principal).getEmail();
        } else {
            return principal.toString();
        }
    }
}
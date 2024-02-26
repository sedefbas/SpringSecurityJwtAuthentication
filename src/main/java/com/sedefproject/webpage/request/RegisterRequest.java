package com.sedefproject.webpage.request;

import com.sedefproject.webpage.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
public class RegisterRequest implements Request {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    Set<Role> authorities;

}


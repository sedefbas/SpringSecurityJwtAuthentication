package com.sedefproject.webpage.businnes.abstracts;

import com.sedefproject.webpage.exception.NotFoundException;
import com.sedefproject.webpage.model.User;
import com.sedefproject.webpage.request.RegisterRequest;

import java.util.List;

public interface IUser {
    User addUser(RegisterRequest request) throws NotFoundException;
    User getOneUserById(int id) throws NotFoundException;
    List<User> getAllUsers();
    User getUserByEmail(String name) throws NotFoundException;
    void deleteUserById(int id);
}

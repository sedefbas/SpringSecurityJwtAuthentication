package com.sedefproject.webpage.repository;

import com.sedefproject.webpage.model.Token;
import com.sedefproject.webpage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String userName);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.isEnabled = TRUE WHERE u.email = ?1")
    int  enableAppUser(String email);
   //modifying oldugunda dönüş tipi için boolean kullanamıyomuşuz o yüzden ya int yada void yapmamız gerek arkadaslar.
}

package com.sedefproject.webpage.repository;

import com.sedefproject.webpage.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("SELECT t FROM Token t JOIN t.user u WHERE u.id = :id AND (t.expired = false OR t.revoked = false)")
    List<Token> findAllValidTokenByUser(Long id);

    Optional<Token> findByToken(String token);

    @Query("SELECT t.token FROM Token t WHERE t.user.id = :userId ORDER BY t.id DESC LIMIT 1")
    String findLastTokenByUserId(@Param("userId") Long userId);
}

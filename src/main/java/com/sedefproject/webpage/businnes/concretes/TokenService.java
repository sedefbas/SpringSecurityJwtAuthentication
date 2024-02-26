package com.sedefproject.webpage.businnes.concretes;

import com.sedefproject.webpage.model.Token;
import com.sedefproject.webpage.model.TokenType;
import com.sedefproject.webpage.model.User;
import com.sedefproject.webpage.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TokenService {
    TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void addToken(User user,String token){
        Token savedToken = new Token();
        savedToken.setToken(token);
        savedToken.setUser(user);
        savedToken.setTokenType(TokenType.BEARER);
        savedToken.setExpired(false);
        savedToken.setRevoked(false);
        tokenRepository.save(savedToken);
    }


    //eksi tokenları geçersiz kıldık
    public void revokeAllUserTokens(User user){
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId()); //int türüne dönüştürür
        if(validUserTokens.isEmpty()){
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true); //süresi doldu
            token.setRevoked(true); //iptal edildi;
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public  String findLastTokenByUserId(Long id){
      return   tokenRepository.findLastTokenByUserId(id);
    }


    }

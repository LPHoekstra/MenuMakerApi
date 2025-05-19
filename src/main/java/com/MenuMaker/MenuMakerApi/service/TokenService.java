package com.MenuMaker.MenuMakerApi.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.MenuMaker.MenuMakerApi.model.TokenBlacklistModel;
import com.MenuMaker.MenuMakerApi.repository.TokenBlacklistRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenService {
    private final SecretKey secretKey;

    private final TokenBlacklistRepository tokenBlacklistRepository;

    public TokenService(@Value("${secretKey}") String secret, TokenBlacklistRepository tokenBlacklistRepository) {
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    /**
     * add a token to the blacklist in the DB.
     * 
     * @param token
     * @return the saved entity, will never be null.
     */
    public TokenBlacklistModel addTokenToBlacklist(String token) {
        TokenBlacklistModel tokenBlacklistModel = new TokenBlacklistModel();
        tokenBlacklistModel.setToken(token);
        tokenBlacklistModel.setCreationDate(new Date());

        return tokenBlacklistRepository.save(tokenBlacklistModel);
    }

    /**
     * check if token is blacklisted in the DB
     * 
     * @param token
     * @return true if is in the DB, otherwise return false
     */
    public boolean isTokenBlacklisted(String token) {
        Optional<TokenBlacklistModel> isTokenBlacklisted = tokenBlacklistRepository.findByToken(token);

        if (isTokenBlacklisted.isPresent()) {
            return true;
        }

        return false;
    }

    @Scheduled(fixedRate = 30 * 60 * 1000)
    private void clearExpiredTokenInBlacklist() {
        List<TokenBlacklistModel> completeBlacklist = tokenBlacklistRepository.findAll();

        List<TokenBlacklistModel> expiredTokenList = new ArrayList<TokenBlacklistModel>();

        // 6 hours
        long validTime = 6 * 60 * 60 * 1000L;
        for (TokenBlacklistModel tokenBlacklistModel : completeBlacklist) {
            long creationDate = tokenBlacklistModel.getCreationDate().getTime();

            long expirationDate = new Date().getTime() + validTime;

            if (creationDate < expirationDate) {
                expiredTokenList.add(tokenBlacklistModel);
            }
        }

        tokenBlacklistRepository.deleteAll(expiredTokenList);
    }

    /**
     * 
     * @param email to put in the token
     * @return the token with an expiration of 10min
     */
    public String shortTimeToken(String email) {
        log.debug("Creating a short time token for email: {}", email);
        final Date expirationDate = new Date(new Date().getTime() + 10 * 60 * 1000L);

        return createToken(email, expirationDate);
    }

    /**
     * 
     * @param email to put in the token
     * @return the token with an expiration of 6 hours
     */
    public String longTimeToken(String email) {
        log.debug("Creating a 6 hours expiration token for email: {}", email);
        final Date expirationDate = new Date(new Date().getTime() + 6 * 60 * 60 * 1000L);

        return createToken(email, expirationDate);
    }

    /**
     * check if the token is blacklisted
     * 
     * @param token to get the email from
     * @return the email
     */
    public String getEmailFromToken(String token) {
        log.debug("Getting email from token: {}", token);
        if (isTokenBlacklisted(token)) {
            throw new AccessDeniedException("Token is blacklisted");
        }

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * 
     * @param email          of the user to put in the token
     * @param expirationDate of the token
     * @return the token
     */
    private String createToken(String email, Date expirationDate) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }
}

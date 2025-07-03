package com.MenuMaker.MenuMakerApi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.MenuMaker.MenuMakerApi.model.DAO.TokenBlacklistModel;
import com.MenuMaker.MenuMakerApi.repository.TokenBlacklistRepository;

@Service
public class TokenBlacklistService {

    private final TokenBlacklistRepository tokenBlacklistRepository;

    public TokenBlacklistService(TokenBlacklistRepository tokenBlacklistRepository) {
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    /**
     * add a token to the blacklist in the DB.
     * @param token
     * @return the saved entity, will never be null.
     */
    public TokenBlacklistModel addTokenToBlacklist(String token) {
        TokenBlacklistModel tokenBlacklistModel = new TokenBlacklistModel.Builder().token(token).build();

        return tokenBlacklistRepository.save(tokenBlacklistModel);
    }

    /**
     * check if token is blacklisted in the DB
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

    /**
     * clear expired token in DB every 30min
     */
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
}

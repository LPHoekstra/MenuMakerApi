package com.MenuMaker.MenuMakerApi.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.MenuMaker.MenuMakerApi.model.DAO.TokenBlacklistModel;

public interface TokenBlacklistRepository extends MongoRepository<TokenBlacklistModel, String> {
    Optional<TokenBlacklistModel> findByToken(String token);
}

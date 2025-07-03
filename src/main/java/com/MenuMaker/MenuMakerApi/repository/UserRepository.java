package com.MenuMaker.MenuMakerApi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.MenuMaker.MenuMakerApi.model.DAO.UserModel;

public interface UserRepository extends MongoRepository<UserModel, String> {
    boolean existsByEmail(String email);
}

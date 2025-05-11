package com.MenuMaker.MenuMakerApi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.MenuMaker.MenuMakerApi.model.MenuModel;

public interface MenuRepository extends MongoRepository<MenuModel, String> {
    List<MenuModel> findAllByUserEmail(String userEmail);
}

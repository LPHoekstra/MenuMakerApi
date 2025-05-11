package com.MenuMaker.MenuMakerApi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.MenuMaker.MenuMakerApi.model.MenuDataModel;

public interface MenuRepository extends MongoRepository<MenuDataModel, String> {
    List<MenuDataModel> findAllByUserEmail(String userEmail);
}

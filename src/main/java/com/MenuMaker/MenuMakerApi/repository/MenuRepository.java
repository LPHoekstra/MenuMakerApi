package com.MenuMaker.MenuMakerApi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.MenuMaker.MenuMakerApi.model.DAO.MenuModel;

public interface MenuRepository extends MongoRepository<MenuModel, String> {
    List<MenuModel> findAllByUserEmail(String userEmail);
}

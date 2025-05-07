package com.MenuMaker.MenuMakerApi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.MenuMaker.MenuMakerApi.model.MenuDataModel;

public interface MenuRepository extends MongoRepository<MenuDataModel, String> {

}

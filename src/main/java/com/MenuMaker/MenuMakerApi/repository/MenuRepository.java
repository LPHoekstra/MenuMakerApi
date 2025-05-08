package com.MenuMaker.MenuMakerApi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.MenuMaker.MenuMakerApi.model.MenuDataModel;

public interface MenuRepository extends MongoRepository<MenuDataModel, String> {
    @Query("{ 'userEmail ' : ?0 }")
    List<MenuDataModel> findAllByUserEmail(String userEmail);
}

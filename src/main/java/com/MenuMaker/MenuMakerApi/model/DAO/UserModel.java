package com.MenuMaker.MenuMakerApi.model.DAO;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class UserModel {
    @Id
    private String id;
    private String email;
    private String restaurantName;
    private Date createdAt;
    private Date updatedAt;
    // add an img

    private UserModel(Builder builder) {
        this.email = builder.email;
        this.restaurantName = builder.restaurantName;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static class Builder {
        private String email;
        private String restaurantName = null;
        private Date createdAt = new Date();
        private Date updatedAt = new Date();
        
        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder restaurantName(String name) {
            this.restaurantName = name;
            return this;
        }

        public UserModel build() {
            return new UserModel(this);
        }
    }

    public String getId() {
        return this.id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantName() {
        return this.restaurantName;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}

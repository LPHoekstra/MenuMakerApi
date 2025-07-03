package com.MenuMaker.MenuMakerApi.model.DAO;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;

@Document(collection = "tokenBlacklist")
public class TokenBlacklistModel {
    @Id
    private String id;
    private String token;
    private Date creationDate;

    private TokenBlacklistModel(Builder builder) {
        this.token = builder.token;
        this.creationDate = builder.creationDate;
    }

    public static class Builder {
        @NotNull
        private String token;
        private Date creationDate = new Date();

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder creationDate(Date creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public TokenBlacklistModel build() {
            return new TokenBlacklistModel(this);
        }
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}

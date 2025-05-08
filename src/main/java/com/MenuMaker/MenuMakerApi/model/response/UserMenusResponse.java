package com.MenuMaker.MenuMakerApi.model.response;

import java.util.Date;

public class UserMenusResponse {
    private String id;
    private Date creationDate;
    // private String imgLink;

    public UserMenusResponse(String id, Date creationDate) {
        this.id = id;
        this.creationDate = creationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}

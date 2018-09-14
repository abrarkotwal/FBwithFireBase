package com.abrarkotwal.facebookdemo.Adapter.Pojo;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class FBLike {
    public String email;

    public FBLike() {
    }

    public FBLike(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
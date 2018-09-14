package com.abrarkotwal.facebookdemo.Adapter.Pojo;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Comment {
    public String comment,email;

    public Comment(){

    }
    public Comment(String comment, String email) {
        this.comment = comment;
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}

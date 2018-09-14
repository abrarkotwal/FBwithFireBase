package com.abrarkotwal.facebookdemo.Adapter.Pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Post implements Parcelable{
    private String postId;
    private String titleName;
    private String description;
    private String imageLink;
    private String postOwner;

    public Post(){

    }

    public Post(String postId, String titleName, String description, String imageLink,String postOwner) {
        this.postId = postId;
        this.titleName = titleName;
        this.description = description;
        this.imageLink = imageLink;
        this.postOwner = postOwner;
    }

    protected Post(Parcel in) {
        postId = in.readString();
        titleName = in.readString();
        description = in.readString();
        imageLink = in.readString();
        postOwner = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getPostOwner() {
        return postOwner;
    }

    public void setPostOwner(String postOwner) {
        this.postOwner = postOwner;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(postId);
        dest.writeString(titleName);
        dest.writeString(description);
        dest.writeString(imageLink);
        dest.writeString(postOwner);
    }
}
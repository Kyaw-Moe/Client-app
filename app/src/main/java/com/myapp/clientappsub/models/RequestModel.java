package com.myapp.clientappsub.models;

public class RequestModel {
    public String title;
    public String imageLink;

    public RequestModel() {
    }

    public RequestModel(String title, String imageLink) {
        this.title = title;
        this.imageLink = imageLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}

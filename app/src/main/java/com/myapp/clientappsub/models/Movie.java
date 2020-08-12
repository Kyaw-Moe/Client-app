package com.myapp.clientappsub.models;

public class Movie {

    public static String COLLECTION_NAME = "movies";

    private String id;
    private String title;
    private String imageUrl;
    private String videoLink;
    private String genreId;
    private String genreName;
    private String createdAt;

    public Movie() {
    }

    public Movie(String id, String title, String imageUrl, String videoLink, String genreId, String genreName, String createdAt) {

        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.videoLink = videoLink;
        this.genreId = genreId;
        this.genreName = genreName;
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
}
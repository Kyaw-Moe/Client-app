package com.myapp.clientappsub.models;

public class Series {

    public static final String COLLECTION_NAME = "series";

    private String id;
    private String title;
    private String desc;
    private int episodeCount;
    private String imageUrl;
    private String genreId;
    private String genreName;
    private String createdAt;

    public Series() {
    }

    public Series(String id, String title, String desc, int episodeCount, String imageUrl, String genreId, String genreName, String createdAt) {

        this.id = id;
        this.title = title;
        this.desc = desc;
        this.episodeCount = episodeCount;
        this.imageUrl = imageUrl;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(int episodeCount) {
        this.episodeCount = episodeCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

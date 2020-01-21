package com.example.feedapp;
import java.io.Serializable;
import java.util.ArrayList;

class Feed implements Serializable {
    private String title = "";
    private String description = "";
    private String imageURL = "";
    private ArrayList<Article> articles = new ArrayList<>();

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}

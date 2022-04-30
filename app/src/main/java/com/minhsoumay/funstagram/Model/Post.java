package com.minhsoumay.funstagram.Model;

public class Post {
    private String description;
    private String image_URL;
    private String post_id;
    private String publisher;

    public Post() {
    }

    public Post(String description, String image_URL, String post_id, String publisher) {
        this.description = description;
        this.image_URL = image_URL;
        this.post_id = post_id;
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_URL() {
        return image_URL;
    }

    public void setImage_URL(String image_URL) {
        this.image_URL = image_URL;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}

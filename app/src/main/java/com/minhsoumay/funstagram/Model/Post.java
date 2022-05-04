/*
 * This class is for our Post. Here we have various getters and setters for the
 * various components of our post in Funstagram.
 */

package com.minhsoumay.funstagram.Model;

/**
 * @author: Minh Duong
 * COURSE: CSC 317 - Spring 2022
 * @description: This file contains the Post class, which contains the information of a post
 *               to be given to from Firebase.
 */
public class Post {
    private String description;
    private String postid;
    private String postimage;
    private String publisher;

    /**
     * The blank constructor of the class, which will be used by Firebase.
     */
    public Post() {}

    /**
     * The constructor for the class with information on description, postid, postimage and
     * publisher
     * @param description
     * @param postid
     * @param postimage
     * @param publisher
     */
    public Post(String description, String postid, String postimage, String publisher) {
        this.description = description;
        this.postid = postid;
        this.postimage = postimage;
        this.publisher = publisher;
    }

    /**
     * The getter constructor for the description field
     * @return  String
     */
    public String getDescription() {
        return description;
    }

    /**
     * The setter constructor for the description field
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * The getter constructor for the postid field
     * @return  String
     */
    public String getPostid() {
        return postid;
    }

    /**
     * The setter constructor for the postid field
     * @param postid
     */
    public void setPostid(String postid) {
        this.postid = postid;
    }

    /**
     * The getter constructor for the postimage field
     * @return  String
     */
    public String getPostimage() {
        return postimage;
    }

    /**
     * The setter constructor for the postimage field
     * @param postimage
     */
    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    /**
     * The getter constructor for the publisher field
     * @return  String
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * The setter constructor for the publisherfield
     * @param publisher
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
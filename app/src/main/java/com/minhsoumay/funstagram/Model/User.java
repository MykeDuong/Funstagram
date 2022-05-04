package com.minhsoumay.funstagram.Model;

/**
 * @author: Minh Duong
 * COURSE: CSC 317 - Spring 2022
 * @description: This file contains the Post class, which contains the information of an user
 *               to be given to from Firebase.
 */
public class User {
    private String name;
    private String email;
    private String username;
    private String bio;
    private String imageurl;
    private String id;
    private String audioChoice;

    /**
     * The blank constructor of the class, which will be used by Firebase.
     */
    public User() {}

    /**
     * The constructor for the class with information on information of an user
     * to be given to from Firebase.
     * @param name
     * @param email
     * @param username
     * @param bio
     * @param imageurl
     * @param id
     * @param audioChoice
     */
    public User(String name, String email, String username, String bio, String imageurl,
                String id, String audioChoice) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.bio = bio;
        this.imageurl = imageurl;
        this.id = id;
        this.audioChoice = audioChoice;
    }

    /**
     * The getter constructor for the name field
     * @return  String
     */
    public String getName() { return name; }

    /**
     * The setter constructor for the name field
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The getter constructor for the email field
     * @return  String
     */
    public String getEmail() {
        return email;
    }

    /**
     * The setter constructor for the email field
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * The getter constructor for the username field
     * @return  String
     */
    public String getUsername() {
        return username;
    }

    /**
     * The setter constructor for the username field
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * The getter constructor for the bio field
     * @return  String
     */
    public String getBio() {
        return bio;
    }

    /**
     * The setter constructor for the bio field
     * @param bio
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * The getter constructor for the imageurl field
     * @return  String
     */
    public String getImageurl() {
        return imageurl;
    }

    /**
     * The setter constructor for the imageurl field
     * @param imageurl
     */
    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    /**
     * The getter constructor for the id field
     * @return  String
     */
    public String getId() {
        return id;
    }

    /**
     * The setter constructor for the id field
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * The getter constructor for the audioChoice field
     * @return  String
     */
    public String getAudioChoice() {
        return audioChoice;
    }

    /**
     * The setter constructor for the audioChoice field
     * @param audioChoice
     */
    public void setAudioChoice(String audioChoice) { this.audioChoice = audioChoice; }
}

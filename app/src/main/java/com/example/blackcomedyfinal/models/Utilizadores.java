package com.example.blackcomedyfinal.models;

public class Utilizadores {

    String id, nome , email, password , bio, pathImage ,image;
    Object followers, followings;

    public Object getFollowings() {
        return followings;
    }

    public void setFollowings(Object followings) {
        this.followings = followings;
    }

    public String getImage() {
        return image;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Object getFollowers() {
        return followers;
    }

    public void setFollowers(Object followers) {
        this.followers = followers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

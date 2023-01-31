package com.example.blackcomedyfinal.models;

import android.media.AudioFormat;

import java.util.Date;
import java.util.List;

public class Jokes {

    String comedyText, idJoke;

    Object user;

    List<String> likes;

    String jokeDate;
    String audioFormat;


    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public String getComedyText() {
        return comedyText;
    }

    public void setComedyText(String comedyText) {
        this.comedyText = comedyText;
    }

    public String getIdJoke() {
        return idJoke;
    }

    public void setIdJoke(String idJoke) {
        this.idJoke = idJoke;
    }

    public String getJokeDate() {
        return jokeDate;
    }

    public void setJokeDate(String jokeDate) {
        this.jokeDate = jokeDate;
    }

    public String getAudioFormat() {
        return audioFormat;
    }

    public void setAudioFormat(String audioFormat) {
        this.audioFormat = audioFormat;
    }

}

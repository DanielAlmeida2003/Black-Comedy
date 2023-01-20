package com.example.blackcomedyfinal.models;

import android.media.AudioFormat;

import java.util.Date;
import java.util.List;

public class Jokes {

    String comedyText, idJoke;

    Object user,likes;


    String jokeDate;
    AudioFormat audioFormat;



    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public Object getLikes() {
        return likes;
    }

    public void setLikes(Object likes) {
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

    public AudioFormat getAudioFormat() {
        return audioFormat;
    }

    public void setAudioFormat(AudioFormat audioFormat) {
        this.audioFormat = audioFormat;
    }

}

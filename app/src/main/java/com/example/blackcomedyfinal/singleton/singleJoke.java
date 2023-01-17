package com.example.blackcomedyfinal.singleton;
import androidx.annotation.NonNull;

import com.example.blackcomedyfinal.models.Jokes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;


public class singleJoke extends ArrayList<Jokes> {

    public static List<Jokes> joke;

    singleJoke(){
        joke = new ArrayList<>();
    }

    private static final singleJoke _INSTANCE = new singleJoke();

    public static singleJoke getInstance(){
        return _INSTANCE;
    }
    
}

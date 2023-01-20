package com.example.blackcomedyfinal.singleton;

import com.example.blackcomedyfinal.models.Jokes;

import java.util.ArrayList;
import java.util.List;

public class singletonposts extends ArrayList<Jokes> {

    public static List<Jokes> jokes;

    singletonposts(){
        jokes = new ArrayList<>();
    }

    private static final singletonposts _INSTANCE = new singletonposts();

    public static singletonposts getInstances(){
        return _INSTANCE;
    }
}

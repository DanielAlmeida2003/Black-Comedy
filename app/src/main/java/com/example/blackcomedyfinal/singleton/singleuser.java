package com.example.blackcomedyfinal.singleton;

import com.example.blackcomedyfinal.models.Utilizadores;

import java.util.ArrayList;
import java.util.List;

public class singleuser {

    public static List<Utilizadores> utilizador;

    singleuser(){
        utilizador = new ArrayList<>();
    }

    private static final singleuser _INSTANCE = new singleuser();

    public static singleuser getInstance(){
        return _INSTANCE;
    }


}

package fr.alanlg.themovieapp.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Compagnie implements Serializable {

    private int id;
    private String name;

    public Compagnie(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String name() {
        return name;
    }

}

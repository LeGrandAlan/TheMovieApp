package fr.alanlg.themovieapp.model;

import java.io.Serializable;

public class Genre implements Serializable {

    private int id;
    private String name;

    public Genre(int id, String name) {
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

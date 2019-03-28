package fr.alanlg.themovieapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import fr.alanlg.themovieapp.adapter.Member;

public class CastMember extends  Member implements Serializable {

    @SerializedName("credit_id")
    private String id;
    private String character;
    private String name;
    @SerializedName("profile_path")
    private String profileImagePath;

    public CastMember(String id, String character, String name, String profileImagePath) {
        this.id = id;
        this.character = character;
        this.name = name;
        this.profileImagePath = profileImagePath;
    }

    public String getId() {
        return id;
    }

    public String getCharacter() {
        return character;
    }

    public String getName() {
        return name;
    }

    public String getProfileImagePath() {
        if (this.profileImagePath == null) {
            return null;
        }
        return "https://image.tmdb.org/t/p/original" + profileImagePath;
    }

    @Override
    public String toString() {
        return "CastMember{" +
                "id='" + id + '\'' +
                ", character='" + character + '\'' +
                ", name='" + name + '\'' +
                ", profileImagePath='" + profileImagePath + '\'' +
                '}';
    }

}

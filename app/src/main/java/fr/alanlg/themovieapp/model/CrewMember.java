package fr.alanlg.themovieapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CrewMember implements Serializable {

    @SerializedName("credit_id")
    private String id;
    private String department;
    private String job;
    private String name;
    @SerializedName("profile_path")
    private String profileImagePath;

    public CrewMember(String id, String department, String job, String name, String profileImagePath) {
        this.id = id;
        this.department = department;
        this.job = job;
        this.name = name;
        this.profileImagePath = profileImagePath;
    }

    public String getId() {
        return id;
    }

    public String getDepartment() {
        return department;
    }

    public String getJob() {
        return job;
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
        return "CrewMember{" +
                "id='" + id + '\'' +
                ", department='" + department + '\'' +
                ", job='" + job + '\'' +
                ", name='" + name + '\'' +
                ", profileImagePath='" + profileImagePath + '\'' +
                '}';
    }

}

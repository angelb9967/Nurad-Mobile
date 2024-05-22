package com.example.nurad.Models;

public class BoardModel {
    private String imageUrl;
    private String name;
    private String position;

    public BoardModel() {
        // Empty constructor required for Firebase
    }

    public BoardModel(String imageUrl, String name, String position) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.position = position;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }
}
package com.example.nurad.Models;

public class AboutUsModel {
    private String title;
    private String description;

    public AboutUsModel() {
        // Empty constructor needed for Firebase
    }

    public AboutUsModel(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
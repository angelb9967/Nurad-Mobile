package com.example.nurad.Models;

public class HelpCenterModel {

    private String title;
    private String description;

    public HelpCenterModel() {
        // Default constructor required for calls to DataSnapshot.getValue(HelpCenterModel.class)
    }

    public HelpCenterModel(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
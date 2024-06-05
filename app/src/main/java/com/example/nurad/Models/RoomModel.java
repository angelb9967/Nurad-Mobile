package com.example.nurad.Models;

import java.io.Serializable;

public class RoomModel implements Serializable {
    private String title;
    private String name;
    private String type;
    private double price;
    private String description;
    private String imageUrl;
    private boolean depositRequired;
    private String priceRule;
    private boolean isRecommended;

    public RoomModel() {
    }

    public RoomModel(String title, String name, String type, double price, String description, String imageUrl, boolean depositRequired, String priceRule, boolean isRecommended) {
        this.title = title;
        this.name = name;
        this.type = type;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.depositRequired = depositRequired;
        this.priceRule = priceRule;
        this.isRecommended = isRecommended;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isDepositRequired() {
        return depositRequired;
    }

    public void setDepositRequired(boolean depositRequired) {
        this.depositRequired = depositRequired;
    }

    public String getPriceRule() {
        return priceRule;
    }

    public void setPriceRule(String priceRule) {
        this.priceRule = priceRule;
    }

    public boolean isRecommended() {
        return isRecommended;
    }

    public void setRecommended(boolean recommended) {
        isRecommended = recommended;
    }
}
package com.example.nurad.Models;

import java.io.Serializable;

public class RoomModel implements Serializable {

    private String title;
    private String roomName;
    private String roomType;
    private double price;
    private String description;
    private String imageUrl;
    private boolean depositRequired;
    private String priceRule;
    private boolean isRecommended;

    public RoomModel() {
    }

    public RoomModel(String title, String roomName, String roomType, double price, String description, String imageUrl, boolean depositRequired, String priceRule, boolean isRecommended) {
        this.title = title;
        this.roomName = roomName;
        this.roomType = roomType;
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

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
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
package com.example.nurad.Models;

public class Model_PriceRule {
    private double price;
    private double friday_price;
    private double saturday_price;
    private double sunday_price;

    public Model_PriceRule() {
    }

    public Model_PriceRule(double price, double friday_price, double saturday_price, double sunday_price) {
        this.price = price;
        this.friday_price = friday_price;
        this.saturday_price = saturday_price;
        this.sunday_price = sunday_price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getFriday_price() {
        return friday_price;
    }

    public void setFriday_price(double friday_price) {
        this.friday_price = friday_price;
    }

    public double getSaturday_price() {
        return saturday_price;
    }

    public void setSaturday_price(double saturday_price) {
        this.saturday_price = saturday_price;
    }

    public double getSunday_price() {
        return sunday_price;
    }

    public void setSunday_price(double sunday_price) {
        this.sunday_price = sunday_price;
    }
}
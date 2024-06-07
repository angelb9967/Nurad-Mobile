package com.example.nurad.Models;

public class VoucherModel {
    private String code;
    private String description;
    private String status;
    private String title;
    private String validity;
    private double value;  // Use double for value to ensure decimal precision

    public VoucherModel() {
        // Default constructor required for calls to DataSnapshot.getValue(VoucherModel.class)
    }

    public VoucherModel(String code, String description, String status, String title, String validity, double value) {
        this.code = code;
        this.description = description;
        this.status = status;
        this.title = title;
        this.validity = validity;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
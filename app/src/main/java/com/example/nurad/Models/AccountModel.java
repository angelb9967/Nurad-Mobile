package com.example.nurad.Models;

public class AccountModel {
    private String email;

    public AccountModel() {
    }

    public AccountModel(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

package com.example.nurad.Models;


public class Model_ContactInfo {
    private String contact_id;
    private String userId;
    private String prefix;
    private String firstName;
    private String lastName;
    private String phone;
    private String mobilePhone;
    private String email;


    // Default constructor
    public Model_ContactInfo() {
    }


    // Constructor with all fields
    public Model_ContactInfo(String contact_id, String userId, String prefix, String firstName, String lastName, String phone, String mobilePhone, String email) {
        this.contact_id = contact_id;
        this.userId = userId;
        this.prefix = prefix;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.mobilePhone = mobilePhone;
        this.email = email;
    }


    // Getter and setter methods for contact_id
    public String getContact_id() {
        return contact_id;
    }


    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }


    // Getter and setter methods for userId
    public String getUserId() {
        return userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }


    // Getter and setter methods for prefix
    public String getPrefix() {
        return prefix;
    }


    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }


    // Getter and setter methods for firstName
    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    // Getter and setter methods for lastName
    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    // Getter and setter methods for phone
    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }


    // Getter and setter methods for mobilePhone
    public String getMobilePhone() {
        return mobilePhone;
    }


    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }


    // Getter and setter methods for email
    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }
}

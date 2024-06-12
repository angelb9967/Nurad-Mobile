package com.example.nurad.Models;


public class Model_AddressInfo {
    private String address_id;
    private String userId;
    private String country;
    private String address1;
    private String address2;
    private String city;
    private String region;
    private String zipCode;


    // Default constructor
    public Model_AddressInfo() {
    }


    // Constructor with all fields
    public Model_AddressInfo(String address_id, String userId, String country, String address1, String address2, String city, String region, String zipCode) {
        this.address_id = address_id;
        this.userId = userId;
        this.country = country;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.region = region;
        this.zipCode = zipCode;
    }


    // Getter and setter methods for address_id
    public String getAddress_id() {
        return address_id;
    }


    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }


    // Getter and setter methods for userId
    public String getUserId() {
        return userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }


    // Getter and setter methods for country
    public String getCountry() {
        return country;
    }


    public void setCountry(String country) {
        this.country = country;
    }


    // Getter and setter methods for address1
    public String getAddress1() {
        return address1;
    }


    public void setAddress1(String address1) {
        this.address1 = address1;
    }


    // Getter and setter methods for address2
    public String getAddress2() {
        return address2;
    }


    public void setAddress2(String address2) {
        this.address2 = address2;
    }


    // Getter and setter methods for city
    public String getCity() {
        return city;
    }


    public void setCity(String city) {
        this.city = city;
    }


    // Getter and setter methods for region
    public String getRegion() {
        return region;
    }


    public void setRegion(String region) {
        this.region = region;
    }


    // Getter and setter methods for zipCode
    public String getZipCode() {
        return zipCode;
    }


    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}

package com.example.nurad.Models;


public class Model_PaymentInfo {
    private String payment_id;
    private String userId;
    private String cardNumber;
    private String expirationDate;
    private String cvv;
    private String nameOnTheCard;


    // Default constructor
    public Model_PaymentInfo() {
    }


    // Constructor with all fields
    public Model_PaymentInfo(String payment_id, String userId, String cardNumber, String expirationDate, String cvv, String nameOnTheCard) {
        this.payment_id = payment_id;
        this.userId = userId;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.nameOnTheCard = nameOnTheCard;
    }


    // Getter and setter methods for payment_id
    public String getPayment_id() {
        return payment_id;
    }


    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }


    // Getter and setter methods for userId
    public String getUserId() {
        return userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }


    // Getter and setter methods for cardNumber
    public String getCardNumber() {
        return cardNumber;
    }


    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }


    // Getter and setter methods for expirationDate
    public String getExpirationDate() {
        return expirationDate;
    }


    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }


    // Getter and setter methods for cvv
    public String getCvv() {
        return cvv;
    }


    public void setCvv(String cvv) {
        this.cvv = cvv;
    }


    // Getter and setter methods for nameOnTheCard
    public String getNameOnTheCard() {
        return nameOnTheCard;
    }


    public void setNameOnTheCard(String nameOnTheCard) {
        this.nameOnTheCard = nameOnTheCard;
    }
}

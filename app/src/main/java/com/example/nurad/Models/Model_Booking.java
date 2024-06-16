package com.example.nurad.Models;


import com.google.firebase.database.PropertyName;

import java.util.Map;
import java.io.Serializable;

public class Model_Booking implements Serializable {
    @PropertyName("booking_id")
    private String bookingId;
    @PropertyName("contact_id")
    private String contactId;
    @PropertyName("address_id")
    private String addressId;
    @PropertyName("payment_id")
    private String paymentId;
    private String userId;
    private String checkInDate;
    private String checkOutDate;
    private String checkInTime;
    private String checkOutTime;
    private String voucherCode;
    private double subtotalValue;
    private int adultCount;
    private int childCount;
    private String notes;
    private String room;
    private Map<String, String> selectedAddOns;

    private String bookingDate;
    private double voucherValueValue;
    private String status;
    private String roomTitle;
    private double roomPrice;
    private double extraAdultPrice;
    private double extraChildPrice;
    private double addOnsPrice;
    private double totalValue;
    private double vatValue;

    // No-argument constructor
    public Model_Booking() {
    }

    public Model_Booking(String bookingId, String contactId, String addressId, String paymentId, String userId, String checkInDate, String checkOutDate, String checkInTime, String checkOutTime, String voucherCode, double subtotalValue, int adultCount, int childCount, String notes, String room, Map<String, String> selectedAddOns, String bookingDate, double voucherValueValue, String status, double roomPrice, double extraAdultPrice, double extraChildPrice, double addOnsPrice, double totalValue, double vatValue, String roomTitle) {
        this.bookingId = bookingId;
        this.contactId = contactId;
        this.addressId = addressId;
        this.paymentId = paymentId;
        this.userId = userId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.voucherCode = voucherCode;
        this.subtotalValue = subtotalValue;
        this.adultCount = adultCount;
        this.childCount = childCount;
        this.notes = notes;
        this.room = room;
        this.selectedAddOns = selectedAddOns;
        this.bookingDate = bookingDate;
        this.voucherValueValue = voucherValueValue;
        this.status = status;
        this.roomPrice = roomPrice;
        this.extraAdultPrice = extraAdultPrice;
        this.extraChildPrice = extraChildPrice;
        this.addOnsPrice = addOnsPrice;
        this.totalValue = totalValue;
        this.vatValue = vatValue;
        this.roomTitle = roomTitle;
    }

    @PropertyName("booking_id")
    public String getBookingId() {
        return bookingId;
    }

    @PropertyName("booking_id")
    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    @PropertyName("contact_id")
    public String getContactId() {
        return contactId;
    }

    @PropertyName("contact_id")
    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    @PropertyName("address_id")
    public String getAddressId() {
        return addressId;
    }

    @PropertyName("address_id")
    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    @PropertyName("payment_id")
    public String getPaymentId() {
        return paymentId;
    }

    @PropertyName("payment_id")
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public double getSubtotalValue() {
        return subtotalValue;
    }

    public void setSubtotalValue(double subtotalValue) {
        this.subtotalValue = subtotalValue;
    }

    public int getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(int adultCount) {
        this.adultCount = adultCount;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Map<String, String> getSelectedAddOns() {
        return selectedAddOns;
    }

    public void setSelectedAddOns(Map<String, String> selectedAddOns) {
        this.selectedAddOns = selectedAddOns;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public double getVoucherValueValue() {
        return voucherValueValue;
    }

    public void setVoucherValueValue(double voucherValueValue) {
        this.voucherValueValue = voucherValueValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(double roomPrice) {
        this.roomPrice = roomPrice;
    }

    public double getExtraAdultPrice() {
        return extraAdultPrice;
    }

    public void setExtraAdultPrice(double extraAdultPrice) {
        this.extraAdultPrice = extraAdultPrice;
    }

    public double getExtraChildPrice() {
        return extraChildPrice;
    }

    public void setExtraChildPrice(double extraChildPrice) {
        this.extraChildPrice = extraChildPrice;
    }

    public double getAddOnsPrice() {
        return addOnsPrice;
    }

    public void setAddOnsPrice(double addOnsPrice) {
        this.addOnsPrice = addOnsPrice;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public double getVatValue() {
        return vatValue;
    }

    public void setVatValue(double vatValue) {
        this.vatValue = vatValue;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }
}

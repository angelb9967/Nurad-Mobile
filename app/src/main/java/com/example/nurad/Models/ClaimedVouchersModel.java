package com.example.nurad.Models;

public class ClaimedVouchersModel {
    private String code;
    private String status;
    private String claimedDate;

    public ClaimedVouchersModel() {
        // Empty constructor needed for Firebase
    }

    public ClaimedVouchersModel(String code, String status, String claimedDate) {
        this.code = code;
        this.status = status;
        this.claimedDate = claimedDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClaimedDate() {
        return claimedDate;
    }

    public void setClaimedDate(String claimedDate) {
        this.claimedDate = claimedDate;
    }

}

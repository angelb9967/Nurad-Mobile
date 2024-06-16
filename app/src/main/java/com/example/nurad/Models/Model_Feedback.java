package com.example.nurad.Models;

public class Model_Feedback {
    private String feedbackId; // New field for feedback ID
    private String userId;
    private int rating;
    private String option;
    private String message;
    private String submissionDate; // New field for submission date (formatted as yyyyMMdd)

    public Model_Feedback() {
        // Default constructor required for Firebase Realtime Database
    }

    public Model_Feedback(String feedbackId, String userId, int rating, String option, String message, String submissionDate) {
        this.feedbackId = feedbackId;
        this.userId = userId;
        this.rating = rating;
        this.option = option;
        this.message = message;
        this.submissionDate = submissionDate;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }
}
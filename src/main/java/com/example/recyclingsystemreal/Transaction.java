package com.example.recyclingsystemreal;

public abstract class Transaction {

    protected int transactionId;
    protected String studentId;
    protected String createdAt; // Kept as String for custom formatting

    // Default Constructor
    public Transaction() {
    }

    // Parameterized Constructor
    public Transaction(int transactionId, String studentId, String createdAt) {
        this.transactionId = transactionId;
        this.studentId = studentId;
        this.createdAt = createdAt;
    }

    // --- Getters and Setters ---

    public void createTransaction(){

    }
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
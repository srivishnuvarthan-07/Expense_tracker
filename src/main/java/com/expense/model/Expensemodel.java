package com.expense.model;


import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Expensemodel {
    private int Id;
    private int amount;
    private Timestamp createdAt;
    private int cateId;
    private String description;
    private String category;


    public Expensemodel(int Id, int amount, String description,Timestamp createdAt, int cateId,String category) {
        this.Id = Id;
        this.amount = amount;
        this.createdAt = createdAt;
        this.description = description;
        this.cateId = cateId;
        this.category = category;
    }


    public int getExpId() {
        return Id;
    }
    public void setExpId(int Id) {
        this.Id = Id;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    public int getCateId() {
        return cateId;
    }
    public void setCateId(int cateId) {
        this.cateId = cateId;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getCategory() {
        return category;
    }


    public void setCategory(String category) {
        this.category = category;
    }


}

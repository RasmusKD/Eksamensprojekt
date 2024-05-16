package com.minkostplan.eksamensprojekt.Model;

import java.util.Date;

public class Subscription {
    private int subscriptionId;
    private int userId;
    private Date startDate;
    private Date endDate;
    private double price;
    private String status;


    public Subscription() {
    }

    public Subscription(int subscriptionId, int userId, Date startDate, Date endDate, double price, String status) {
        this.subscriptionId = subscriptionId;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.status = status;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "subscriptionId=" + subscriptionId +
                ", userId=" + userId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", price=" + price +
                ", status='" + status + '\'' +
                '}';
    }
}

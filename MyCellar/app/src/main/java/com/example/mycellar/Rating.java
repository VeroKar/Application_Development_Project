package com.example.mycellar;

public class Rating {
    String average;
    String reviews;

    public Rating(String average, String reviews) {
        this.average = average;
        this.reviews = reviews;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "average='" + average + '\'' +
                ", reviews='" + reviews + '\'' +
                '}';
    }
}


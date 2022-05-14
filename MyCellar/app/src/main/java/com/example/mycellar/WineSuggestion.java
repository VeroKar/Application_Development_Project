package com.example.mycellar;

public class WineSuggestion {

    String winery;
    String wine;
    Rating rating;
    String location;
    String image;
    String id;

    public WineSuggestion(String winery, String wine, Rating rating, String location, String image, String id) {
        this.winery = winery;
        this.wine = wine;
        this.rating = rating;
        this.location = location;
        this.image = image;
        this.id = id;
    }

    public String getWinery() {
        return winery;
    }

    public void setWinery(String winery) {
        this.winery = winery;
    }

    public String getWine() {
        return wine;
    }

    public void setWine(String wine) {
        this.wine = wine;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "WineSuggestion{" +
                "winery='" + winery + '\'' +
                ", wine='" + wine + '\'' +
                ", rating=" + rating +
                ", location='" + location + '\'' +
                ", image='" + image + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}

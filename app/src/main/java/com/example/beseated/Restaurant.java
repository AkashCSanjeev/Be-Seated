package com.example.beseated;

public class Restaurant {

    String id,url,restName,town,state;

    String bookedTime,day;

    int noOfSeats;

    public String getId() {
        return id;
    }


    public String getDay() {
        return day;
    }

    public Restaurant(String day, String bookedTime, int noOfSeats) {
        this.bookedTime = bookedTime;
        this.noOfSeats = noOfSeats;
        this.day = day;
    }

    public Restaurant(String town, String state, String restName, String url, String id  ) {
        this.url = url;
        this.restName = restName;
        this.town = town;
        this.state = state;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }


    public String getRestName() {
        return restName;
    }

    public String getTown() {
        return town;
    }

    public String getState() {
        return state;
    }

    public String getBookedTime() {
        return bookedTime;
    }

    public int getNoOfSeats() {
        return noOfSeats;
    }


}

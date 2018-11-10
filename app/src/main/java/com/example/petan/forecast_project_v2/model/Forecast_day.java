package com.example.petan.forecast_project_v2.model;

public class Forecast_day {
    private String Date;
    private String Temperature;
    private String image_url;

    public Forecast_day(String date, String temperature, String image_url) {
        Date = date;
        Temperature = temperature;
        this.image_url = image_url;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTemperature() {
        return Temperature;
    }

    public void setTemperature(String temperature) {
        Temperature = temperature;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}

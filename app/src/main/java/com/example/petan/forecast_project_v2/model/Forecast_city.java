package com.example.petan.forecast_project_v2.model;

public class Forecast_city {
    public String cityName;
    public String country;
    public double temp_c;
    public double temp_f;
    public double wind_kph;
    public double pressure;
    public String forecast;

    public Forecast_city(String cityName, String country, double temp_c, double temp_f, double wind_kph, double pressure, String forecast) {
        this.cityName = cityName;
        this.country = country;
        this.temp_c = temp_c;
        this.temp_f = temp_f;
        this.wind_kph = wind_kph;
        this.pressure = pressure;
        this.forecast = forecast;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getTemp_c() {
        return temp_c;
    }

    public void setTemp_c(double temp_c) {
        this.temp_c = temp_c;
    }

    public double getTemp_f() {
        return temp_f;
    }

    public void setTemp_f(double temp_f) {
        this.temp_f = temp_f;
    }

    public double getWind_kph() {
        return wind_kph;
    }

    public void setWind_kph(double wind_kph) {
        this.wind_kph = wind_kph;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public String getForecast() {
        return forecast;
    }

    public void setForecast(String forecast) {
        this.forecast = forecast;
    }
}

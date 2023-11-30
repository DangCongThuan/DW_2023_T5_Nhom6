package com.group6.datawarehouse.model;

public class CountryModel {
    private int id;
    private String countryCode;
    private String countryName;
    private String region;

    public CountryModel(int id, String countryCode, String countryName, String region) {
        this.id = id;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.region = region;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}

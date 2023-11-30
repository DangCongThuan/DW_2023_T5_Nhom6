package com.group6.datawarehouse.model;

public class BankModel {
    private int id;
    private String bankName;
    private String bankCode;
    private int countryId;

    public BankModel() {
    }

    public BankModel(int id, String bankName, String bankCode, int countryId) {
        this.id = id;
        this.bankName = bankName;
        this.bankCode = bankCode;
        this.countryId = countryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}

package com.group6.datawarehouse.model;

public class Currency {
    private int id;
    private String date;
    private String dateTime;
    private String bankName;
    private String currencySymbol;
    private String currencyName;
    private double buyCash;
    private double buyTransfer;
    private double saleCash;
    private double saleTransfer;

    public Currency() {
    }

    public Currency(int id, String date, String dateTime, String bankName, String currencySymbol, String currencyName, double buyCash, double buyTransfer, double saleCash, double saleTransfer) {
        this.id = id;
        this.date = date;
        this.dateTime = dateTime;
        this.bankName = bankName;
        this.currencySymbol = currencySymbol;
        this.currencyName = currencyName;
        this.buyCash = buyCash;
        this.buyTransfer = buyTransfer;
        this.saleCash = saleCash;
        this.saleTransfer = saleTransfer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public double getBuyCash() {
        return buyCash;
    }

    public void setBuyCash(double buyCash) {
        this.buyCash = buyCash;
    }

    public double getBuyTransfer() {
        return buyTransfer;
    }

    public void setBuyTransfer(double buyTransfer) {
        this.buyTransfer = buyTransfer;
    }

    public double getSaleCash() {
        return saleCash;
    }

    public void setSaleCash(double saleCash) {
        this.saleCash = saleCash;
    }

    public double getSaleTransfer() {
        return saleTransfer;
    }

    public void setSaleTransfer(double saleTransfer) {
        this.saleTransfer = saleTransfer;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", bankName='" + bankName + '\'' +
                ", currencySymbol='" + currencySymbol + '\'' +
                ", currencyName='" + currencyName + '\'' +
                ", buyCash=" + buyCash +
                ", buyTransfer=" + buyTransfer +
                ", saleCash=" + saleCash +
                ", saleTransfer=" + saleTransfer +
                '}';
    }
}

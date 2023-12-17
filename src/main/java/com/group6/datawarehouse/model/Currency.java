package com.group6.datawarehouse.model;

public class Currency {
    private int id_dim;
    private String date;
    private String time;
    private String bank_code;
    private String currency_code;
    private String currency_name;
    private String buy_cash;
    private String buy_Transfer;
    private String sale_Cash;
    private String sale_Transfer;

    public Currency() {
    }

    public Currency(int id_dim, String date, String time, String bank_code, String currency_code, String currency_name, String buy_cash, String buy_Transfer, String sale_Cash, String sale_Transfer) {
        this.id_dim = id_dim;
        this.date = date;
        this.time = time;
        this.bank_code = bank_code;
        this.currency_code = currency_code;
        this.currency_name = currency_name;
        this.buy_cash = buy_cash;
        this.buy_Transfer = buy_Transfer;
        this.sale_Cash = sale_Cash;
        this.sale_Transfer = sale_Transfer;
    }

    public int getId_dim() {
        return id_dim;
    }

    public void setId_dim(int id_dim) {
        this.id_dim = id_dim;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getCurrency_name() {
        return currency_name;
    }

    public void setCurrency_name(String currency_name) {
        this.currency_name = currency_name;
    }

    public String getBuy_cash() {
        return buy_cash;
    }

    public void setBuy_cash(String buy_cash) {
        this.buy_cash = buy_cash;
    }

    public String getBuy_Transfer() {
        return buy_Transfer;
    }

    public void setBuy_Transfer(String buy_Transfer) {
        this.buy_Transfer = buy_Transfer;
    }

    public String getSale_Cash() {
        return sale_Cash;
    }

    public void setSale_Cash(String sale_Cash) {
        this.sale_Cash = sale_Cash;
    }

    public String getSale_Transfer() {
        return sale_Transfer;
    }

    public void setSale_Transfer(String sale_Transfer) {
        this.sale_Transfer = sale_Transfer;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id_dim=" + id_dim +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", bank_code='" + bank_code + '\'' +
                ", currency_code='" + currency_code + '\'' +
                ", currency_name='" + currency_name + '\'' +
                ", buy_cash='" + buy_cash + '\'' +
                ", buy_Transfer='" + buy_Transfer + '\'' +
                ", sale_Cash='" + sale_Cash + '\'' +
                ", sale_Transfer='" + sale_Transfer + '\'' +
                '}';
    }
}

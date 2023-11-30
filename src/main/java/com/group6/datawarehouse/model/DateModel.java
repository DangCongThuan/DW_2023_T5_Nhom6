package com.group6.datawarehouse.model;

import java.util.Date;

public class DateModel {
    private int id;
    private Date fullDate;
    private String dayOfWeek;
    private int dayOfMonth;
    private int dayOfYear;
    private int month;
    private int year;
    private int quater;

    public DateModel(int id, Date fullDate, String dayOfWeek, int dayOfMonth, int dayOfYear, int month, int year, int quater) {
        this.id = id;
        this.fullDate = fullDate;
        this.dayOfWeek = dayOfWeek;
        this.dayOfMonth = dayOfMonth;
        this.dayOfYear = dayOfYear;
        this.month = month;
        this.year = year;
        this.quater = quater;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFullDate() {
        return fullDate;
    }

    public void setFullDate(Date fullDate) {
        this.fullDate = fullDate;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getDayOfYear() {
        return dayOfYear;
    }

    public void setDayOfYear(int dayOfYear) {
        this.dayOfYear = dayOfYear;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getQuater() {
        return quater;
    }

    public void setQuater(int quater) {
        this.quater = quater;
    }
}

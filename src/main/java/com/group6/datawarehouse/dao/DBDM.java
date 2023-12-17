package com.group6.datawarehouse.dao;

import com.group6.datawarehouse.model.Currency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBDM {
    IConnect iConnect = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Connection connection = null;

    public List<Currency> loadCurrency(String selectedBank,String selectedDate) throws SQLException {
        List<Currency> currencyList = new ArrayList<>();

        String query = "SELECT *FROM dim WHERE bank_code=? AND date=?";
        iConnect = new ConnectDM();
        connection = iConnect.connectToMySQL();
        ps = connection.prepareStatement(query);
        ps.setString(1,selectedBank);
        ps.setString(2,selectedDate);
        rs = ps.executeQuery();

        while (rs.next()) {
            currencyList.add(new Currency(
                    rs.getInt("id_dim"),
                    rs.getString("date"),
                    rs.getString("time"),
                    rs.getString("bank_code"),
                    rs.getString("currency_code"),
                    rs.getString("currency_name"),
                    rs.getString("buy_cash"),
                    rs.getString("buy_Transfer"),
                    rs.getString("sale_Cash"),
                    rs.getString("sale_Transfer")
            ));
        }


        return currencyList;
    }

    public IConnect getiConnect() {
        return iConnect;
    }

    public void setiConnect(IConnect iConnect) {
        this.iConnect = iConnect;
    }

    public static void main(String[] args) throws SQLException {
        DBDM dbdm=new DBDM();
        List<Currency> currencyList = new ArrayList<>();
        currencyList = dbdm.loadCurrency("vcb","2023-1-21");
        for (Currency currency : currencyList) {
            System.out.println(currency);
        }
    }
}

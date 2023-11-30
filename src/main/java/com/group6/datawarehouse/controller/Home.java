package com.group6.datawarehouse.controller;

import com.group6.datawarehouse.dao.DBDM;
import com.group6.datawarehouse.model.CurrencyModel;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.group6.datawarehouse.model.Currency;

@WebServlet("/trang-chu")
public class Home extends HttpServlet {
    private DBDM dbdm = new DBDM();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        String bank = req.getParameter("bank");

        Currency currency = new Currency();
        List<Currency> arrayListTienTe = new ArrayList<>();
        try {
            arrayListTienTe = dbdm.loadCurrency();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        dbdm.getiConnect().closeConnection();

        req.setAttribute("arrayListTienTe", arrayListTienTe);
        RequestDispatcher rd = req.getRequestDispatcher("view/Trangchu.jsp");
        rd.forward(req, resp);
//        resp.sendRedirect("view/Trangchu.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}

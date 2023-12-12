package com.group6.datawarehouse.controller;

import com.group6.datawarehouse.dao.DBDM;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.group6.datawarehouse.model.Currency;

@WebServlet(name = "trang-chu", value = "/trang-chu")
public class Home extends HttpServlet {
    private DBDM dbdm = new DBDM();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletContext().getRealPath("/") + File.separator + "tool" + File.separator + "HomeEx.java";
        System.out.println("path: " + path);
        System.out.println("doget");
//        resp.sendRedirect("view/Trangchu.jsp");
        Currency currency = new Currency();
        List<Currency> arrayListTienTe = new ArrayList<>();
        try {
            arrayListTienTe = dbdm.loadCurrency("acb", "2023-12-10");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbdm.getiConnect().closeConnection();
        }
        req.setAttribute("arrayListTienTe", arrayListTienTe);
        RequestDispatcher rd = req.getRequestDispatcher("view/Trangchu.jsp");
        rd.forward(req, resp);
    }


//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println("doPossr");
//        resp.setContentType("text/html");
//        resp.setCharacterEncoding("UTF-8");
//
//        String selectedBank = req.getParameter("bank");
//        String selectedDate = req.getParameter("date");
//
//        System.out.println(selectedBank);
//        System.out.println(selectedDate);
//        if (selectedBank.isEmpty() || selectedDate.isEmpty()) {
//           doGet(req,resp);
//            System.out.println("emty");
//        } else {
//            Currency currency = new Currency();
//            List<Currency> arrayListTienTe = new ArrayList<>();
//            try {
//                arrayListTienTe = dbdm.loadCurrency(selectedBank,selectedDate);
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }finally {
//                dbdm.getiConnect().closeConnection();
//            }
//            req.setAttribute("arrayListTienTe", arrayListTienTe);
//            RequestDispatcher rd = req.getRequestDispatcher("view/Trangchu.jsp");
//            rd.forward(req, resp);
//        }
//
//    }
}

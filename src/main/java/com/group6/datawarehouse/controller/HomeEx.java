package com.group6.datawarehouse.controller;

import com.group6.datawarehouse.dao.DBDM;
import com.group6.datawarehouse.model.Currency;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "HomeEx", value = "/HomeEx")
public class HomeEx extends HttpServlet {
    private DBDM dbdm = new DBDM();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

        String selectedBank = req.getParameter("bank");
        String selectedDate = req.getParameter("date");
        System.out.println(selectedBank);
        System.out.println(selectedDate);

        if (selectedBank != null && selectedDate != null) {
            // Không chuyển hướng ngay lập tức, mà chờ đợi khi cả hai tham số đều có sẵn
            List<Currency> arrayListTienTe = new ArrayList<>();
            try {
                arrayListTienTe = dbdm.loadCurrency(selectedBank, selectedDate);
                for (Currency currency : arrayListTienTe) {
                    System.out.println(currency);
                }if (arrayListTienTe.isEmpty()){
                    PrintWriter out = resp.getWriter();
                        out.println("");
                }else {
                    PrintWriter out = resp.getWriter();
                    for (Currency c : arrayListTienTe) {
                        out.println("<div class=\"row\">" +
                                "  <div class=\"item1 border_1\">" + c.getCurrencyName() + "</div>\n" +
                                "                        <div class=\"item2 border_1\">" + c.getCurrencySymbol() + "</div>\n" +
                                "                        <div class=\"item3 border_1\">" + c.getBuyCash() + "</div>\n" +
                                "                        <div class=\"item4 border_1\">" + c.getBuyTransfer() + "</div>\n" +
                                "                        <div class=\"item5 border_1\">" + c.getSaleCash() + "</div>\n" +
                                "                        <div class=\"item5 border_1\">" + c.getSaleTransfer() + "</div>" +
                                " </div>")
                        ;
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                dbdm.getiConnect().closeConnection();
            }
        }
        if (selectedBank == null && selectedDate == null) {
            resp.sendRedirect("view/Trangchu.jsp");
        }
    }

}

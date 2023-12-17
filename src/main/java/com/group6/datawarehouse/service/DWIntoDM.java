package com.group6.datawarehouse.service;

import com.group6.datawarehouse.dao.ConnectControl;
import com.group6.datawarehouse.dao.ConnectDM;
import com.group6.datawarehouse.dao.ConnectDW;
import com.group6.datawarehouse.dao.IConnect;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DWIntoDM {
    private ConnectControl connectControl;
    private ConnectDW connectDW;
    private ConnectDM connectDM;
    private PreparedStatement preparedStatement;
    private String date;

    public void connectControl() {// 1 và 2
        connectControl = new ConnectControl();
        try {
            connectControl.connectToMySQL();
        } catch (Exception e) {
//            createLogFile();
        }


    }

    public void connectDW() {
        connectDW = new ConnectDW();
        try {
            connectDW.connectToMySQL();
        } catch (Exception ignored) {

        }

    }

    public void connectDM() {
        connectDM = new ConnectDM();
        connectDM.connectToMySQL();
    }

    /*
    check status của DW
     */
    public void checkStatusDW() {
        String sql = "SELECT *  FROM config WHERE id=? OR id=?";
        try (
                // Tạo đối tượng PreparedStatement để thực hiện lệnh SQL
                PreparedStatement preparedStatement = connectControl.getConnection().prepareStatement(sql);
                // Đặt giá trị cho các tham số trong câu lệnh UPDATE

        ) {
            preparedStatement.setInt(1, 5);
            preparedStatement.setInt(2, 6);
            // Thực hiện câu lệnh
            ResultSet resultSet = preparedStatement.executeQuery();
            List<String> liststt = new ArrayList<>();
            List<Integer> listsid = new ArrayList<>();
            while (resultSet.next()) {
                String stt = resultSet.getString("stt");
                System.out.println("stt: " + stt);
                int id = resultSet.getInt("id");
                System.out.println("id: " + id);
                liststt.add(stt);
                listsid.add(id);
            }
            xuLiStatus(liststt, listsid);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void xuLiStatus(List<String> liststt, List<Integer> listsid) {
        if (!(liststt.get(0).equals("Done")) && !(liststt.get(1).equals("Done"))) {
            System.out.println(liststt.get(0));
            System.out.println(liststt.get(1));
            System.out.println("action khác done");
            createLogFile(7, "Tiến trình của config: " + listsid.get(0) + "và" + listsid.get(1) + " ở DW chưa hoàn thành");
            connectControl.closeConnection();
            connectDW.closeConnection();
            connectDM.closeConnection();
            return;
        }

        if (liststt.get(0).equals("Done")) {
            loadDate(listsid.get(0), "Done");
            insert_log(7, "Prepared", "Tiến trình của config: " + listsid.get(0) + " ở DW đã hoàn thành,Chuẩn bị lấy dữ liệu");
        } else {
            createLogFile(7, "Tiến trình của config: " + listsid.get(0) + " ở DW chưa hoàn thành");
        }
        if (liststt.get(1).equals("Done")) {
            loadDate(listsid.get(1), "Done");
            insert_log(7, "Prepared", "Tiến trình của config: " + listsid.get(1) + " ở DW đã hoàn thành,Chuẩn bị lấy dữ liệu");
        } else {
            createLogFile(7, "Tiến trình của config: " + listsid.get(1) + " ở DW chưa hoàn thành");
        }
        if ((liststt.get(0).equals("Done")) || (liststt.get(1).equals("Done"))) {
            System.out.println("action done");
            update_config(7, "Prepared");
            loadDataFromDWtoDm();
        }
    }

    public void loadDataFromDWtoDm() {//10
        String storedProcedureCall = "{call InsertIntoAggregate(?)}";
        String storedProcedureCall2="{call InsertIntoDIM(?)}";
        try (CallableStatement callableStatement = connectDM.getConnection().prepareCall(storedProcedureCall)) {
            // Truyền tham số cho stored procedure
            callableStatement.setString(1,date);
            // Thực thi stored procedure
            callableStatement.execute();
            System.out.println("Stored procedure executed successfully.");
            update_config(7, "Done");
            insert_log(7, "Done", "Đã load xong dữ liệu từ dw sang bảng aggregate của dm");
        } catch (SQLException e) {
            insert_log(7, "Error", "Lỗi khi load dữ liệu từ dw sang bảng aggregate của dm");
            e.printStackTrace();
        }
        try (CallableStatement callableStatement2 = connectDM.getConnection().prepareCall(storedProcedureCall2)) {
            // Truyền tham số cho stored procedure
            callableStatement2.setString(1,date);
            // Thực thi stored procedure
            callableStatement2.execute();
            System.out.println("Stored procedure executed successfully.");
            createLogFile(7,"đã get dữ liệu cần thiết từ aggregate sang dim");
        } catch (SQLException e) {
            insert_log(7, "Error", "Lỗi khi load dữ liệu từ aggreate sang bảng dim của dm");
            e.printStackTrace();
        }

    }

    public String loadDate(int id, String str) {
        String sql = "SELECT time FROM log WHERE id_config = ? AND stt =? ORDER BY time DESC LIMIT 1";
        String time = "";
        String formattedDate = "";
        try (
                // Tạo đối tượng PreparedStatement để thực hiện lệnh SQL
                PreparedStatement preparedStatement = connectControl.getConnection().prepareStatement(sql);
                // Đặt giá trị cho các tham số trong câu lệnh UPDATE

        ) {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, str);
            // Thực hiện câu lệnh
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                time = resultSet.getString("time");
                System.out.println("time: " + time);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        //
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date = dateFormat.parse(time);

            // Định dạng mới chỉ chứa phần ngày
            SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            // Lấy ngày từ đối tượng Date và chuyển đổi thành chuỗi
            formattedDate = newDateFormat.format(date);
            System.out.println("Ngày: " + formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setDate(formattedDate);
        return formattedDate;
    }

    public void insert_log(int config_id, String stt, String description) {//5
        try {
            // Câu lệnh INSERT
            String insertQuery = "INSERT INTO log (id_config, stt, description, time) VALUES (?, ?, ?,CURRENT_TIMESTAMP)";

            // Tạo PreparedStatement để thực hiện câu lệnh INSERT
            PreparedStatement preparedStatement = connectControl.getConnection().prepareStatement(insertQuery);

            // Đặt giá trị cho các tham số trong câu lệnh INSERT
            preparedStatement.setInt(1, config_id);
            preparedStatement.setString(2, stt);
            preparedStatement.setString(3, description);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /*
    Hàm update config trong db control
     */
    public void update_config(int id, String stt) {//5
        String sql = "UPDATE config SET stt = ? WHERE id = ?";
        try (
                // Tạo đối tượng PreparedStatement để thực hiện lệnh SQL
                PreparedStatement preparedStatement = connectControl.getConnection().prepareStatement(sql)
        ) {
            // Đặt giá trị cho các tham số trong câu lệnh UPDATE
            preparedStatement.setString(1, stt);
            preparedStatement.setInt(2, id);

            // Thực hiện câu lệnh UPDATE
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void createLogFile(int id, String content) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String contentLog = dateFormat.format(new Date());
        LocalDate currentDate = LocalDate.now();
        // Định dạng ngày thành chuỗi "yyyy-MM-dd"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        String filePath = "src\\main\\resources\\log\\DM" + "log_" + currentDate + ".txt";
        try (FileWriter fileWriter = new FileWriter(filePath, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            // Nếu file không tồn tại, FileWriter sẽ tạo file mới
            // Nếu file tồn tại, FileWriter sẽ ghi tiếp vào nội dung hiện có
            // Ghi dữ liệu vào file
            bufferedWriter.write(contentLog + "\t \t \t" + content);

            // Xuống dòng để ngăn cách giữa các đoạn dữ liệu (tuỳ chọn)
            bufferedWriter.newLine();

            System.out.println("Dữ liệu đã được ghi vào file.");

        } catch (IOException e) {
            System.err.println("Lỗi khi ghi vào file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static void main(String[] args) {
        DWIntoDM dwIntoDM = new DWIntoDM();
        dwIntoDM.connectControl();
        dwIntoDM.connectDW();
        dwIntoDM.connectDM();
        dwIntoDM.checkStatusDW();
    }
}

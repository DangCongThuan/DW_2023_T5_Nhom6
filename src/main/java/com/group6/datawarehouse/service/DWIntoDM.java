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
    private boolean flag;

    public void connectControl() {
        connectControl = new ConnectControl();  // step 1 : load initialization from file database.properties get (severName,userID,password portNumber,dbName)
        connectControl.connectToMySQL();   // step 2 : 2.Connect DB control
    }

    public void connectDW() {
        // step 1 : load initialization from file database.properties get (severName,userID,password portNumber,dbName)
        // step 2 : 2.Connect DB control
        connectDW = new ConnectDW();
        connectDW.connectToMySQL();
    }

    public void connectDM() {
        // step 8 : Connect DB DM?
        // step 9 : insert_log?stt=Error ,update_configs?stt=Error
        connectDM = new ConnectDM();
        connectDM.connectToMySQL();

    }

    /*
    check status của DW
     */
    public void checkStatusDW() {
        // step 1 : load initialization from file database.properties get (severName,userID,password portNumber,dbName)
        // step 2 : Connect DB control
        connectControl();
        //step 4 : get status in table config:status: ="?"
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
            // step 5 : Check the status is done or not?
            xuLiStatus(liststt, listsid);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    // step 5 : Check the status is done or not?
    public void xuLiStatus(List<String> liststt, List<Integer> listsid) {
        // step 6: create or insert File log log_yyyy_mm_dd.txt
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
            // step 7 : 7.insert_log?stt=Prepared ,update_configs?stt=Prepared
            update_config(7, "Prepared");
            insert_log(7, "Prepared", "Tiến trình của config: " + listsid.get(0) + " ở DW đã hoàn thành,Chuẩn bị lấy dữ liệu");
        } else {
            // step 6: create or insert File log log_yyyy_mm_dd.txt
            createLogFile(7, "Tiến trình của config: " + listsid.get(0) + " ở DW chưa hoàn thành");
        }
        if (liststt.get(1).equals("Done")) {
            loadDate(listsid.get(1), "Done");
            // step 7 : 7.insert_log?stt=Prepared ,update_configs?stt=Prepared
            update_config(7, "Prepared");
            insert_log(7, "Prepared", "Tiến trình của config: " + listsid.get(1) + " ở DW đã hoàn thành,Chuẩn bị lấy dữ liệu");
        } else {
            // step 6: create or insert File log log_yyyy_mm_dd.txt
            createLogFile(7, "Tiến trình của config: " + listsid.get(1) + " ở DW chưa hoàn thành");
        }
        if ((liststt.get(0).equals("Done")) || (liststt.get(1).equals("Done"))) {
            System.out.println("action done");
            // step 8 : Connect DB DM?
            // step 9 : create  or insert File log log_yyyy_mm_dd.txt
            // step 10 : Check whether the day's data has been loaded or not?
            // step 11 : insert_log?stt=Done ,update_configs?stt=Done
            // step 12 : insert_log?stt=Crawling ,update_configs?stt=Crawling
            checkFlag(date);
        }
    }

    public void checkFlag(String date) {
        // step 8 : Connect DB DM?
        connectDM();

        // step 10 : Check whether the day's data has been loaded or not?
        String sql = "SELECT full_date FROM dm.aggregate ar WHERE ar.full_date=?";
        String fdate = "";
        try (
                // Tạo đối tượng PreparedStatement để thực hiện lệnh SQL
                PreparedStatement preparedStatement = connectDM.getConnection().prepareStatement(sql);
                // Đặt giá trị cho các tham số trong câu lệnh UPDATE

        ) {
            preparedStatement.setString(1, date);
            // Thực hiện câu lệnh
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                fdate = resultSet.getString("full_date");
                System.out.println(fdate);
                if (fdate.isEmpty() || fdate == null) {
                    setflag(false);
                } else {
                    setflag(true);
                }
                System.out.println("fdate: " + fdate);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        if (flag == true) {

            // step 11 : insert_log?stt=Done ,update_configs?stt=Done
            insert_log(7, "Done", "Process này đã hoàn thành trước đó");
        } else {
            // step 12 : insert_log?stt=Crawling ,update_configs?stt=Crawling
            System.out.println(" flag false");
            update_config(7, "Crawling");
            insert_log(7, "Crawling", "Tiến hành lấy dữ liệu về");
            // step 13 : loadDataFromDWtoDm?
            loadDataFromDWtoDm();
        }
    }
    // step 13 : loadDataFromDWtoDm?
    // step 14 : insert_log?stt=Error ,update_configs?stt=Error
    // step 15 : insert_log?stt=Done ,update_configs?stt=Done

    public void loadDataFromDWtoDm() {
        // step 13 : loadDataFromDWtoDm?
        String storedProcedureCall = "{call InsertIntoAggregate(?)}";
        String storedProcedureCall2 = "{call InsertIntoDIM(?)}";
        try (CallableStatement callableStatement = connectDM.getConnection().prepareCall(storedProcedureCall)) {
            // Truyền tham số cho stored procedure
            callableStatement.setString(1, date);
            // Thực thi stored procedure
            callableStatement.execute();
            System.out.println("Stored procedure executed successfully.");

            // step 15 : insert_log?stt=Done ,update_configs?stt=Done
            update_config(7, "Done");
            insert_log(7, "Done", "Đã load xong dữ liệu từ dw sang bảng aggregate của dm");
        } catch (SQLException e) {
            // step 14 : insert_log?stt=Error ,update_configs?stt=Error
            update_config(7, "Error");
            insert_log(7, "Error", "Lỗi khi load dữ liệu từ dw sang bảng aggregate của dm");
            e.printStackTrace();
        }
        try (CallableStatement callableStatement2 = connectDM.getConnection().prepareCall(storedProcedureCall2)) {
            // Truyền tham số cho stored procedure
            callableStatement2.setString(1, date);
            // Thực thi stored procedure
            callableStatement2.execute();
            System.out.println("Stored procedure executed successfully.");
            insert_log(7, "Done", "đã get dữ liệu cần thiết từ aggregate sang dim");
        } catch (SQLException e) {
            insert_log(7, "Error", "Lỗi khi load dữ liệu từ aggreate sang bảng dim của dm");
            e.printStackTrace();
        }
        connectControl.closeConnection();
        connectDM.closeConnection();
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
    // step 9,11,12,14,15
    public void insert_log(int config_id, String stt, String description) {
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
    // step 9,11,12,14,15
    public void update_config(int id, String stt) {
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
    // step 3,6
    public void createLogFile(int id, String content) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String contentLog = dateFormat.format(new Date());
        LocalDate currentDate = LocalDate.now();
        // Định dạng ngày thành chuỗi "yyyy-MM-dd"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        String filePath = "C:\\Users\\trand\\OneDrive\\Máy tính\\datawarehourse\\log\\DM\\" + currentDate + ".txt";
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
        connectControl.closeConnection();
        connectDW.closeConnection();
        connectDM.closeConnection();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean getflag() {
        return flag;
    }

    public void setflag(boolean checkflag) {
        this.flag = flag;
    }

    public static void main(String[] args) {
        DWIntoDM dwIntoDM = new DWIntoDM();
        dwIntoDM.checkStatusDW();// Step 1,2,3,4,5,6,7,8,9,10,11,12,13,14
    }
}

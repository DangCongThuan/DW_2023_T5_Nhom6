package com.group6.datawarehouse.service;

import com.group6.datawarehouse.dao.ConnectControl;
import com.group6.datawarehouse.dao.ConnectStaging;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Staging {
    static ConnectControl control = new ConnectControl();
    static ConnectStaging staging = new ConnectStaging();
    static Connection connectionControl = control.connectToMySQL();
    static Connection connectionStaging = staging.connectToMySQL();
    // Lấy ngày hiện tại
    static LocalDate currentDate = LocalDate.now();
    // Định dạng ngày yyyyMMdd
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    static String formattedDate = currentDate.format(formatter);
    // Lấy thời điểm hiện tại
    static LocalDateTime currentDateTime = LocalDateTime.now();
    // Chuyển đổi thành Timestamp
    static Timestamp currentTimestamp = Timestamp.valueOf(currentDateTime);

    static String excelVCBPath = "D:\\DW_currency_exchange_rate\\Currency_Exchange_Rate\\vcb_" + formattedDate + ".xlsx";
    static String excelACBPath = "D:\\DW_currency_exchange_rate\\Currency_Exchange_Rate\\acb_" + formattedDate + ".xlsx";

    public void loadVCBStaging(String excelFilePath) throws SQLException {

        /* Buoc 1: run if
        D:\DW_currency_exchange_rate\Currency_Exchange_Rate\vcb_yyyymmdd.xlsx
        exists and not empty */
        File fileVCB = new File(excelFilePath);
        if (fileVCB.exists()) {
            if (fileVCB.length()>0) {
                /* Buoc 2: Run database.properties lấy kết nối đến các database */

                /* Buoc 3: Kết nối đến database Database Control */
                if (Staging.connect_control()) {

                    /* Buoc 4: Kết nối đến database Database Staging */
                    if (Staging.connect_staging()) {

                        /* Buoc 5: Kiểm tra control.config đã có dữ liệu chưa with (stt='Done',id=2) */
                        int id = 2;
                        if (check_stt_config(id, "Done")==1){

                            /* Buoc 6: truncate staging.vcb*/
                            String table = "vcb";
                            Staging.truncate_data(table);

                            /* Buoc 7: update config SET status="Crawling" WHERE id = 4*/
                            id = 4;
                            update_config (id, "Crawling");

                            /* Buoc 8: insert into Log(id_config,status,description,time=CURRENT_TIMESTAMP)
                            VALUES (4,"Crawling","Tiến hành lấy dữ liệu từ file vcb_yyyymmdd.xlsx vào staging") */
                            insert_log(id, "Crawling", "Tiến hành lấy dữ liệu từ file vcb_yyyymmdd.xlsx vào staging");

                            /* Buoc 9: Load data từ vcb_yyyymmdd.xlsx vào staging.vcb */
                            // Đọc dữ liệu từ file Excel
                            Staging.load_exelVCB_into_staging(excelFilePath);

                            /* Buoc 10. Kiểm tra data từ file có load lên hết vào table:vcb:
                            countLinesFile(vcb_yyyymmdd.xlsx)==countLinesDB(vcb) */
                            int lineOFFile = countLinesFile(excelFilePath);
                            int lineOFDB = countLinesDB(table);
                            checkNumRow(lineOFFile,lineOFDB,id);

                            /* Buoc 12: Kiểm tra status và thông báo kết quả ra màn hình */
                            if(check_stt_config(id, "Done") == 1 && check_stt_log(id, "Done") == 1){
                                System.out.println("Hoàn tất load từ file vcb_yyyymmdd.xlsx vào table vcb");
                            } else {
                                System.out.println("Lỗi load từ file vcb_yyyymmdd.xlsx vào table vcb");
                            }
                        }
                    }
                }
            }
        } else {
            System.out.println("File does not exist.");
        }
    }

    public void loadACBStaging(String excelFilePath) throws SQLException {

        /* Buoc 1: run if
        D:\DW_currency_exchange_rate\Currency_Exchange_Rate\acb_yyyymmdd.xlsx
        exists and not empty */
        File fileACB = new File(excelFilePath);
        if (fileACB.exists()) {
            if (fileACB.length()>0) {
                /* Buoc 2: Run database.properties lấy kết nối đến các database */

                /* Buoc 3: Kết nối đến database Database Control */
                if (Staging.connect_control()) {

                    /* Buoc 4: Kết nối đến database Database Staging */
                    if (Staging.connect_staging()) {

                        /* Buoc 5: Kiểm tra control.config đã có dữ liệu chưa (status='Done',id=1) */
                        int id = 1;
                        if (check_stt_config(id, "Done")==1) {

                            /* Buoc 6: TRUNCATE TABLE acb */
                            String table = "acb";
                            Staging.truncate_data(table);

                            /* Buoc 7: update config SET status="Crawling" WHERE id = 3 */
                            id = 3;
                            update_config (id, "Crawling");

                            /* Buoc 8: insert into Log(config_id,status,description,time=CURRENT_TIMESTAMP)
                            VALUES (3,"Crawling","Tiến hành lấy dữ liệu từ file acb_yyyymmdd.xlsx vào staging") */
                            insert_log(id, "Crawling", "Tiến hành lấy dữ liệu từ file acb_yyyymmdd.xlsx vào table acb");

                            /* Buoc 9: Load data từ acb_yyyymmdd.xlsx vào staging.acb */
                            // Đọc dữ liệu từ file Excel
                            Staging.load_exelACB_into_staging(excelFilePath);

                            /* Buoc 10: Kiểm tra data từ file có load lên hết vào table:acb:
                            countLinesFile(acb_yyyymmdd.xlsx)==countLinesDB(acb) */
                            int lineOFFile = countLinesFile(excelFilePath);
                            int lineOFDB = countLinesDB(table);
                            checkNumRow(lineOFFile,lineOFDB,id);

                            /* Buoc 12: Kiểm tra status thông báo kết quả ra màn hình */
                            if(check_stt_config(id, "Done") == 1 && check_stt_log(id, "Done") == 1){
                                System.out.println("Hoàn tất load từ file acb_yyyymmdd.xlsx vào table acb");
                            } else {
                                System.out.println("Lỗi load từ file acb_yyyymmdd.xlsx vào table acb");
                            }
                        }
                    }
                }
            }
        } else {
            System.out.println("File does not exist.");
        }
    }

    // Buoc 3: Kết nối đến database Database Control
    public static boolean connect_control(){
        boolean result;
        try (Connection connection = control.connectToMySQL()) {
            result = true;
            System.out.println("Đã kết nối đến Database Control.");
        } catch (SQLException e) {
            result = false;
            System.err.println("Error connecting to the database Control: " + e.getMessage());
        }
        return result;
    }

    // Buoc 5: Kết nối đến database Database Staging
    public static boolean connect_staging(){
        boolean result;
        try (Connection connection = staging.connectToMySQL()) {
            result = true;
            System.out.println("Đã kết nối đến Database Staging.");
        } catch (SQLException e) {
            result = false;
            System.err.println("Error connecting to the database Staging: " + e.getMessage());
        }
        return result;
    }

    // Buoc 6: Truncate table
    public static void truncate_data (String table) throws SQLException {
        String truncateQuery = "TRUNCATE TABLE " + table;
        try (Statement statement = connectionStaging.createStatement()) {
            // Thực hiện câu lệnh truncate
            statement.executeUpdate(truncateQuery);

            System.out.println("Table truncated successfully.");
        }
    }

    /* Buoc 7: update config SET status="Crawling" WHERE id = 4*/
    public static void update_config(int id, String stt) {
        String sql = "UPDATE config SET stt = ? WHERE id = ?";

        try (
                // Tạo đối tượng PreparedStatement để thực hiện lệnh SQL
                PreparedStatement preparedStatement = connectionControl.prepareStatement(sql)
        )
        {
            // Đặt giá trị cho các tham số trong câu lệnh UPDATE
            preparedStatement.setString(1, stt);
            preparedStatement.setInt(2, id);

            // Thực hiện câu lệnh UPDATE
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /* Buoc 8: insert into Log(config_id,status,description,time=CURRENT_TIMESTAMP)
    VALUES (4,"Crawling","Tiến hành lấy dữ liệu từ file vcb_yyyymmdd.xlsx vào staging") */
    public static void insert_log(int config_id, String stt, String description){
        try {
            // Câu lệnh INSERT
            String insertQuery = "INSERT INTO log (id_config, stt, description, time) VALUES (?, ?, ?, ?)";

            // Tạo PreparedStatement để thực hiện câu lệnh INSERT
            PreparedStatement preparedStatement = connectionControl.prepareStatement(insertQuery);

            // Đặt giá trị cho các tham số trong câu lệnh INSERT
            preparedStatement.setInt(1, config_id);
            preparedStatement.setString(2, stt);
            preparedStatement.setString(3, description);
            preparedStatement.setTimestamp(4, currentTimestamp);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    // Buoc 9: Load data từ file vào staging
    public static void load_exelVCB_into_staging(String excelVCBPath){
        try (FileInputStream fis = new FileInputStream(excelVCBPath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            String insertQueryVCB = "INSERT INTO vcb (source,get_date,date_time,currency_symbol,currency_name,buy_cash,buy_transfer,sell) VALUES (?,?,?,?,?,?,?,?)";

                try (PreparedStatement preparedStatement = connectionStaging.prepareStatement(insertQueryVCB)) {
                    Sheet sheet = workbook.getSheetAt(0); // Lấy Sheet đầu tiên
                    for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                        Row row = sheet.getRow(rowIndex);
                        if (row != null) {
                            // Lấy giá trị từ các ô trong dòng
                            String source = row.getCell(0).getStringCellValue();
                            String get_date = row.getCell(1).getStringCellValue();
                            String date_time = row.getCell(2).getStringCellValue();
                            String currency_symbol = row.getCell(3).getStringCellValue();
                            String currency_name = row.getCell(4).getStringCellValue();
                            String buy_cash = row.getCell(5).getStringCellValue();
                            String buy_transfer = row.getCell(6).getStringCellValue();
                            String sell = row.getCell(7).getStringCellValue();

                            // Thiết lập giá trị vào PreparedStatement
                            preparedStatement.setString(1, source);
                            preparedStatement.setString(2, get_date);
                            preparedStatement.setString(3, date_time);
                            preparedStatement.setString(4, currency_symbol);
                            preparedStatement.setString(5, currency_name);
                            preparedStatement.setString(6, buy_cash);
                            preparedStatement.setString(7, buy_transfer);
                            preparedStatement.setString(8, sell);

                            // Thực hiện lệnh INSERT
                            preparedStatement.executeUpdate();
                        }
                    }
                }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void load_exelACB_into_staging(String excelACBPath){
        String insertQueryACB = "INSERT INTO acb (source,get_date,date_time,currency_symbol,currency_name,buy_cash,buy_transfer,sell_cash,sell_transfer) VALUES (?,?,?,?,?,?,?,?,?)";
        try (FileInputStream fis = new FileInputStream(excelACBPath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            try (PreparedStatement preparedStatement = connectionStaging.prepareStatement(insertQueryACB)) {
                Sheet sheet = workbook.getSheetAt(0); // Lấy Sheet đầu tiên

                for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row != null) {
                        // Lấy giá trị từ các ô trong dòng
                        String source = row.getCell(0).getStringCellValue();
                        String get_date = row.getCell(1).getStringCellValue();
                        String date_time = row.getCell(2).getStringCellValue();
                        String currency_symbol = row.getCell(3).getStringCellValue();
                        String currency_name = row.getCell(4).getStringCellValue();
                        String buy_cash = row.getCell(5).getStringCellValue();
                        String buy_transfer = row.getCell(6).getStringCellValue();
                        String sell_cash = row.getCell(7).getStringCellValue();
                        String sell_transfer = row.getCell(8).getStringCellValue();

                        // Thiết lập giá trị vào PreparedStatement
                        preparedStatement.setString(1, source);
                        preparedStatement.setString(2, get_date);
                        preparedStatement.setString(3, date_time);
                        preparedStatement.setString(4, currency_symbol);
                        preparedStatement.setString(5, currency_name);
                        preparedStatement.setString(6, buy_cash);
                        preparedStatement.setString(7, buy_transfer);
                        preparedStatement.setString(8, sell_cash);
                        preparedStatement.setString(9, sell_transfer);

                        // Thực hiện lệnh INSERT
                        preparedStatement.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /* Buoc 10: Kiểm tra data từ file có load lên hết vào staging */
    // Đếm số dòng trong file (trừ dòng đầu tiên)
    private static int countLinesFile(String filePath) {
        int count = 0;
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            // Lấy sheet đầu tiên
            Sheet sheet = workbook.getSheetAt(0);
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    count++;
                }
            }
            System.out.println("Số dòng trong file Excel: " + count);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return count;
    }

    // Đếm số dòng trong db:staging
    public static int countLinesDB(String table) {
        int count = 0;
        String sqlCount = "SELECT COUNT(*) AS count FROM " + table;
        try (PreparedStatement countStatement = connectionStaging.prepareStatement(sqlCount);
             ResultSet resultSet = countStatement.executeQuery()) {
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
            System.out.println("Số dòng trong vcb: " + count);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;
    }

    public static boolean checkNumRow (int lineOFFile, int lineOFDB, int id) {
        boolean result = false;
        // nếu số dòng dữ liệu bằng nhau
        if (lineOFFile == lineOFDB) {
            result = true;
            /* Buoc 10.1: update config SET status="Done" WHERE id = 3 */
            update_config (id, "Done");
            /* Buoc 11.1: insert into Log(config_id,status,description,create_at=CURRENT_TIMESTAMP)
            VALUES (3,"Done","Load dữ liệu vào table:acb thành công") */
            insert_log(id, "Done", "Load dữ liệu vào table:acb thành công");
        }
        // nếu số dòng dữ liệu không bằng nhau
        else {
            result = false;
            /* Buoc 10.2: update config SET status="Error" WHERE id = 3 */
            update_config (id, "Error");
            /* Buoc 11.2: insert into Log(config_id,status,description,create_at=CURRENT_TIMESTAMP)
            VALUES (3,"Error","Load dữ liệu vào table:acb thất bại") */
            insert_log(id, "Error", "Load dữ liệu vào table:acb thất bại");
        }
        return result;
    }

    /* Buoc 5, 12: Kiểm tra lại stt */
    public static int check_stt_config(int id, String stt) {
        int count = 0;
        String sqlCount = "SELECT COUNT(*) AS count FROM config WHERE id = ? AND stt = ?";

        try (PreparedStatement countStatement = connectionControl.prepareStatement(sqlCount)) {
            countStatement.setInt(1, id);
            countStatement.setString(2, stt);

            try (ResultSet resultSet = countStatement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt("count");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return count;
    }

    public static int check_stt_log(int id_config, String stt) {
        int count = 0;
        String sqlCount = "SELECT time FROM log WHERE id_config = ? AND stt = ? ORDER BY time DESC LIMIT 1";

        try (PreparedStatement countStatement = connectionControl.prepareStatement(sqlCount)) {
            countStatement.setInt(1, id_config);
            countStatement.setString(2, stt);

            try (ResultSet resultSet = countStatement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getRow();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return count;
    }

        public static void main(String[] args) throws SQLException {
        Staging staging = new Staging();
        //staging.loadVCBStaging(excelVCBPath);
        staging.loadACBStaging(excelACBPath);
    }
}
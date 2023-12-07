package com.group6.datawarehouse.service;

import com.group6.datawarehouse.dao.ConnectControl;
import com.group6.datawarehouse.dao.ConnectStaging;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Staging {
    static ConnectControl connectControl = new ConnectControl();
    static ConnectStaging connectStaging = new ConnectStaging();
    String excelFilePath = "D:\\Thư\\Data Warehouse\\vcb_20231119.xlsx";

    public void loadStaging() {

        /* Buoc 1: Task Schedule run if
            D:\DW_currency_exchange_rate\Currency_Exchange_Rate\vcb_yyyymmdd.xlsx
            not empty & yyyymmdd=nowDate() in 30m */

        /* Buoc 2: Run all into from file database.properties lấy kết nối đến các database */

        /* Buoc 3: Kết nối đến database Database Control */

        /* Buoc 4: Kiểm tra data file trong control.control_data_file
            đã có dữ liệu chưa with (status='Done',df_config_id=1) */

        /* Buoc 5: Kết nối đến database Database Staging */
        try (Connection connection = connectStaging.connectToMySQL()) {
            System.out.println("Đã kết nối đến Database Staging.");

            // Tạo truy vấn SQL
            /* Buoc 6. insert into Log with location='DB:staging',status='Crawling',
                    create_at=CURRENT_TIMESTAMP,description='source_file to staging' */


            /* Buoc 7: Load data từ file vào staging.tghd_staging */
            // Đọc dữ liệu từ file Excel
            try (FileInputStream fis = new FileInputStream(excelFilePath);
                 Workbook workbook = new XSSFWorkbook(fis)) {

                String insertQuery = "INSERT INTO tghd_staging (source,get_date,date_time,currency_symbol,currency_name,buy_cash,buy_transfer,sale_cash,sale_transfer) VALUES (?,?,?,?,?,?,?,?,?)";

                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
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
                            String sale_cash = row.getCell(7).getStringCellValue();
                            String sale_transfer = row.getCell(7).getStringCellValue();

                            // Thiết lập giá trị vào PreparedStatement
                            preparedStatement.setString(1, source);
                            preparedStatement.setString(2, get_date);
                            preparedStatement.setString(3, date_time);
                            preparedStatement.setString(4, currency_symbol);
                            preparedStatement.setString(5, currency_name);
                            preparedStatement.setString(6, buy_cash);
                            preparedStatement.setString(7, buy_transfer);
                            preparedStatement.setString(8, sale_cash);
                            preparedStatement.setString(9, sale_transfer);

                            // Thực hiện lệnh INSERT
                            preparedStatement.executeUpdate();
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }

            /* Buoc 8: Kiểm tra data từ file có load lên hết vào staging.tghd_staging
            không: row_df=select count() with date=nowdate()*/

                // 8.1: nếu số dòng dữ liệu bằng

                // 8.2: nếu số dòng dữ liệu không bằng

            /* Buoc 9: Kiểm tra lại status */


        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Staging staging = new Staging();
        staging.loadStaging();
    }
}
package fpt.swp.pcols.service.impl;

import fpt.swp.pcols.entity.Order;
import fpt.swp.pcols.service.ExcelService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public ByteArrayInputStream exportOrdersToExcel(List<Order> orders) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Orders");
            Row headerRow = sheet.createRow(0);

            String[] columns = {"Order ID", "Customer Name", "Email", "Order Date", "Status"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                CellStyle headerCellStyle = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                headerCellStyle.setFont(font);
                cell.setCellStyle(headerCellStyle);
            }

            int rowNum = 1;
            for (Order order : orders) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(order.getId());
                row.createCell(1).setCellValue(order.getFirstName() + " " + order.getLastName());
                row.createCell(2).setCellValue(order.getEmail());
                row.createCell(3).setCellValue(order.getOrderDate().toString());
                row.createCell(4).setCellValue(order.getStatus().toString());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel file", e);
        }
    }
}

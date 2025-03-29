package fpt.swp.pcols.service;

import fpt.swp.pcols.entity.Order;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface ExcelService {
    ByteArrayInputStream exportOrdersToExcel(List<Order> orders);
}

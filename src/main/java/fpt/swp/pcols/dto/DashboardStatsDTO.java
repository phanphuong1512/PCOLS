package fpt.swp.pcols.dto;

import java.math.BigDecimal;

public record DashboardStatsDTO(
        int totalOrders,
        int sales,
        BigDecimal earnings
){
}
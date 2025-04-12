package fpt.swp.pcols.controller;

import fpt.swp.pcols.dto.DashboardStatsDTO;
import fpt.swp.pcols.service.OrderService;
import fpt.swp.pcols.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final OrderService orderService;
    private final ProductService productService;

    @GetMapping("/admin/dashboard")
    public String showDashboard(Model model,
                                @RequestParam(value = "year", required = false) Integer year) {
        int selectedYear = (year != null) ? year : LocalDateTime.now().getYear();
        DashboardStatsDTO stats = orderService.getDashboardStats(selectedYear);
        List<Integer> years = orderService.getYearRangeForOrders();

        model.addAttribute("ordersThisYear", stats.totalOrders());
        model.addAttribute("sales", stats.sales());
        model.addAttribute("earnings", stats.earnings());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("years", years);
        model.addAttribute("selectedYear", selectedYear);
        return "admin/admin-home";
    }
}

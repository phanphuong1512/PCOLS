package fpt.swp.pcols.service;

import fpt.swp.pcols.dto.BillDTO;
import fpt.swp.pcols.dto.DashboardStatsDTO;
import fpt.swp.pcols.entity.Order;
import fpt.swp.pcols.entity.Product;
import fpt.swp.pcols.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    boolean hasUserPurchasedProduct(User user, Product product);

    Order save(Order cart);

    Optional<Order> getCurrentCartForUser(User user);

    Optional<Order> getLatestPlacedOrderForUser(User user);

    List<Order> getFilteredOrders(String sort, Order.OrderStatus status, String email);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    @Transactional
    void confirmOrder(Order order, BillDTO billDTO);

    void updateOrderStatus(Long orderId, Order.OrderStatus status);

    DashboardStatsDTO getDashboardStats(int year);

    List<Integer> getYearRangeForOrders();

    List<Order> getOrdersByYear(int selectedYear);
}
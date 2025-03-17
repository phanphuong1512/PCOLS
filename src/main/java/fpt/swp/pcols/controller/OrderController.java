package fpt.swp.pcols.controller;

import fpt.swp.pcols.entity.Order;
import fpt.swp.pcols.entity.OrderDetail;
import fpt.swp.pcols.entity.Product;
import fpt.swp.pcols.entity.User;
import fpt.swp.pcols.service.OrderService;
import fpt.swp.pcols.service.ProductService;
import fpt.swp.pcols.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService; // Service để lấy thông tin User
    private final ProductService productService;


    @GetMapping("/checkout")
    public String checkoutPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/auth/login";
        }

        User user = userService.findByUsername(principal.getName()).orElse(null);
        if (user == null) {
            return "redirect:/auth/login";
        }
        Optional<Order> optionalCart = orderService.getCurrentCartForUser(user);
        Order cart;
        if (optionalCart.isPresent()) {
            cart = optionalCart.get();
        } else {
            // Tạo cart mới rỗng nếu không có cart hiện tại
            cart = new Order();
            cart.setUser(user);
            cart.setStatus(Order.OrderStatus.PENDING);
            cart.setOrderDate(LocalDateTime.now());
            cart.setOrderDetails(new ArrayList<>());
            cart = orderService.save(cart);
        }

        model.addAttribute("order", cart);
        model.addAttribute("orderDetails", cart.getOrderDetails());
        return "checkout";
    }


    @PostMapping("/cart/add")
    @ResponseBody
    public ResponseEntity<?> addToCart(@RequestParam("productId") Long productId,
                                       @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                                       Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElseThrow(() -> new RuntimeException("User not found"));
        // Lấy giỏ hàng hiện tại của user
        Order cart = orderService.getCurrentCartForUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));
        if (cart == null) {
            cart = new Order();
            cart.setUser(user);
            cart.setStatus(Order.OrderStatus.PENDING);
            cart.setOrderDate(LocalDateTime.now());
            cart.setOrderDetails(new ArrayList<>());
            cart = orderService.save(cart);
        }
        // Kiểm tra xem sản phẩm đã có trong giỏ chưa
        OrderDetail existingDetail = orderService.findOrderDetailByOrderAndProduct(cart, productId);
        if (existingDetail != null) {
            existingDetail.setQuantity(existingDetail.getQuantity() + quantity);
        } else {
            Product product = productService.getProductById(productId);
            OrderDetail detail = new OrderDetail();
            detail.setOrder(cart);
            detail.setProduct(product);
            detail.setProductName(product.getName());
            detail.setPrice(BigDecimal.valueOf(product.getPrice()));
            detail.setQuantity(quantity);
            cart.getOrderDetails().add(detail);
        }
        orderService.save(cart);
        return ResponseEntity.ok("Product added to cart successfully");
    }

    @PostMapping("/cart/update")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateCartDetail(@RequestParam Long detailId,
                                                                @RequestParam int quantity) {
        // Tìm OrderDetail theo ID
        Optional<OrderDetail> optionalDetail = orderService.findDetailById(detailId);
        if (optionalDetail.isEmpty()) {
            // Nếu không tìm thấy, trả về 404 NOT FOUND kèm thông báo lỗi
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Order detail not found for id: " + detailId));
        }

        OrderDetail detail = optionalDetail.get();
        // Cập nhật số lượng
        detail.setQuantity(quantity);
        // Lưu thay đổi
        orderService.saveDetail(detail);

        // Tính lại tổng tiền cho dòng này
        BigDecimal lineTotal = detail.getPrice().multiply(BigDecimal.valueOf(quantity));

        // Tạo response
        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        response.put("lineTotal", lineTotal.toString());

        // Trả về JSON với mã 200 OK
        return ResponseEntity.ok(response);
    }

    // Xóa một OrderDetail khỏi giỏ hàng
    @PostMapping("/cart/remove")
    @ResponseBody
    public ResponseEntity<Map<String, String>> removeCartDetail(@RequestParam Long detailId) {
        // Tìm OrderDetail theo id
        OrderDetail detail = orderService.findDetailById(detailId)
                .orElseThrow(() -> new RuntimeException("Order detail not found for id: " + detailId));

        // Xóa OrderDetail; giả sử bạn có phương thức trong service để xóa detail
        orderService.deleteDetail(detail);

        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        response.put("message", "Item removed successfully");

        return ResponseEntity.ok(response);
    }
}
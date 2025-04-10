package fpt.swp.pcols.controller;

import fpt.swp.pcols.dto.BillDTO;
import fpt.swp.pcols.entity.Order;
import fpt.swp.pcols.entity.OrderDetail;
import fpt.swp.pcols.entity.Product;
import fpt.swp.pcols.entity.User;
import fpt.swp.pcols.exception.OutOfStockException;
import fpt.swp.pcols.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final ProductService productService;
    private final OrderDetailService orderDetailService;
    private final ExcelService excelService;

    @GetMapping("/checkout")
    public String checkoutPage(Model model, Principal principal) {
        User user;
        if (principal instanceof OAuth2AuthenticationToken oauthToken) {
            String email = (String) oauthToken.getPrincipal().getAttributes().get("email");
            user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        } else {
            user = userService.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found with username: " + principal.getName()));
        }

        // Lấy giỏ hàng của người dùng
        Order cart = orderService.getCurrentCartForUser(user)
                .orElseGet(() -> {
                    Order newCart = new Order();
                    newCart.setUser(user);
                    newCart.setStatus(Order.OrderStatus.PENDING);
                    newCart.setOrderDate(LocalDateTime.now());
                    newCart.setOrderDetails(new ArrayList<>());
                    return orderService.save(newCart);
                });

        model.addAttribute("order", cart);
        model.addAttribute("orderDetails", cart.getOrderDetails());

        if (model.containsAttribute("error")) {
            model.addAttribute("error", model.getAttribute("error"));
        }
        return "checkout";
    }

    @PostMapping("/cart/add")
    @ResponseBody
    public ResponseEntity<?> addToCart(@RequestParam("productId") Long productId,
                                       @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                                       Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElseThrow(() -> new RuntimeException("User not found"));
        // get current cart for user
        Order cart = orderService.getCurrentCartForUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));
        if (cart == null) {
            cart = new Order();
            cart.setUser(user);
            cart.setStatus(Order.OrderStatus.PENDING);
            cart.setOrderDate(LocalDateTime.now());
            cart.setOrderDetails(new ArrayList<>());
            cart = orderService.save(cart);
        }
        // check if product already exists in cart
        OrderDetail existingDetail = orderDetailService.findByOrderAndProduct_Id(cart, productId);
        if (existingDetail != null) {
            existingDetail.setQuantity(existingDetail.getQuantity() + quantity);
        } else {
            Product product = productService.findById(productId).orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
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
        // find order by id
        Optional<OrderDetail> optionalDetail = orderDetailService.findById(detailId);
        if (optionalDetail.isEmpty()) {
            // if order not found, return 404 NOT FOUND with error message
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Order detail not found for id: " + detailId));
        }

        OrderDetail detail = optionalDetail.get();
        detail.setQuantity(quantity);
        orderDetailService.save(detail);

        // Recalculate to get new line total
        BigDecimal lineTotal = detail.getPrice().multiply(BigDecimal.valueOf(quantity));

        // create response
        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        response.put("lineTotal", lineTotal.toString());

        // Return JSON with status 200 OK
        return ResponseEntity.ok(response);
    }

    // Xóa một OrderDetail khỏi giỏ hàng
    @PostMapping("/cart/remove")
    @ResponseBody
    public ResponseEntity<Map<String, String>> removeCartDetail(@RequestParam Long detailId) {
        // Tìm OrderDetail theo id
        OrderDetail detail = orderDetailService.findById(detailId)
                .orElseThrow(() -> new RuntimeException("Order detail not found for id: " + detailId));

        // Xóa OrderDetail; giả sử bạn có phương thức trong service để xóa detail
        orderDetailService.delete(detail);

        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        response.put("message", "Item removed successfully");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/checkout/success")
    public String checkoutSuccess(Principal principal, Model model) {
        try {
            // Lấy thông tin người dùng
            User user = userService.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Lấy đơn hàng vừa đặt của người dùng (trạng thái đơn hàng là PENDING hoặc PAID)
            Order order = orderService.getCurrentCartForUser(user)
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                if (orderDetail.getProduct() == null) {
                    // Xử lý khi product là null nếu cần (ví dụ: gán một giá trị mặc định hoặc báo lỗi)
                    orderDetail.setProduct(new Product());  // Ví dụ: Tạo đối tượng product rỗng để tránh lỗi
                }
            }

            // Thêm thông tin đơn hàng vào model để hiển thị
            model.addAttribute("order", order);
            model.addAttribute("orderDetails", order.getOrderDetails());

            // Nếu có lỗi (OutOfStockException), model sẽ chứa errorMap để hiển thị thông báo
            model.addAttribute("errorMap", model.containsAttribute("errorMap") ? model.getAttribute("errorMap") : null);

            return "order-success"; // Trang HTML hiển thị thông tin đơn hàng thành công
        } catch (Exception ex) {
            model.addAttribute("error", "An error occurred: " + ex.getMessage());
            return "checkout"; // Nếu có lỗi, quay lại trang checkout
        }
    }


    @PostMapping("/checkout/confirm")
    public String confirmCheckout(@ModelAttribute BillDTO billDTO, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Order order = orderService.getCurrentCartForUser(user)
                    .orElseThrow(() -> new RuntimeException("Cart not found"));

            orderService.confirmOrder(order, billDTO);

            return "redirect:/checkout/success";
        } catch (OutOfStockException ex) {
            redirectAttributes.addFlashAttribute("errorMap", ex.getErrorMap());
            return "redirect:/checkout";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "An error occurred: " + ex.getMessage());
            return "redirect:/checkout";
        }
    }

    @GetMapping("admin/orders")
    public String listOrders(Model model,
                             @RequestParam(value = "sort", required = false, defaultValue = "desc") String sort,
                             @RequestParam(value = "status", required = false) String status,
                             @RequestParam(value = "email", required = false) String email) {
        Order.OrderStatus orderStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid order status: " + status);
            }
        }
        List<Order> orders = orderService.getFilteredOrders(sort, orderStatus, email);
        model.addAttribute("orders", orders);
        model.addAttribute("sort", sort);
        model.addAttribute("status", status);
        model.addAttribute("email", email);
        return "admin/order/list";
    }

    @GetMapping("admin/order/detail/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        if (order == null) {
            return "redirect:/orders?error=Order not found";
        }
        model.addAttribute("order", order);
        return "admin/order/details";
    }

    @GetMapping("/admin/orders/export/excel")
    public ResponseEntity<InputStreamResource> exportToExcel() {
        List<Order> orders = orderService.findAll();
        ByteArrayInputStream inStream = excelService.exportOrdersToExcel(orders);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=orders.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(inStream));
    }
}
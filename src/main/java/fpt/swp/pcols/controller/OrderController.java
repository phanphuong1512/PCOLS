package fpt.swp.pcols.controller;

import fpt.swp.pcols.dto.BillDTO;
import fpt.swp.pcols.entity.Order;
import fpt.swp.pcols.entity.OrderDetail;
import fpt.swp.pcols.entity.Product;
import fpt.swp.pcols.entity.User;
import fpt.swp.pcols.service.ExcelService;
import fpt.swp.pcols.service.OrderService;
import fpt.swp.pcols.service.ProductService;
import fpt.swp.pcols.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.SpringVersion;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    private final ExcelService excelService;

    @GetMapping("/checkout")
    public String checkoutPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/auth/login";
        }
        User user;
        if (principal instanceof OAuth2AuthenticationToken oauthToken) {
            String email = (String) oauthToken.getPrincipal().getAttributes().get("email");
            user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        } else {
            user = userService.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found with username: " + principal.getName()));
        }

        Order cart = orderService.getCurrentCartForUser(user)
                .orElseGet(() -> {
                    Order newCart = new Order();
                    newCart.setUser(user);
                    newCart.setStatus(Order.OrderStatus.PENDING);
                    newCart.setOrderDate(LocalDateTime.now());
                    newCart.setOrderDetails(new ArrayList<>());
                    return orderService.save(newCart);
                });

        System.out.println("Cart: " + cart);
        System.out.println("Order Details: " + cart.getOrderDetails());
        cart.getOrderDetails().forEach(detail -> {
            System.out.println("Product: " + detail.getProduct());
            System.out.println("Images: " + detail.getProduct().getImages());
        });

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
        // find order by id
        Optional<OrderDetail> optionalDetail = orderService.findDetailById(detailId);
        if (optionalDetail.isEmpty()) {
            // if order not found, return 404 NOT FOUND with error message
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Order detail not found for id: " + detailId));
        }

        OrderDetail detail = optionalDetail.get();
        detail.setQuantity(quantity);
        orderService.saveDetail(detail);

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
        OrderDetail detail = orderService.findDetailById(detailId)
                .orElseThrow(() -> new RuntimeException("Order detail not found for id: " + detailId));

        // Xóa OrderDetail; giả sử bạn có phương thức trong service để xóa detail
        orderService.deleteDetail(detail);

        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        response.put("message", "Item removed successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/checkout/confirm")
    public String confirmCheckout(@ModelAttribute BillDTO billDTO, Principal principal) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderService.getCurrentCartForUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        order.setFirstName(billDTO.firstName());
        order.setLastName(billDTO.lastName());
        order.setEmail(billDTO.email());
        order.setAddress(billDTO.address());
        order.setCity(billDTO.city());
        order.setCountry(billDTO.country());
        order.setZipCode(billDTO.zipCode());
        order.setPhone(billDTO.phone());
        order.setShippingMethod(billDTO.shipping());
        order.setPaymentMethod(billDTO.payment());
        orderService.save(order);

        return "redirect:/home";
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
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return "redirect:/orders?error=Order not found";
        }
        model.addAttribute("order", order);
        return "admin/order/details";
    }

    @GetMapping("/admin/orders/export/excel")
    public ResponseEntity<InputStreamResource> exportToExcel() {
        List<Order> orders = orderService.getAllOrders();
        ByteArrayInputStream inStream = excelService.exportOrdersToExcel(orders);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=orders.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(inStream));
    }
}
package fpt.swp.pcols.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"orderDetails", "user"})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết đến người dùng đặt hàng (không lưu các thông tin billing từ đây)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Ngày đặt hàng
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate = LocalDateTime.now();

    // Trạng thái đơn hàng
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private OrderStatus status = OrderStatus.CART;

    // Danh sách chi tiết đơn hàng
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    // Thông tin billing/shipping (snapshot tại thời điểm đặt hàng)
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    // Email của khách hàng (snapshot, tránh phụ thuộc vào thông tin User)
    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "phone")
    private String phone;

    @Column(name = "shipping_method")
    private String shippingMethod;

    @Column(name = "payment_method")
    private String paymentMethod;

    public enum OrderStatus {
        CART,       // Giỏ hàng đang được tích lũy, có thể thay đổi
        PENDING,    // Đơn hàng đã được xác nhận (place order) và đang chờ xử lý
        PACKED,
        SHIPPED,
        CANCELLED,
        PAID
    }

    @Transient
    public BigDecimal getSubtotal() {
        if (orderDetails == null || orderDetails.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return orderDetails.stream()
                .map(OrderDetail::getTotalPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transient
    public BigDecimal getShippingFee() {
        if ("FREE".equalsIgnoreCase(shippingMethod)) {
            return BigDecimal.ZERO;
        } else if ("STANDARD".equalsIgnoreCase(shippingMethod)) {
            return new BigDecimal("4.00");
        }
        // Giá trị mặc định nếu shippingMethod không hợp lệ
        return BigDecimal.ZERO;
    }

    @Transient
    public BigDecimal getGrandTotal() {
        BigDecimal subtotal = getSubtotal() != null ? getSubtotal() : BigDecimal.ZERO;
        BigDecimal shippingFee = getShippingFee() != null ? getShippingFee() : BigDecimal.ZERO;
        return subtotal.add(shippingFee);
    }
}
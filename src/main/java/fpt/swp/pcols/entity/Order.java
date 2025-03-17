package fpt.swp.pcols.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết đến người dùng đặt hàng
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Sử dụng LocalDateTime để lưu ngày đặt hàng
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate = LocalDateTime.now();

    // Sử dụng enum để lưu trạng thái đơn hàng
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private OrderStatus status = OrderStatus.PENDING;

    // Danh sách các chi tiết đơn hàng (OrderDetail)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails;

    // Enum định nghĩa các trạng thái đơn hàng
    public enum OrderStatus {
        PENDING,
        PAID,
        SHIPPED,
        CANCELLED
    }
}
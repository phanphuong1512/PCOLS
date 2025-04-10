package fpt.swp.pcols.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "order_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"order", "product"})
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết đến đơn hàng chứa chi tiết này
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Liên kết đến sản phẩm được đặt (bản gốc)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Số lượng sản phẩm trong đơn hàng
    @Column(name = "quantity", nullable = false)
    private int quantity;

    // Giá snapshot tại thời điểm đặt hàng (sử dụng BigDecimal để đảm bảo tính chính xác)
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    // Snapshot tên sản phẩm, nhằm lưu giữ thông tin ngay lúc đặt hàng
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Transient
    public BigDecimal getTotalPrice() {
        if (price == null || quantity == 0) {
            return BigDecimal.ZERO;
        }
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
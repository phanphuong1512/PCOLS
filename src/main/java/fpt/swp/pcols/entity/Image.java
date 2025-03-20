package fpt.swp.pcols.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url")
    private String imageUrl; // Stores the relative path of the image

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}

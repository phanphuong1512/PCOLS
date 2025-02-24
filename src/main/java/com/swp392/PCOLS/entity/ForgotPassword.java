package com.swp392.PCOLS.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "forgot_password")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ForgotPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "otp", nullable = false)
    private Integer otp;

    @Column(name = "expiration_time")
    private Date expirationTime;

    @OneToOne
    private User user;
}
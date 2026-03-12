package com.felixgnwn.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponResponse {

    private Long id;
    private String code;
    private String description;
    private BigDecimal discountPercentage;
    private boolean active;
    private LocalDateTime expiryDate;
}

package com.felixgnwn.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponRequest {

    private String code;
    private String description;
    private BigDecimal discountPercentage;
    private LocalDateTime expiryDate;
}

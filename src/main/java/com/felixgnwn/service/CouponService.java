package com.felixgnwn.service;

import com.felixgnwn.dto.CouponRequest;
import com.felixgnwn.dto.CouponResponse;
import com.felixgnwn.entity.Coupon;
import com.felixgnwn.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponResponse createCoupon(CouponRequest request) {
        Coupon coupon = Coupon.builder()
                .code(request.getCode())
                .description(request.getDescription())
                .discountPercentage(request.getDiscountPercentage())
                .expiryDate(request.getExpiryDate())
                .active(true)
                .build();

        return toResponse(couponRepository.save(coupon));
    }

    public List<CouponResponse> getAllCoupons() {
        return couponRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CouponResponse deactivateCoupon(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found with id: " + id));
        coupon.setActive(false);
        return toResponse(couponRepository.save(coupon));
    }

    public Coupon validateAndGetActiveCoupon(String code) {
        Coupon coupon = couponRepository.findByCodeIgnoreCaseAndActiveTrue(code)
                .orElseThrow(() -> new RuntimeException("Invalid or inactive coupon code"));

        if (coupon.getExpiryDate() != null && coupon.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Coupon has expired");
        }

        return coupon;
    }

    private CouponResponse toResponse(Coupon coupon) {
        return CouponResponse.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .description(coupon.getDescription())
                .discountPercentage(coupon.getDiscountPercentage())
                .active(coupon.isActive())
                .expiryDate(coupon.getExpiryDate())
                .build();
    }
}

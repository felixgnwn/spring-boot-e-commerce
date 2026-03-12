package com.felixgnwn.controller;

import com.felixgnwn.dto.CouponRequest;
import com.felixgnwn.dto.CouponResponse;
import com.felixgnwn.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(@RequestBody CouponRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(couponService.createCoupon(request));
    }

    @GetMapping
    public ResponseEntity<List<CouponResponse>> getAllCoupons() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CouponResponse> deactivateCoupon(@PathVariable Long id) {
        return ResponseEntity.ok(couponService.deactivateCoupon(id));
    }
}

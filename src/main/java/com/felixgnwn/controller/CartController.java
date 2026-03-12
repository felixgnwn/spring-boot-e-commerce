package com.felixgnwn.controller;

import com.felixgnwn.dto.CartItemRequest;
import com.felixgnwn.dto.CartResponse;
import com.felixgnwn.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<CartResponse> addItem(@PathVariable Long userId,
                                                @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addItem(userId, request));
    }

    @PatchMapping("/{userId}/update")
    public ResponseEntity<CartResponse> updateItemQuantity(@PathVariable Long userId,
                                                           @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.updateItemQuantity(userId, request));
    }

    @DeleteMapping("/{userId}/remove/{productId}")
    public ResponseEntity<CartResponse> removeItem(@PathVariable Long userId,
                                                   @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeItem(userId, productId));
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
package com.bookshop.dto;

import java.time.Instant;
import java.util.List;

public class CartDTO {

    private Long userId;
    private final List<CartItemDTO> items;

    public CartDTO(Long userId, List<CartItemDTO> items) {
        this.userId = userId;
        this.items = items;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }
}

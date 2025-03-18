package com.bookshop.dto;

public class CartItemDTO {

    private Long userId;
    private Long bookId;
    private Integer quantityDays;

    public CartItemDTO() {
    }

    public CartItemDTO(Long userId, Long bookId, Integer quantityDays) {
        this.userId = userId;
        this.bookId = bookId;
        this.quantityDays = quantityDays;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Integer getQuantityDays() {
        return quantityDays;
    }

    public void setQuantityDays(Integer quantityDays) {
        this.quantityDays = quantityDays;
    }
}

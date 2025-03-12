package com.bookshop.entities;

import com.bookshop.entities.pk.CartItemPK;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class CartItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private CartItemPK id = new CartItemPK();

    private Integer quantityDays;

    public CartItem() {
    }

    public CartItem(Cart cart, Book book, Integer quantityDays) {
        id.setCart(cart);
        id.setBook(book);
        this.quantityDays = quantityDays;
    }

    public Cart getCart() {
        return id.getCart();
    }

    public void setCart(Cart cart) {
        id.setCart(cart);
    }

    public Book getBook() {
        return id.getBook();
    }

    public void setBook(Book book) {
        id.setBook(book);
    }

    public Integer getQuantityDays() {
        return quantityDays;
    }

    public void setQuantityDays(Integer quantityDays) {
        this.quantityDays = quantityDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(id, cartItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

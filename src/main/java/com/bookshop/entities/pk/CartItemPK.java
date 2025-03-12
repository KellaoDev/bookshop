package com.bookshop.entities.pk;

import com.bookshop.entities.Book;
import com.bookshop.entities.Cart;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CartItemPK implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItemPK that = (CartItemPK) o;
        return Objects.equals(cart, that.cart) && Objects.equals(book, that.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cart, book);
    }
}

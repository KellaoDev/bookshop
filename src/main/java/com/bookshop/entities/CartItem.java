package com.bookshop.entities;

import com.bookshop.entities.pk.CartItemPK;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class CartItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private final CartItemPK id = new CartItemPK();

    private Integer quantityDays;

    @ManyToOne
    @MapsId("cart")
    @JoinColumn(name = "cart_id", insertable = false, updatable = false)
    private Cart cart; // Relacionamento com o Cart

    @ManyToOne
    @MapsId("book")
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;

    public CartItem() {
    }

    public CartItem(Cart cart, Book book, Integer quantityDays, Loan loan) {
        id.setCart(cart);
        id.setBook(book);
        this.quantityDays = quantityDays;
        this.loan = loan;
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

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
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

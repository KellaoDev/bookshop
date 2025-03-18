package com.bookshop.repositories;

import com.bookshop.entities.Cart;
import com.bookshop.entities.CartItem;
import com.bookshop.entities.pk.CartItemPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    long countByCart(Cart cart);
    Optional<Object> findById(CartItemPK cartItemPK);
}

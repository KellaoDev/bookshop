package com.bookshop.services;

import com.bookshop.dto.CartDTO;
import com.bookshop.dto.CartItemDTO;
import com.bookshop.entities.*;
import com.bookshop.entities.pk.CartItemPK;
import com.bookshop.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public CartDTO getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        List<CartItemDTO> items = cart.getItems().stream()
                .map(item -> new CartItemDTO(cart.getUser().getId(), item.getBook().getId(), item.getQuantityDays()))
                .toList();

        return new CartDTO(cart.getUser().getId(), items);
    }

    public Cart addItemToCart(Long userId, CartItemDTO cartItemDTO) {
        var user = userEntityRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        Book book = bookRepository.findById(cartItemDTO.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        CartItemPK cartItemPK = new CartItemPK();
        cartItemPK.setCart(cart);
        cartItemPK.setBook(book);

        CartItem cartItem = (CartItem) cartItemRepository.findById(cartItemPK).orElse(null);

        if (cartItem != null) {
            cartItem.setQuantityDays(cartItem.getQuantityDays() + cartItemDTO.getQuantityDays());
        } else {
            cartItem = new CartItem(cart, book, cartItemDTO.getQuantityDays(), null);
        }

        cartItemRepository.save(cartItem);

        return cartRepository.save(cart);
    }

    public Cart deleteItemFromCart(Long userId, Long bookId) {
        var user = userEntityRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        CartItemPK cartItemPK = new CartItemPK();
        cartItemPK.setCart(cart);
        cartItemPK.setBook(book);


        CartItem cartItem = (CartItem) cartItemRepository.findById(cartItemPK)
                .orElseThrow(() -> new RuntimeException("Item não encontrado no carrinho"));

        cartItemRepository.delete(cartItem);

        if (cartItemRepository.countByCart(cart) == 0) {
            cartRepository.delete(cart);
            return null;
        }

        return cart;
    }





}

package com.bookshop.services;

import com.bookshop.dto.CartDTO;
import com.bookshop.dto.CartItemDTO;
import com.bookshop.entities.Book;
import com.bookshop.entities.Cart;
import com.bookshop.entities.CartItem;
import com.bookshop.entities.pk.CartItemPK;
import com.bookshop.repositories.BookRepository;
import com.bookshop.repositories.CartItemRepository;
import com.bookshop.repositories.CartRepository;
import com.bookshop.repositories.UserEntityRepository;
import com.bookshop.services.exceptions.DatabaseException;
import com.bookshop.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new ResourceNotFoundException("Carrinho não encontrado para: " + userId));

        List<CartItemDTO> items = cart.getItems().stream()
                .map(item -> new CartItemDTO(cart.getUser().getId(), item.getBook().getId(), item.getQuantityDays()))
                .toList();

        return new CartDTO(cart.getUser().getId(), items);
    }

    @Transactional
    public Cart addItemToCart(Long userId, CartItemDTO cartItemDTO) {
        var user = userEntityRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        Book book = bookRepository.findById(cartItemDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado para: " + cartItemDTO.getBookId()));

        CartItemPK cartItemPK = new CartItemPK();
        cartItemPK.setCart(cart);
        cartItemPK.setBook(book);

        CartItem cartItem = (CartItem) cartItemRepository.findById(cartItemPK).orElse(null);

        if (cartItem != null) {
            cartItem.setQuantityDays(cartItem.getQuantityDays() + cartItemDTO.getQuantityDays());
        } else {
            cartItem = new CartItem(cart, book, cartItemDTO.getQuantityDays(), null);
        }

        try {
            cartItemRepository.save(cartItem);
            return cartRepository.save(cart);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Carrinho ou item não encontrado");
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Erro ao salvar o carrinho ou item no banco de dados");
        }
    }

    @Transactional
    public Cart deleteItemFromCart(Long userId, Long bookId) {
        var user = userEntityRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrinho não encontrado."));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado."));

        CartItemPK cartItemPK = new CartItemPK();
        cartItemPK.setCart(cart);
        cartItemPK.setBook(book);


        CartItem cartItem = (CartItem) cartItemRepository.findById(cartItemPK)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado no carrinho."));

        try {
            cartItemRepository.delete(cartItem);

            if (cartItemRepository.countByCart(cart) == 0) {
                cartRepository.delete(cart);
                return null;
            }

            return cart;
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Carrinho ou item não encontrado");
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Erro ao salvar o carrinho ou item no banco de dados");
        }

    }
}

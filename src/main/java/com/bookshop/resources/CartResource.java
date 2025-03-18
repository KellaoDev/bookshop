package com.bookshop.resources;

import com.bookshop.dto.CartDTO;
import com.bookshop.dto.CartItemDTO;
import com.bookshop.entities.Cart;
import com.bookshop.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/carts")
public class CartResource {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCartByUserId(@PathVariable Long userId) {
        CartDTO cartDTO = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cartDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addItemToCart(@RequestBody CartItemDTO cartItemDTO) {
        Cart cart = cartService.addItemToCart(cartItemDTO.getUserId(), cartItemDTO);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{userId}/items/{bookId}")
    public ResponseEntity<?> deleteItemFromCart(@PathVariable Long userId, @PathVariable Long bookId) {
        Cart updatedCart = cartService.deleteItemFromCart(userId, bookId);
        if (updatedCart == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(updatedCart);
    }


}

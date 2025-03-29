package xa.sh.ecom.ecom.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import xa.sh.ecom.ecom.cart.models.Cart;
import xa.sh.ecom.ecom.cart.service.impl.CartItemRequestDto;
import xa.sh.ecom.ecom.cart.service.impl.CartServiceImpl; // TODO: Use CartService interface
import xa.sh.ecom.ecom.exception.ResourceNotFoundException;
import xa.sh.ecom.ecom.security.SecurityUtils; // Your utility for getting user ID

@RestController
@RequestMapping("/api/cart") // Base path for cart operations
@Validated // Enables validation of path/request params if needed
@PreAuthorize("isAuthenticated()") // Ensure user is logged in for all cart ops
public class CartController {

    private final CartServiceImpl cartService; // TODO: Inject CartService interface

    // Constructor Injection
    public CartController(CartServiceImpl cartService) {
        this.cartService = cartService;
    }

    // Get the current user's cart
    @GetMapping
    public ResponseEntity<Cart> getCurrentUserCart() throws ResourceNotFoundException {
        Long userId = SecurityUtils.getAuthenticatedUserId(); // Get logged-in user's ID
        Cart cart = cartService.getOrCreateCart(userId);
        return ResponseEntity.ok(cart);
    }

    // Add an item to the cart
    @PostMapping("/items")
    public ResponseEntity<Cart> addItemToCart(@Valid @RequestBody CartItemRequestDto itemDto) throws ResourceNotFoundException {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        Cart updatedCart = cartService.addItemToCart(userId, itemDto);
        return ResponseEntity.ok(updatedCart);
    }

    // Update quantity of a specific item in the cart
    @PatchMapping("/items/{itemId}")
    public ResponseEntity<Cart> updateCartItemQuantity(
            @PathVariable Long itemId,
            @RequestParam @Min(value = 0, message = "Quantity cannot be negative") int quantity) // Quantity 0 means remove
            throws ResourceNotFoundException {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        Cart updatedCart = cartService.updateCartItemQuantity(userId, itemId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    // Remove a specific item (alternative using DELETE)
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Cart> removeItemFromCart(@PathVariable Long itemId) throws ResourceNotFoundException {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        // Use update method with quantity 0 to remove
        Cart updatedCart = cartService.updateCartItemQuantity(userId, itemId, 0);
        return ResponseEntity.ok(updatedCart); // Or return ResponseEntity.noContent()
    }

    // Clear the entire cart
    @DeleteMapping
    public ResponseEntity<Void> clearCart() throws ResourceNotFoundException {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build(); // Standard response for successful delete/clear
    }
}

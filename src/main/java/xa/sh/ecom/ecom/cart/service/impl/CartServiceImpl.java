package xa.sh.ecom.ecom.cart.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import xa.sh.ecom.ecom.cart.models.Cart;
import xa.sh.ecom.ecom.cart.models.CartItem;
import xa.sh.ecom.ecom.cart.repo.CartRepo;
import xa.sh.ecom.ecom.exception.ResourceNotFoundException;
import xa.sh.ecom.ecom.models.User;
import xa.sh.ecom.ecom.product.models.Product;
import xa.sh.ecom.ecom.product.repo.ProductRepo;
import xa.sh.ecom.ecom.repository.UserRepository;

@Service
@Transactional
public class CartServiceImpl {

    @Autowired
    private CartRepo cartRepository;

    @Autowired
    private ProductRepo productRepository;

    @Autowired
    private UserRepository userRepository;

    public Cart getOrCreateCart(Long userId) throws ResourceNotFoundException {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        if (cart.isEmpty()) {
            User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Client not found"));;
                Cart newCart = new Cart();
                newCart.setUser(user);
                return cartRepository.save(newCart);
        }

        return cart.get();
    }

    public Cart addItemToCart(Long userId, CartItemRequestDto itemDto) throws ResourceNotFoundException {
        Cart cart = getOrCreateCart(userId);
        Product product = productRepository.findById(itemDto.getProduct().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Optional<CartItem> existingItem = cart.getItems().stream()
            .filter(item -> item.getProduct().getId().equals(product.getId()))
            .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + itemDto.getQuantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(itemDto.getQuantity());
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }

        return cartRepository.save(cart);
    }

    public Cart updateCartItemQuantity(Long userId, Long itemId, int quantity) throws ResourceNotFoundException {
        Cart cart = getOrCreateCart(userId);
        CartItem item = cart.getItems().stream()
            .filter(i -> i.getId().equals(itemId))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (quantity <= 0) {
            cart.getItems().remove(item);
        } else {
            item.setQuantity(quantity);
        }

        return cartRepository.save(cart);
    }

    public void clearCart(Long userId) throws ResourceNotFoundException {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
package xa.sh.ecom.ecom.cart.service.impl;

import xa.sh.ecom.ecom.cart.models.Cart;
import xa.sh.ecom.ecom.product.models.Product;

public class CartItemRequestDto {
    private Long id;

    private Cart cart;

    private Product product;

    private Integer quantity;

    public CartItemRequestDto(Cart cart, Long id, Product product, Integer quantity) {
        this.cart = cart;
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public CartItemRequestDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

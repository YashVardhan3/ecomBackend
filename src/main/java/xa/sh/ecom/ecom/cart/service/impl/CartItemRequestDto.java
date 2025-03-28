package xa.sh.ecom.ecom.cart.service.impl;

import lombok.Data;
import xa.sh.ecom.ecom.cart.models.Cart;
import xa.sh.ecom.ecom.product.models.Product;

@Data
public class CartItemRequestDto {
    private Long id;

    private Cart cart;

    private Product product;

    private Integer quantity;
}

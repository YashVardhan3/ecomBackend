package xa.sh.ecom.ecom.exception;

public class CartItemNotFoundException extends ApiException {
    public CartItemNotFoundException(Long itemId) {
        super("Cart item not found with id: " + itemId);
    }
}
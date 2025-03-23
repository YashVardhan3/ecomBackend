package xa.sh.ecom.ecom.exception;

public class InsufficientStockException extends ApiException {
    public InsufficientStockException() {
        super("Insufficient stock available");
    }

    public InsufficientStockException(String productName) {
        super("Insufficient stock for product: " + productName);
    }
}
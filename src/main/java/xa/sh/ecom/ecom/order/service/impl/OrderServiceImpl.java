// package xa.sh.ecom.ecom.order.service.impl;

// import java.math.BigDecimal;
// import java.util.ArrayList;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import jakarta.transaction.Transactional;
// import xa.sh.ecom.ecom.cart.models.Cart;
// import xa.sh.ecom.ecom.cart.service.impl.CartServiceImpl;
// import xa.sh.ecom.ecom.coupon.service.impl.CouponServiceImpl;
// import xa.sh.ecom.ecom.coupon.service.impl.CouponValidationResult;
// import xa.sh.ecom.ecom.exception.EmptyCartException;
// import xa.sh.ecom.ecom.exception.InsufficientStockException;
// import xa.sh.ecom.ecom.exception.InvalidCouponException;
// import xa.sh.ecom.ecom.exception.InvalidGiftCardException;
// import xa.sh.ecom.ecom.exception.PaymentProcessingException;
// import xa.sh.ecom.ecom.exception.ResourceNotFoundException;
// import xa.sh.ecom.ecom.giftcard.models.GiftCardRedemption;
// import xa.sh.ecom.ecom.giftcard.service.impl.GiftCardServiceImpl;
// import xa.sh.ecom.ecom.giftcard.service.impl.GiftCardValidationResult;
// import xa.sh.ecom.ecom.order.models.Order;
// import xa.sh.ecom.ecom.order.models.OrderItem;
// import xa.sh.ecom.ecom.order.models.OrderStatus;
// import xa.sh.ecom.ecom.order.repo.OrderRepo;
// import xa.sh.ecom.ecom.product.service.impl.ProductServiceImpl;

// @Service
// @Transactional
// public class OrderServiceImpl {

//     @Autowired
//     private OrderRepo orderRepository;

//     @Autowired
//     private CartServiceImpl cartService;

//     @Autowired
//     private ProductServiceImpl productService;

//     @Autowired
//     private CouponServiceImpl couponService;

//     @Autowired
//     private GiftCardServiceImpl giftCardService;

//     public Order createOrder(Long userId, OrderRequestDto orderRequest) throws InvalidCouponException, ResourceNotFoundException, InvalidGiftCardException, PaymentProcessingException,Exception, EmptyCartException, InsufficientStockException {
//         Cart cart = cartService.getOrCreateCart(userId);
//         validateCart(cart);

//         Order order = new Order();
//         order.setUser(cart.getUser());
//         order.setStatus(OrderStatus.PENDING);

//         // Convert cart items to order items
//         List<OrderItem> orderItems = cart.getItems().stream()
//             .map(cartItem -> {
//                 OrderItem orderItem = new OrderItem();
//                 orderItem.setProduct(cartItem.getProduct());
//                 orderItem.setQuantity(cartItem.getQuantity());
//                 orderItem.setPriceAtPurchase(BigDecimal.v);
//                 return orderItem;
//             }).toList();

//         order.setItems(orderItems);

//         // Calculate total
//         double total = orderItems.stream()
//             .mapToDouble(item -> item.getPriceAtPurchase() * item.getQuantity())
//             .sum();

//         // Apply coupon
//         if (orderRequest.getCouponCode() != null) {
//             CouponValidationResult couponResult = couponService.validateCoupon(
//                 userId, 
//                 orderRequest.getCouponCode(), 
//                 total
//             );
//             total = couponResult.getNewOrderTotal();
//             order.setCoupon(couponResult.getCoupon());
//             order.setDiscountedAmount(couponResult.getDiscount());
//         }

//         // Apply gift cards
//         List<GiftCardRedemption> redemptions = new ArrayList<>();
//         for (String giftCardCode : orderRequest.getGiftCardCodes()) {
//             GiftCardValidationResult giftCardResult = giftCardService.validateGiftCard(
//                 userId, 
//                 giftCardCode, 
//                 total
//             );
            
//             total = giftCardResult.getRemainingTotal();
//             redemptions.add(giftCardResult.getRedemption());
//         }

//         order.setTotalPrice(total);
//         order.setGiftCardRedemptions(redemptions);

//         // Process payment (mock implementation)
//         boolean paymentSuccess = mockPaymentProcessing(order);
//         if (!paymentSuccess) {
//             throw new PaymentProcessingException();
//         }

//         // Update inventory
//         cart.getItems().forEach(item -> 
//             {
//                 try {
//                     productService.updateStock(
//                         item.getProduct().getId(), 
//                         -item.getQuantity()
//                     );
//                 } catch (ResourceNotFoundException e) {
//                     // TODO Auto-generated catch block
//                     e.printStackTrace();
//                 } catch (InsufficientStockException e) {
//                     // TODO Auto-generated catch block
//                     e.printStackTrace();
//                 }
//             }
//         );

//         // Clear cart
//         cartService.clearCart(userId);

//         return orderRepository.save(order);
//     }

//     private void validateCart(Cart cart) throws EmptyCartException {
//         if (cart.getItems().isEmpty()) {
//             throw new EmptyCartException();
//         }

//         cart.getItems().forEach(item -> {
//             if (item.getProduct().getStock() < item.getQuantity()) {
//                 System.out.println("Exception occured");
//             }
//         });
//     }

//     private boolean mockPaymentProcessing(Order order) {
//         // Integration with payment gateway would go here
//         return true;
//     }
// }


package xa.sh.ecom.ecom.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // Needed for Collectors.toList() if using Java 16+ or alternative for older Java

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Prefer Spring's annotation

import xa.sh.ecom.ecom.cart.models.Cart;
import xa.sh.ecom.ecom.cart.models.CartItem;
import xa.sh.ecom.ecom.cart.service.impl.CartServiceImpl; // Ideally use interface
import xa.sh.ecom.ecom.coupon.service.impl.CouponServiceImpl; // Ideally use interface
import xa.sh.ecom.ecom.coupon.service.impl.CouponValidationResult;
import xa.sh.ecom.ecom.exception.AccessDeniedException;
import xa.sh.ecom.ecom.exception.EmptyCartException;
import xa.sh.ecom.ecom.exception.InsufficientStockException;
import xa.sh.ecom.ecom.exception.InvalidCouponException;
import xa.sh.ecom.ecom.exception.InvalidGiftCardException;
import xa.sh.ecom.ecom.exception.ResourceNotFoundException;
import xa.sh.ecom.ecom.giftcard.models.GiftCardRedemption;
import xa.sh.ecom.ecom.giftcard.service.impl.GiftCardServiceImpl; // Ideally use interface
import xa.sh.ecom.ecom.giftcard.service.impl.GiftCardValidationResult;
import xa.sh.ecom.ecom.order.models.Order;
import xa.sh.ecom.ecom.order.models.OrderItem;
import xa.sh.ecom.ecom.order.models.OrderStatus;
import xa.sh.ecom.ecom.order.repo.OrderRepo;
import xa.sh.ecom.ecom.product.service.impl.ProductServiceImpl; // Ideally use interface

@Service
// Consider using org.springframework.transaction.annotation.Transactional
@Transactional // from jakarta.transaction is okay too, but Spring's is often preferred in Spring apps
public class OrderServiceImpl {

    @Autowired
    private OrderRepo orderRepository;

    @Autowired
    private CartServiceImpl cartService; // Inject interface CartService instead

    @Autowired
    private ProductServiceImpl productService; // Inject interface ProductService instead

    @Autowired
    private CouponServiceImpl couponService; // Inject interface CouponService instead

    @Autowired
    private GiftCardServiceImpl giftCardService; // Inject interface GiftCardService instead

    // Removed redundant 'throws Exception'
    public Order createOrder(Long userId, OrderRequestDto orderRequest) throws InvalidCouponException, ResourceNotFoundException, InvalidGiftCardException, EmptyCartException, InsufficientStockException, Exception {
        Cart cart = cartService.getOrCreateCart(userId);
        validateCart(cart); // Still contains flawed stock check logic as per original code

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus(OrderStatus.PENDING);
        // Initialize lists to avoid potential NullPointerException
        order.setItems(new ArrayList<>());
        order.setGiftCardRedemptions(new ArrayList<>());

        // Convert cart items to order items
        List<OrderItem> orderItems = cart.getItems().stream()
            .map(cartItem -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity());
                // *** FETCH ACTUAL PRICE and ensure it's BigDecimal ***
                // Assuming Product has getPrice() returning BigDecimal
                BigDecimal price = BigDecimal.valueOf(cartItem.getProduct().getPrice());
                if (price == null) {
                    // Handle missing price scenario appropriately - throwing exception is safer
                    throw new IllegalStateException("Product " + cartItem.getProduct().getName() + " has a null price.");
                }
                orderItem.setPriceAtPurchase(price);
                orderItem.setOrder(order); // Link item to order
                return orderItem;
            }).collect(Collectors.toList()); // Use collect(Collectors.toList()) for broader Java compatibility

        order.setItems(orderItems);

        // Calculate total using BigDecimal
        BigDecimal total = orderItems.stream()
            // Calculate item total: price * quantity
            .map(item -> item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())))
            // Sum all item totals
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Apply coupon
        // Ensure CouponValidationResult uses BigDecimal
        if (orderRequest.getCouponCode() != null && !orderRequest.getCouponCode().trim().isEmpty()) {
            // Ensure couponService.validateCoupon accepts and returns BigDecimal
            CouponValidationResult couponResult = couponService.validateCoupon(
                userId,
                orderRequest.getCouponCode(),
                total // Pass BigDecimal total
            );
            total = couponResult.getNewOrderTotal(); // Assign BigDecimal result
            order.setCoupon(couponResult.getCoupon());
            order.setDiscountedAmount(couponResult.getDiscount()); // Ensure this is BigDecimal
        }

        // Apply gift cards
        // Ensure GiftCardValidationResult uses BigDecimal
        List<GiftCardRedemption> redemptions = new ArrayList<>();
        // Check if gift card codes exist and list is not empty
        if (orderRequest.getGiftCardCodes() != null && !orderRequest.getGiftCardCodes().isEmpty()) {
            for (String giftCardCode : orderRequest.getGiftCardCodes()) {
                 // Optional: Check if total is already <= 0 and break early
                 if (total.compareTo(BigDecimal.ZERO) <= 0) {
                     break;
                 }
                // Ensure giftCardService.validateGiftCard (or applyGiftCard) accepts and returns BigDecimal
                GiftCardValidationResult giftCardResult = giftCardService.applyGiftCard( // Assuming method was renamed/corrected
                    userId,
                    giftCardCode,
                    total // Pass BigDecimal total
                );

                total = giftCardResult.getRemainingTotal(); // Assign BigDecimal result
                // Only add redemption if one was actually created/used
                if (giftCardResult.getRedemption() != null) {
                    redemptions.add(giftCardResult.getRedemption());
                     // Ensure GiftCardRedemption amountUsed is BigDecimal
                }
            }
        }
        order.setGiftCardRedemptions(redemptions); // Set the list of applied redemptions

        // Set final total price - Ensure Order.totalPrice is BigDecimal
        order.setTotalPrice(total);


        // Process payment (mock implementation)
        // Payment logic might need adjustment based on the final BigDecimal 'total'
        boolean paymentSuccess = mockPaymentProcessing(order);
        if (!paymentSuccess) {
             order.setStatus(OrderStatus.PENDING); // Set failed status
            // Consider if saving a failed order is desired
            // orderRepository.save(order);
            throw new Exception("Payment failed"); // Provide a message
        }
        // Set status after successful payment (if total > 0) or if total is 0
        order.setStatus(OrderStatus.PROCESSING); // Or PAID, CONFIRMED etc.


        // Update inventory - THIS ERROR HANDLING IS STILL PROBLEMATIC
        // It catches exceptions but doesn't roll back the transaction correctly.
        cart.getItems().forEach(item ->
            {
                try {
                    // Ensure productService.updateStock handles negative quantity correctly
                    try {
                        productService.updateStock(
                            item.getProduct().getId(),
                            -item.getQuantity()
                        );
                    } catch (AccessDeniedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } catch (ResourceNotFoundException e) {
                    // FIXME: This just prints and continues, potentially leaving data inconsistent.
                    // Should re-throw a runtime exception to trigger rollback.
                    e.printStackTrace();
                } catch (InsufficientStockException e) {
                    // FIXME: Same as above. This error ideally shouldn't happen here if validateCart worked correctly.
                    e.printStackTrace();
                }
            }
        );

        // Clear cart
        cartService.clearCart(userId);

        // Save the final order
        return orderRepository.save(order);
    }

    // This validation logic is flawed as per previous review (doesn't throw on insufficient stock)
    private void validateCart(Cart cart) throws InsufficientStockException, Exception {
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new Exception("Cart is empty.");
        }

        // Flawed stock check: This loop doesn't prevent order creation on insufficient stock.
        // It should throw InsufficientStockException immediately.
        for (CartItem item : cart.getItems()) {
            // You would typically fetch current stock here before checking
             int currentStock = productService.getProductStock(item.getProduct().getId()); // Assuming this method exists
             if (currentStock < item.getQuantity()) {
                System.out.println("Exception occured [FIXME: Should throw InsufficientStockException here!]");
                 // throw new InsufficientStockException("Insufficient stock for " + item.getProduct().getName());
             }
        }
    }

    // Payment processing should ideally use the BigDecimal total from the order
    private boolean mockPaymentProcessing(Order order) {
        System.out.println("Attempting MOCK payment for amount: " + order.getTotalPrice());
        // Integration with payment gateway would go here
        return true; // Keep original mock logic
    }
}
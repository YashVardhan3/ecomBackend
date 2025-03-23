package xa.sh.ecom.ecom.order.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import xa.sh.ecom.ecom.cart.models.Cart;
import xa.sh.ecom.ecom.cart.service.impl.CartServiceImpl;
import xa.sh.ecom.ecom.coupon.service.impl.CouponServiceImpl;
import xa.sh.ecom.ecom.coupon.service.impl.CouponValidationResult;
import xa.sh.ecom.ecom.exception.EmptyCartException;
import xa.sh.ecom.ecom.exception.InsufficientStockException;
import xa.sh.ecom.ecom.exception.InvalidCouponException;
import xa.sh.ecom.ecom.exception.InvalidGiftCardException;
import xa.sh.ecom.ecom.exception.PaymentProcessingException;
import xa.sh.ecom.ecom.exception.ResourceNotFoundException;
import xa.sh.ecom.ecom.giftcard.models.GiftCardRedemption;
import xa.sh.ecom.ecom.giftcard.service.impl.GiftCardServiceImpl;
import xa.sh.ecom.ecom.giftcard.service.impl.GiftCardValidationResult;
import xa.sh.ecom.ecom.order.models.Order;
import xa.sh.ecom.ecom.order.models.OrderItem;
import xa.sh.ecom.ecom.order.models.OrderStatus;
import xa.sh.ecom.ecom.order.repo.OrderRepo;
import xa.sh.ecom.ecom.product.service.impl.ProductServiceImpl;

@Service
@Transactional
public class OrderServiceImpl {

    @Autowired
    private OrderRepo orderRepository;

    @Autowired
    private CartServiceImpl cartService;

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private CouponServiceImpl couponService;

    @Autowired
    private GiftCardServiceImpl giftCardService;

    public Order createOrder(Long userId, OrderRequestDto orderRequest) throws InvalidCouponException, ResourceNotFoundException, InvalidGiftCardException, PaymentProcessingException,Exception, EmptyCartException, InsufficientStockException {
        Cart cart = cartService.getOrCreateCart(userId);
        validateCart(cart);

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus(OrderStatus.PENDING);

        // Convert cart items to order items
        List<OrderItem> orderItems = cart.getItems().stream()
            .map(cartItem -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPriceAtPurchase(cartItem.getProduct().getPrice());
                return orderItem;
            }).toList();

        order.setItems(orderItems);

        // Calculate total
        double total = orderItems.stream()
            .mapToDouble(item -> item.getPriceAtPurchase() * item.getQuantity())
            .sum();

        // Apply coupon
        if (orderRequest.getCouponCode() != null) {
            CouponValidationResult couponResult = couponService.validateCoupon(
                userId, 
                orderRequest.getCouponCode(), 
                total
            );
            total = couponResult.getNewOrderTotal();
            order.setCoupon(couponResult.getCoupon());
            order.setDiscountedAmount(couponResult.getDiscount());
        }

        // Apply gift cards
        List<GiftCardRedemption> redemptions = new ArrayList<>();
        for (String giftCardCode : orderRequest.getGiftCardCodes()) {
            GiftCardValidationResult giftCardResult = giftCardService.validateGiftCard(
                userId, 
                giftCardCode, 
                total
            );
            
            total = giftCardResult.getRemainingTotal();
            redemptions.add(giftCardResult.getRedemption());
        }

        order.setTotalPrice(total);
        order.setGiftCardRedemptions(redemptions);

        // Process payment (mock implementation)
        boolean paymentSuccess = mockPaymentProcessing(order);
        if (!paymentSuccess) {
            throw new PaymentProcessingException();
        }

        // Update inventory
        cart.getItems().forEach(item -> 
            {
                try {
                    productService.updateStock(
                        item.getProduct().getId(), 
                        -item.getQuantity()
                    );
                } catch (ResourceNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InsufficientStockException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        );

        // Clear cart
        cartService.clearCart(userId);

        return orderRepository.save(order);
    }

    private void validateCart(Cart cart) throws EmptyCartException {
        if (cart.getItems().isEmpty()) {
            throw new EmptyCartException();
        }

        cart.getItems().forEach(item -> {
            if (item.getProduct().getStock() < item.getQuantity()) {
                System.out.println("Exception occured");
            }
        });
    }

    private boolean mockPaymentProcessing(Order order) {
        // Integration with payment gateway would go here
        return true;
    }
}

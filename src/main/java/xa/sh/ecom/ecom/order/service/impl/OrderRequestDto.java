package xa.sh.ecom.ecom.order.service.impl;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import xa.sh.ecom.ecom.coupon.models.Coupon;
import xa.sh.ecom.ecom.giftcard.models.GiftCardRedemption;
import xa.sh.ecom.ecom.models.User;
import xa.sh.ecom.ecom.order.models.OrderItem;
import xa.sh.ecom.ecom.order.models.OrderStatus;

@Data

public class OrderRequestDto {
    private Long id;

    private User user;

    private Double totalPrice;

    private OrderStatus status;

    private Coupon coupon;

    private Double discountedAmount;

    private List<OrderItem> items = new ArrayList<>();

    private List<GiftCardRedemption> giftCardRedemptions = new ArrayList<>();
    private String couponCode;  // The user-provided coupon code
    private List<String> giftCardCodes;
}

package xa.sh.ecom.ecom.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import xa.sh.ecom.ecom.coupon.models.Coupon;
import xa.sh.ecom.ecom.giftcard.models.GiftCardRedemption;
import xa.sh.ecom.ecom.models.User;
import xa.sh.ecom.ecom.order.models.OrderItem;
import xa.sh.ecom.ecom.order.models.OrderStatus;


public class OrderRequestDto {
    private Long id;

    public OrderRequestDto() {
    }
    public OrderRequestDto(Long id, User user, BigDecimal totalPrice, OrderStatus status, Coupon coupon,
            BigDecimal discountedAmount, List<OrderItem> items, List<GiftCardRedemption> giftCardRedemptions,
            String couponCode, List<String> giftCardCodes) {
        this.id = id;
        this.user = user;
        this.totalPrice = totalPrice;
        this.status = status;
        this.coupon = coupon;
        this.discountedAmount = discountedAmount;
        this.items = items;
        this.giftCardRedemptions = giftCardRedemptions;
        this.couponCode = couponCode;
        this.giftCardCodes = giftCardCodes;
    }
    private User user;

    private BigDecimal totalPrice;

    private OrderStatus status;

    private Coupon coupon;

    private BigDecimal discountedAmount;

    private List<OrderItem> items = new ArrayList<>();

    private List<GiftCardRedemption> giftCardRedemptions = new ArrayList<>();
    private String couponCode;  // The user-provided coupon code
    private List<String> giftCardCodes;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    public OrderStatus getStatus() {
        return status;
    }
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    public Coupon getCoupon() {
        return coupon;
    }
    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }
    public BigDecimal getDiscountedAmount() {
        return discountedAmount;
    }
    public void setDiscountedAmount(BigDecimal discountedAmount) {
        this.discountedAmount = discountedAmount;
    }
    public List<OrderItem> getItems() {
        return items;
    }
    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
    public List<GiftCardRedemption> getGiftCardRedemptions() {
        return giftCardRedemptions;
    }
    public void setGiftCardRedemptions(List<GiftCardRedemption> giftCardRedemptions) {
        this.giftCardRedemptions = giftCardRedemptions;
    }
    public String getCouponCode() {
        return couponCode;
    }
    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
    public List<String> getGiftCardCodes() {
        return giftCardCodes;
    }
    public void setGiftCardCodes(List<String> giftCardCodes) {
        this.giftCardCodes = giftCardCodes;
    }
}

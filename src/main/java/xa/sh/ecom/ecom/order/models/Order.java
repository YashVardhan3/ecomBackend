package xa.sh.ecom.ecom.order.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import xa.sh.ecom.ecom.coupon.models.Coupon;
import xa.sh.ecom.ecom.giftcard.models.GiftCardRedemption;
import xa.sh.ecom.ecom.models.User;

@Entity
@Table(name = "orders")
public class Order {
    public Order() {
    }

    public Order(Long id, User user, BigDecimal totalPrice, OrderStatus status, Coupon coupon,
            BigDecimal discountedAmount, List<OrderItem> items, List<GiftCardRedemption> giftCardRedemptions) {
        this.id = id;
        this.user = user;
        this.totalPrice = totalPrice;
        this.status = status;
        this.coupon = coupon;
        this.discountedAmount = discountedAmount;
        this.items = items;
        this.giftCardRedemptions = giftCardRedemptions;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Column(precision = 10, scale = 2)
    private BigDecimal discountedAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "order")
    private List<GiftCardRedemption> giftCardRedemptions = new ArrayList<>();

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
}

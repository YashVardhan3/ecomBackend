package xa.sh.ecom.ecom.giftcard.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import xa.sh.ecom.ecom.models.User;
import xa.sh.ecom.ecom.order.models.Order;

@Entity
@Table(name = "gift_card_redemptions")
public class GiftCardRedemption {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private GiftCard giftCard;

    @ManyToOne
    private Order order;

    public GiftCardRedemption(Long id, GiftCard giftCard, Order order, User user, BigDecimal amountUsed,
            LocalDateTime redemptionDate) {
        this.id = id;
        this.giftCard = giftCard;
        this.order = order;
        this.user = user;
        this.amountUsed = amountUsed;
        this.redemptionDate = redemptionDate;
    }

    @ManyToOne
    private User user;

    @Column(precision = 10, scale = 2)
    private BigDecimal amountUsed;

    private LocalDateTime redemptionDate;

    public GiftCardRedemption() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GiftCard getGiftCard() {
        return giftCard;
    }

    public void setGiftCard(GiftCard giftCard) {
        this.giftCard = giftCard;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getAmountUsed() {
        return amountUsed;
    }

    public void setAmountUsed(BigDecimal amountUsed) {
        this.amountUsed = amountUsed;
    }

    public LocalDateTime getRedemptionDate() {
        return redemptionDate;
    }

    public void setRedemptionDate(LocalDateTime redemptionDate) {
        this.redemptionDate = redemptionDate;
    }
}
package xa.sh.ecom.ecom.giftcard.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import xa.sh.ecom.ecom.models.User;
import xa.sh.ecom.ecom.order.models.Order;

@Data
@Entity
@Table(name = "gift_card_redemptions")
public class GiftCardRedemption {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private GiftCard giftCard;

    @ManyToOne
    private Order order;

    @ManyToOne
    private User user;

    @Column(precision = 10, scale = 2)
    private Double amountUsed;
}
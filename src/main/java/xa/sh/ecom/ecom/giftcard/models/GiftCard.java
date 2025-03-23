package xa.sh.ecom.ecom.giftcard.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;

@Data
@Entity
@Table(name = "gift_cards")
public class GiftCard {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 20)
    private String code;

    @Column(precision = 10, scale = 2)
    private Double initialValue;

    @Column(precision = 10, scale = 2)
    private Double remainingBalance;

    private LocalDate expirationDate;
    private Boolean isActive = true;

    @Version
    private Long version;
}

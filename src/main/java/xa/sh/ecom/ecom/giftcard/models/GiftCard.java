package xa.sh.ecom.ecom.giftcard.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "gift_cards")
public class GiftCard {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 20)
    private String code;

    @Column(precision = 10, scale = 2)
    private BigDecimal initialValue;

    public GiftCard() {
    }

    public GiftCard(Long id, String code, BigDecimal initialValue, BigDecimal remainingBalance,
            LocalDate expirationDate, Boolean isActive, Long version) {
        this.id = id;
        this.code = code;
        this.initialValue = initialValue;
        this.remainingBalance = remainingBalance;
        this.expirationDate = expirationDate;
        this.isActive = isActive;
        this.version = version;
    }

    @Column(precision = 10, scale = 2)
    private BigDecimal remainingBalance;

    private LocalDate expirationDate;
    private Boolean isActive = true;

    @Version
    private Long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(BigDecimal initialValue) {
        this.initialValue = initialValue;
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}

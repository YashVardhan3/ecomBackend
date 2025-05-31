package xa.sh.ecom.ecom.giftcard.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public class GiftCardRequestDTO {

    public GiftCardRequestDTO() {
    }

    public GiftCardRequestDTO(
            @NotNull(message = "Initial balance cannot be null") @DecimalMin(value = "0.01", message = "Initial balance must be positive") BigDecimal initialBalance,
            @Future(message = "Expiration date must be in the future") LocalDate expirationDate) {
        this.initialBalance = initialBalance;
        this.expirationDate = expirationDate;
    }

    @NotNull(message = "Initial balance cannot be null")
    @DecimalMin(value = "0.01", message = "Initial balance must be positive") // Min value > 0
    private BigDecimal initialBalance;

    @Future(message = "Expiration date must be in the future") // Must be strictly future
    private LocalDate expirationDate; // Optional, can be null if it never expires

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    //private String description; // Optional description or note
}

package xa.sh.ecom.ecom.giftcard.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GiftCardRequestDTO {

    @NotNull(message = "Initial balance cannot be null")
    @DecimalMin(value = "0.01", message = "Initial balance must be positive") // Min value > 0
    private BigDecimal initialBalance;

    @Future(message = "Expiration date must be in the future") // Must be strictly future
    private LocalDate expirationDate; // Optional, can be null if it never expires

    //private String description; // Optional description or note
}

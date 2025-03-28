package xa.sh.ecom.ecom.giftcard.service.impl;


import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xa.sh.ecom.ecom.giftcard.models.GiftCardRedemption;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GiftCardValidationResult {
    private boolean isValid;
    private BigDecimal remainingTotal;
    private GiftCardRedemption redemption;
}

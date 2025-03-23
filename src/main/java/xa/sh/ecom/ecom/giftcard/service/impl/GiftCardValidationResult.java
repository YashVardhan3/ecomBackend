package xa.sh.ecom.ecom.giftcard.service.impl;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import xa.sh.ecom.ecom.giftcard.models.GiftCardRedemption;

@Data
@Getter
@AllArgsConstructor
public class GiftCardValidationResult {
    private boolean isValid;
    private double remainingTotal;
    private GiftCardRedemption redemption;
}

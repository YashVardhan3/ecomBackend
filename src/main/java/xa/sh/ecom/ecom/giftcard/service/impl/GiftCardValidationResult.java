package xa.sh.ecom.ecom.giftcard.service.impl;


import java.math.BigDecimal;

import xa.sh.ecom.ecom.giftcard.models.GiftCardRedemption;

public class GiftCardValidationResult {
    public GiftCardValidationResult(boolean isValid, BigDecimal remainingTotal, GiftCardRedemption redemption) {
        this.isValid = isValid;
        this.remainingTotal = remainingTotal;
        this.redemption = redemption;
    }
    public GiftCardValidationResult() {
    }
    private boolean isValid;
    private BigDecimal remainingTotal;
    private GiftCardRedemption redemption;
    public boolean isValid() {
        return isValid;
    }
    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }
    public BigDecimal getRemainingTotal() {
        return remainingTotal;
    }
    public void setRemainingTotal(BigDecimal remainingTotal) {
        this.remainingTotal = remainingTotal;
    }
    public GiftCardRedemption getRedemption() {
        return redemption;
    }
    public void setRedemption(GiftCardRedemption redemption) {
        this.redemption = redemption;
    }
}

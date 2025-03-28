package xa.sh.ecom.ecom.giftcard.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Use Spring's Transactional

import xa.sh.ecom.ecom.exception.InvalidGiftCardException;
import xa.sh.ecom.ecom.exception.ResourceNotFoundException;
import xa.sh.ecom.ecom.giftcard.models.GiftCard;
import xa.sh.ecom.ecom.giftcard.models.GiftCardRedemption;
import xa.sh.ecom.ecom.giftcard.repo.GiftCardRedeemRepo;
import xa.sh.ecom.ecom.giftcard.repo.GiftCardRepo;
import xa.sh.ecom.ecom.models.User;
import xa.sh.ecom.ecom.repository.UserRepository;

@Service
// Use org.springframework.transaction.annotation.Transactional
public class GiftCardServiceImpl { // Assuming an interface exists

    private final GiftCardRepo giftCardRepo;
    private final GiftCardRedeemRepo redemptionRepo;
    private final UserRepository userRepo;

    // Constructor Injection
    public GiftCardServiceImpl(GiftCardRepo giftCardRepo, GiftCardRedeemRepo redemptionRepo, UserRepository userRepo) {
        this.giftCardRepo = giftCardRepo;
        this.redemptionRepo = redemptionRepo;
        this.userRepo = userRepo;
    }

    @Transactional // Apply transaction to this method specifically (or keep at class level)
    public GiftCardValidationResult applyGiftCard(Long userId, String giftCardCode, BigDecimal orderTotal)
            throws ResourceNotFoundException, InvalidGiftCardException {

        // 1. Find Gift Card
        GiftCard giftCard = giftCardRepo.findByCode(giftCardCode)
                .orElseThrow(() -> new ResourceNotFoundException("Gift Card not found with code: " + giftCardCode));

        // 2. Validate Status (using helper method)
        validateGiftCardStatus(giftCard);

        // 3. Check if there's anything to apply against
        if (orderTotal.compareTo(BigDecimal.ZERO) <= 0) {
             // Or return a result indicating nothing was applied
            return new GiftCardValidationResult(true, orderTotal, null); // No redemption created
        }

        // 4. Calculate usable amount
        BigDecimal amountToUse = orderTotal.min(giftCard.getRemainingBalance());

        // If no amount can be used from the gift card (e.g., balance was zero, but passed initial check somehow)
        if (amountToUse.compareTo(BigDecimal.ZERO) <= 0) {
             return new GiftCardValidationResult(true, orderTotal, null); // No redemption needed
        }

        // 5. Calculate new totals
        BigDecimal remainingOrderTotal = orderTotal.subtract(amountToUse); // Use subtract for clarity
        BigDecimal newGiftCardBalance = giftCard.getRemainingBalance().subtract(amountToUse);

        // 6. Update Gift Card
        giftCard.setRemainingBalance(newGiftCardBalance);
        // No need to call save explicitly IF using JPA context persistence propagation within the transaction.
        // However, explicit save is clearer and safer if unsure about context configuration.
        giftCardRepo.save(giftCard);

        // 7. Find User
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // 8. Create and Save Redemption Record
        GiftCardRedemption redemption = new GiftCardRedemption();
        redemption.setGiftCard(giftCard);
        redemption.setUser(user);
        redemption.setAmountUsed(amountToUse);
        redemption.setRedemptionDate(LocalDateTime.now()); // Consider adding redemption date
        redemptionRepo.save(redemption);

        // 9. Return Result
        // Assuming GiftCardValidationResult(boolean success, BigDecimal remainingOrderTotal, GiftCardRedemption redemption)
        return new GiftCardValidationResult(true, remainingOrderTotal, redemption);
    }

    // Helper method for validation logic
    private void validateGiftCardStatus(GiftCard giftCard) throws InvalidGiftCardException {
        if (!giftCard.getIsActive()) {
            throw new InvalidGiftCardException("Gift card is inactive");
        }

        if (giftCard.getExpirationDate() != null &&
            giftCard.getExpirationDate().isBefore(LocalDate.now())) {
            throw new InvalidGiftCardException("Gift card has expired");
        }

        // Use compareTo for BigDecimal comparison
        if (giftCard.getRemainingBalance().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidGiftCardException("Gift card has no remaining balance");
        }
    }

    // Removed the redundant redeemGiftCard method
    // public void redeemGiftCard(GiftCardRedemption redemption) { ... }
}
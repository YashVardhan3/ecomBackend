package xa.sh.ecom.ecom.giftcard.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Use Spring's Transactional

import xa.sh.ecom.ecom.exception.InvalidGiftCardException;
import xa.sh.ecom.ecom.exception.ResourceNotFoundException;
import xa.sh.ecom.ecom.giftcard.dto.GiftCardRequestDTO;
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
        if (orderTotal==null||orderTotal.compareTo(BigDecimal.ZERO) <= 0) {
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


    @Transactional
    public GiftCard createGiftCard(GiftCardRequestDTO dto) {
        //log.info("Attempting to create new gift card with balance: {}", dto.getInitialBalance());

        GiftCard giftCard = new GiftCard();
        giftCard.setInitialValue(dto.getInitialBalance());
        //giftCard.setInitialBalance(dto.getInitialBalance());
        giftCard.setRemainingBalance(dto.getInitialBalance()); // Remaining starts same as initial
        giftCard.setExpirationDate(dto.getExpirationDate()); // Can be null
        //giftCard.setDescription(dto.getDescription()); // Can be null
        giftCard.setIsActive(true); // Active by default on creation

        // Generate a unique code (simple UUID-based example)
        String uniqueCode;
        int attempts = 0;
        int maxAttempts = 5; // Prevent infinite loop
        do {
            // Generate a random code (e.g., part of a UUID)
            uniqueCode = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16).toUpperCase();
            attempts++;
            if (attempts > maxAttempts) {
                //log.error("Failed to generate a unique gift card code after {} attempts.", maxAttempts);
                throw new RuntimeException("Could not generate a unique gift card code."); // Or a custom exception
            }
        } while (giftCardRepo.existsByCode(uniqueCode)); // Check uniqueness in DB

        giftCard.setCode(uniqueCode);

        GiftCard savedGiftCard = giftCardRepo.save(giftCard);
        //log.info("Gift card created successfully with ID: {} and code: {}", savedGiftCard.getId(), savedGiftCard.getCode());
        return savedGiftCard;
    }
    // Removed the redundant redeemGiftCard method
    // public void redeemGiftCard(GiftCardRedemption redemption) { ... }
}
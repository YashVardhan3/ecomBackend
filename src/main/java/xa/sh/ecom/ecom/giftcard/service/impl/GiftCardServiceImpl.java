package xa.sh.ecom.ecom.giftcard.service.impl;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import xa.sh.ecom.ecom.exception.InvalidGiftCardException;
import xa.sh.ecom.ecom.exception.ResourceNotFoundException;
import xa.sh.ecom.ecom.giftcard.models.GiftCard;
import xa.sh.ecom.ecom.giftcard.models.GiftCardRedemption;
import xa.sh.ecom.ecom.giftcard.repo.GiftCardRedeemRepo;
import xa.sh.ecom.ecom.giftcard.repo.GiftCardRepo;
import xa.sh.ecom.ecom.models.User;
import xa.sh.ecom.ecom.repository.UserRepository;

@Service
@Transactional
public class GiftCardServiceImpl {

    @Autowired
    private GiftCardRepo giftCardRepo;

    @Autowired
    private GiftCardRedeemRepo redemptionRepo;

    @Autowired
    private UserRepository userRepo;

    // public GiftCardValidationResult validateGiftCard(Long userId, String code, double remainingTotal) {
    //     GiftCard giftCard = giftCardRepository.findByCode(code)
    //             .orElseThrow(new InvalidGiftCardException("Invalid gift card"));
    //         //.orElseThrow(() -> new InvalidGiftCardException("Invalid gift card"))
    //     validateGiftCardStatus(giftCard);

    //     double usableAmount = Math.min(giftCard.getRemainingBalance(), remainingTotal);
    //     double newRemainingTotal = remainingTotal - usableAmount;

    //     GiftCardRedemption redemption = new GiftCardRedemption();
    //     redemption.setGiftCard(giftCard);
    //     redemption.setAmountUsed(usableAmount);

    //     return new GiftCardValidationResult(
    //         giftCard,
    //         redemption,
    //         newRemainingTotal
    //     );
    // }
    public GiftCardValidationResult validateGiftCard(Long userId, String giftCardCode, double orderTotal) throws ResourceNotFoundException, InvalidGiftCardException  {
        Optional<GiftCard> giftCardN = giftCardRepo.findByCode(giftCardCode);
        if (giftCardN.isEmpty()) {
            throw new ResourceNotFoundException("Gifcard Not Found");
        }
        GiftCard giftCard = giftCardN.get();
        if (!giftCard.getIsActive() || giftCard.getRemainingBalance() <= 0 || giftCard.getExpirationDate().isBefore(LocalDate.now())) {
            throw new InvalidGiftCardException("Gift card is invalid or expired");
        }

        double amountUsed = Math.min(giftCard.getRemainingBalance(), orderTotal);
        double remainingTotal = orderTotal - amountUsed;

        giftCard.setRemainingBalance(giftCard.getRemainingBalance() - amountUsed);
        giftCardRepo.save(giftCard);

        GiftCardRedemption redemption = new GiftCardRedemption();
        redemption.setGiftCard(giftCard);
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }
        redemption.setUser(user.get()); // Ensure you pass the correct user
        redemption.setAmountUsed(amountUsed);
        redemptionRepo.save(redemption);

        return new GiftCardValidationResult(true, remainingTotal, redemption);
    }




    public void redeemGiftCard(GiftCardRedemption redemption) {
        GiftCard giftCard = redemption.getGiftCard();
        giftCard.setRemainingBalance(giftCard.getRemainingBalance() - redemption.getAmountUsed());
        giftCardRepo.save(giftCard);
        redemptionRepo.save(redemption);
    }

    private void validateGiftCardStatus(GiftCard giftCard) throws InvalidGiftCardException {
        if (!giftCard.getIsActive()) {
            throw new InvalidGiftCardException("Gift card is inactive");
        }

        if (giftCard.getExpirationDate() != null && 
            giftCard.getExpirationDate().isBefore(LocalDate.now())) {
            throw new InvalidGiftCardException("Gift card has expired");
        }

        if (giftCard.getRemainingBalance() <= 0) {
            throw new InvalidGiftCardException("Gift card has no remaining balance");
        }
    }
}
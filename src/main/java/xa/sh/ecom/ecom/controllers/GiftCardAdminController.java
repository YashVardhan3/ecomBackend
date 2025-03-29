package xa.sh.ecom.ecom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import xa.sh.ecom.ecom.giftcard.dto.GiftCardRequestDTO;
import xa.sh.ecom.ecom.giftcard.models.GiftCard;
// TODO: import xa.sh.ecom.ecom.giftcard.service.GiftCardService; // Use interface
import xa.sh.ecom.ecom.giftcard.service.impl.GiftCardServiceImpl; // Using impl for now

@RestController
@RequestMapping("/api/admin/giftcards") // Admin-specific path
@PreAuthorize("hasRole('ADMIN')") // Secure all methods
public class GiftCardAdminController {

    private final GiftCardServiceImpl giftCardService; // TODO: Use GiftCardService interface

    // Constructor Injection
    @Autowired
    public GiftCardAdminController(GiftCardServiceImpl giftCardService) {
        this.giftCardService = giftCardService;
    }

    @PostMapping
    public ResponseEntity<GiftCard> createGiftCard(@Valid @RequestBody GiftCardRequestDTO giftCardDto) {
        GiftCard createdGiftCard = giftCardService.createGiftCard(giftCardDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGiftCard);
    }

    // Add other admin endpoints (get balance, deactivate, list, etc.) here
    // Example:
    // @GetMapping("/{code}")
    // public ResponseEntity<GiftCard> getGiftCardDetails(@PathVariable String code) { ... }

}
package xa.sh.ecom.ecom.giftcard.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xa.sh.ecom.ecom.giftcard.models.GiftCardRedemption;

@Repository
public interface GiftCardRedeemRepo extends JpaRepository<GiftCardRedemption, Long> {

}

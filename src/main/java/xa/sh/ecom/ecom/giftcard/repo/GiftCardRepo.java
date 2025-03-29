package xa.sh.ecom.ecom.giftcard.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xa.sh.ecom.ecom.giftcard.models.GiftCard;


@Repository
public interface GiftCardRepo extends JpaRepository<GiftCard, Long> {
    Optional<GiftCard> findByCode(String code);

    boolean existsByCode(String uniqueCode);
}

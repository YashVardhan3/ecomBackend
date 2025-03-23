package xa.sh.ecom.ecom.coupon.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import xa.sh.ecom.ecom.models.User;

@Data
@Entity
@Table(name = "coupon_usages")
public class CouponUsage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Coupon coupon;

    private Integer usageCount = 0;

    // Constructor for creating a new CouponUsage
    public CouponUsage(Long userId, Coupon coupon) {
        this.user = new User(); // Assuming User has a constructor or setter for ID
        this.user.setId(userId);
        this.coupon = coupon;
    }

    // Default constructor required by JPA
    public CouponUsage() {}
}
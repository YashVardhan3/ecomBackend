package xa.sh.ecom.ecom.coupon.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import xa.sh.ecom.ecom.models.User;


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

    

    public CouponUsage(Long id, User user, Coupon coupon, Integer usageCount) {
        this.id = id;
        this.user = user;
        this.coupon = coupon;
        this.usageCount = usageCount;
    }

    // Constructor for creating a new CouponUsage
    public CouponUsage(Long userId, Coupon coupon) {
        this.user = new User(); // Assuming User has a constructor or setter for ID
        this.user.setId(userId);
        this.coupon = coupon;
    }

    // Default constructor required by JPA
    public CouponUsage() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }
}
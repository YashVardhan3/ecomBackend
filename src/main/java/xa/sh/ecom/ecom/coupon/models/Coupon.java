package xa.sh.ecom.ecom.coupon.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "coupons")
public class Coupon {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 20)
    private String code;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Column(precision = 10, scale = 2)
    private Double discountValue;

    @Column(precision = 10, scale = 2)
    private Double maxDiscount;

    private Boolean isActive = true;
    private LocalDate validUntil;
    private Integer maxUses;
    private Integer usedCount = 0;

    private Boolean forNewCustomers = false;
    private Integer allowedUsesPerUser = 1;

    @OneToMany(mappedBy = "coupon")
    private Set<CouponUsage> usages = new HashSet<>();
}
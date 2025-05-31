package xa.sh.ecom.ecom.coupon.models;

import java.math.BigDecimal;
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

@Entity
@Table(name = "coupons")
public class Coupon {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 20)
    private String code;

    public Coupon(Long id, String code, DiscountType discountType, BigDecimal discountValue, BigDecimal maxDiscount,
            Boolean isActive, LocalDate validUntil, Integer maxUses, Integer usedCount, Boolean forNewCustomers,
            Integer allowedUsesPerUser, Set<CouponUsage> usages) {
        this.id = id;
        this.code = code;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.maxDiscount = maxDiscount;
        this.isActive = isActive;
        this.validUntil = validUntil;
        this.maxUses = maxUses;
        this.usedCount = usedCount;
        this.forNewCustomers = forNewCustomers;
        this.allowedUsesPerUser = allowedUsesPerUser;
        this.usages = usages;
    }

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    public Coupon() {
    }

    @Column(precision = 10, scale = 2)
    private BigDecimal discountValue;
    

    public Coupon(String code, DiscountType discountType, BigDecimal discountValue, Long id, BigDecimal maxDiscount, Integer maxUses, LocalDate validUntil) {
        this.code = code;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.id = id;
        this.maxDiscount = maxDiscount;
        this.maxUses = maxUses;
        this.validUntil = validUntil;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    @Column(precision = 10, scale = 2)
    private BigDecimal maxDiscount;

    private Boolean isActive = true;
    private LocalDate validUntil;
    private Integer maxUses;
    private Integer usedCount = 0;

    private Boolean forNewCustomers = false;
    private Integer allowedUsesPerUser = 1;

    @OneToMany(mappedBy = "coupon")
    private Set<CouponUsage> usages = new HashSet<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(BigDecimal maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }

    public Integer getMaxUses() {
        return maxUses;
    }

    public void setMaxUses(Integer maxUses) {
        this.maxUses = maxUses;
    }

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public Boolean getForNewCustomers() {
        return forNewCustomers;
    }

    public void setForNewCustomers(Boolean forNewCustomers) {
        this.forNewCustomers = forNewCustomers;
    }

    public Integer getAllowedUsesPerUser() {
        return allowedUsesPerUser;
    }

    public void setAllowedUsesPerUser(Integer allowedUsesPerUser) {
        this.allowedUsesPerUser = allowedUsesPerUser;
    }

    public Set<CouponUsage> getUsages() {
        return usages;
    }

    public void setUsages(Set<CouponUsage> usages) {
        this.usages = usages;
    }
}
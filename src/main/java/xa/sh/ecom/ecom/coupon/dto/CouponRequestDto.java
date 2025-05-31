package xa.sh.ecom.ecom.coupon.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import xa.sh.ecom.ecom.coupon.models.DiscountType;

public class CouponRequestDto {

    @NotBlank(message = "Coupon code cannot be blank")
    @Size(min = 3, max = 20, message = "Coupon code must be between 3 and 20 characters")
    private String code;

    @NotNull(message = "Discount type cannot be null")
    private DiscountType discountType;

    @NotNull(message = "Discount value cannot be null")
    @Positive(message = "Discount value must be positive")
    private BigDecimal discountValue;

    // Optional: Maximum discount amount (especially relevant for PERCENTAGE type)
    @PositiveOrZero(message = "Maximum discount must be zero or positive")
    private BigDecimal maxDiscount; // Defaults handled in service if null

    // Optional: Expiry date
    @FutureOrPresent(message = "Valid until date must be today or in the future")
    private LocalDate validUntil;

    // Optional: Max total uses for the coupon
    @Min(value = 1, message = "Maximum uses must be at least 1")
    private Integer maxUses;
    

    

    // Optional: Flag for new customers only
    private Boolean forNewCustomers = false; // Default to false

    // Optional: Uses allowed per customer

    @Min(value = 1, message = "Allowed uses per user must be at least 1")
    private Integer allowedUsesPerUser = 1; // Default to 1

    // Active status - usually defaults to true on creation
    // private Boolean isActive = true; // No need to include in DTO, set in service

    public CouponRequestDto(String code, DiscountType discountType, BigDecimal discountValue, BigDecimal maxDiscount, Integer maxUses, LocalDate validUntil) {
        this.code = code;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.maxDiscount = maxDiscount;
        this.maxUses = maxUses;
        this.validUntil = validUntil;
    }

    public CouponRequestDto() {
    }

    public CouponRequestDto(String code, DiscountType discountType, BigDecimal discountValue, BigDecimal maxDiscount, Integer maxUses, LocalDate validUntil, Boolean forNewCustomers, Integer allowedUsesPerUser) {
        this.code = code;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.maxDiscount = maxDiscount;
        this.maxUses = maxUses;
        this.validUntil = validUntil;
        this.forNewCustomers = forNewCustomers;
        this.allowedUsesPerUser = allowedUsesPerUser;

    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public Boolean getForNewCustomers() {
        return forNewCustomers;
    }

    public void setForNewCustomers(Boolean forNewCustomers) {
        this.forNewCustomers = forNewCustomers;
    }

    public Integer getMaxUses() {
        return maxUses;
    }

    public void setMaxUses(Integer maxUses) {
        this.maxUses = maxUses;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }

    public BigDecimal getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(BigDecimal maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public Integer getAllowedUsesPerUser() {
        return allowedUsesPerUser;
    }

    public void setAllowedUsesPerUser(Integer allowedUsesPerUser) {
        this.allowedUsesPerUser = allowedUsesPerUser;
    }
}
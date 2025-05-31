package xa.sh.ecom.ecom.product.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;

import xa.sh.ecom.ecom.product.models.Category;
import xa.sh.ecom.ecom.seller.models.Seller;

public class ProductRequestDto {
    public ProductRequestDto() {
    }

    private Long id;
    private String name ;
    private Category category;
    private String description;
    private BigDecimal price;
    private Integer stock ;
    private Seller seller;
    private ArrayList<String> imageUrls;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}

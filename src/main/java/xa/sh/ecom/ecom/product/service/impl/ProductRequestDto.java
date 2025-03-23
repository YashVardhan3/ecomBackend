package xa.sh.ecom.ecom.product.service.impl;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xa.sh.ecom.ecom.product.models.Category;
import xa.sh.ecom.ecom.product.models.ProductImage;
import xa.sh.ecom.ecom.seller.models.Seller;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {
    private Long id;
    private String name ;
    private Category category;
    private String description;
    private Double price;
    private Integer stock ;
    private Seller seller;
    private ArrayList<ProductImage> images;
}

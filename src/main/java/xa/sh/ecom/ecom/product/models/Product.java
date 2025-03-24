package xa.sh.ecom.ecom.product.models;

import java.util.ArrayList;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xa.sh.ecom.ecom.seller.models.Seller;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column
    private Double price;
    
    @Min(0)
    @Column
    private Integer stock;

    @Lob
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private ArrayList<ProductImage> images = new ArrayList<>();
    
    @ManyToOne(fetch =  FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id" , nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Category category;



    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;
}
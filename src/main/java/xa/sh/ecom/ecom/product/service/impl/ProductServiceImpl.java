package xa.sh.ecom.ecom.product.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xa.sh.ecom.ecom.exception.AccessDeniedException;
import xa.sh.ecom.ecom.exception.InsufficientStockException;
import xa.sh.ecom.ecom.exception.ResourceNotFoundException;
import xa.sh.ecom.ecom.product.models.Category;
import xa.sh.ecom.ecom.product.models.Product;
import xa.sh.ecom.ecom.product.repo.CategoryRepo;
import xa.sh.ecom.ecom.product.repo.ProductRepo;
import xa.sh.ecom.ecom.security.SecurityUtils;
import xa.sh.ecom.ecom.seller.models.Seller;
import xa.sh.ecom.ecom.seller.repo.SellerRepo;

@Service
public class ProductServiceImpl {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private SellerRepo sellerRepo;

    @Autowired
    public ProductServiceImpl(ProductRepo productRepo, CategoryRepo categoryRepo, SellerRepo sellerRepo){
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
        this.sellerRepo = sellerRepo;
    }


    @Transactional
    public Product createProduct(ProductRequestDto prodDTO) throws Exception, ResourceNotFoundException{
            
            Long sellerId = SecurityUtils.getAuthenticatedUserId();
            Optional<Seller> seller = sellerRepo.findById(sellerId);
            if (seller.get()==null) {
                throw new ResourceNotFoundException("Seller Not Found");
            }

            Optional<Category> category = categoryRepo.findById(prodDTO.getCategory().getId());
            Category newCategory = new Category();
            if (category.get()== null) {
                
               newCategory = categoryRepo.save(new Category(prodDTO.getCategory().getId(), prodDTO.getCategory().getName(), prodDTO.getDescription()));

            }
            else {
                newCategory = category.get();
            }

            Product product = new Product();
            product.setName(prodDTO.getName());
            product.setCategory(newCategory);
            product.setPrice(prodDTO.getPrice());
            product.setDescription(prodDTO.getDescription());
            product.setSeller(seller.get());
            product.setStock(prodDTO.getStock());
            product.setImages(prodDTO.getImages());

            return productRepo.save(product);

    }

    public Page<Product> searchProducts(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepo.searchByNameOrDescription(query, pageable);
    }

    public Page<Product> getProductsBySeller (Long sellerId, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return productRepo.findBySellerId(sellerId, pageable);
    }

    public Page<Product> getProductsByCategory(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepo.findByCategoryId(categoryId, pageable);

    }

    public Page<Product> getAllProducts(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return productRepo.findAll(pageable);
    }




    public Product getProduct(Long productId) throws ResourceNotFoundException{
        Optional<Product> product = productRepo.findById(productId);
        if (product.isEmpty()) {
            throw new ResourceNotFoundException("Product not found");
        }
        return product.get();

    }

    public Product updateProduct(ProductRequestDto prodDto) throws ResourceNotFoundException, AccessDeniedException{


        Optional<Product> product = productRepo.findById(prodDto.getId());

        SecurityUtils.checkCurrentUserMatchesId(product.get().getSeller().getId());

        if (product.isEmpty()) {
            throw new ResourceNotFoundException("product not found");
        }
        Product newProduct = product.get();
    //     private Long id;
    // private String name ;
    // private Category category;
    // private String description;
    // private Double price;
    // private Integer stock ;
    // private Seller seller;
    // private ArrayList<ProductImage> images;
        if (prodDto.getName()!=null) {
            newProduct.setName(prodDto.getName());
        }
        if (prodDto.getCategory()!=null) {
            newProduct.setCategory(prodDto.getCategory());
        }
        if (prodDto.getDescription()!=null) {
            newProduct.setDescription(prodDto.getDescription());
        }
        if (prodDto.getPrice()!=null) {
            newProduct.setPrice(prodDto.getPrice());
        }
        if (prodDto.getStock()!=null) {
            newProduct.setStock(prodDto.getStock());
        }
        if (prodDto.getImages()!=null) {
            newProduct.setImages(prodDto.getImages());
        }

        return productRepo.save(newProduct);
    }

    public Product updateStock(Long productId, int quantityChange) throws ResourceNotFoundException, InsufficientStockException, AccessDeniedException {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        SecurityUtils.checkCurrentUserMatchesId(product.getSeller().getId());

        int newStock = product.getStock() + quantityChange;
        if (newStock < 0) {
            throw new InsufficientStockException("Not enough stock available");
        }

        product.setStock(newStock);
        return productRepo.save(product);
    }

    public void deleteProduct(Long productId) throws ResourceNotFoundException, AccessDeniedException {

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        
        SecurityUtils.checkCurrentUserMatchesId(product.getSeller().getId());
        // .orElseThrow(()-> new ResourceNotFoundException("Product not found"));

        productRepo.delete(product);
    }


    public int getProductStock(Long productId){
        Optional<Product> product  = productRepo.findById(productId);
        return product.get().getStock();
    }
}

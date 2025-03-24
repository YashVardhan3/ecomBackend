package xa.sh.ecom.ecom.product.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xa.sh.ecom.ecom.exception.InsufficientStockException;
import xa.sh.ecom.ecom.exception.ResourceNotFoundException;
import xa.sh.ecom.ecom.product.models.Category;
import xa.sh.ecom.ecom.product.models.Product;
import xa.sh.ecom.ecom.product.repo.CategoryRepo;
import xa.sh.ecom.ecom.product.repo.ProductRepo;
import xa.sh.ecom.ecom.seller.models.Seller;
import xa.sh.ecom.ecom.seller.repo.SellerRepo;

@Service
public class ProductServiceImpl {

    @Autowired
    private ProductRepo productRepository;

    @Autowired
    private CategoryRepo categoryRepository;

    @Autowired
    private SellerRepo sellerRepository;

    public Product createProduct(ProductRequestDto prodDTO, Long sellerId) throws Exception{
            Optional<Seller> seller = sellerRepository.findById(sellerId);
            if (seller.get()==null) {
                throw new Exception("Seller not found ");
            }

            Optional<Category> category = categoryRepository.findById(prodDTO.getCategory().getId());
            Category newCategory = new Category();
            if (category.get()== null) {
                
               newCategory = categoryRepository.save(new Category(prodDTO.getCategory().getId(), prodDTO.getCategory().getName(), prodDTO.getDescription()));

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

            return productRepository.save(product);

    }

    // public Page<Product> searchProducts(String query, Pageable pageable) {
    //     return productRepository.searchByNameOrDescription(query, pageable);
    // }

    public Product updateStock(Long productId, int quantityChange) throws ResourceNotFoundException, InsufficientStockException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        int newStock = product.getStock() + quantityChange;
        if (newStock < 0) {
            throw new InsufficientStockException("Not enough stock available");
        }

        product.setStock(newStock);
        return productRepository.save(product);
    }

    public void deleteProduct(Long productId, Long sellerId) throws ResourceNotFoundException {
        Product product = productRepository.findByIdAndSellerId(productId, sellerId);
        // .orElseThrow(()-> new ResourceNotFoundException("Product not found"));

        productRepository.delete(product);
    }
}

package xa.sh.ecom.ecom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import xa.sh.ecom.ecom.exception.AccessDeniedException;
import xa.sh.ecom.ecom.exception.InsufficientStockException;
import xa.sh.ecom.ecom.exception.ResourceNotFoundException;
import xa.sh.ecom.ecom.product.models.Product;
import xa.sh.ecom.ecom.product.service.impl.ProductRequestDto;
import xa.sh.ecom.ecom.product.service.impl.ProductServiceImpl;




@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private ProductServiceImpl prodSer;

    @PostMapping(value ="/create",consumes = {"multipart/form-data"})
    public ResponseEntity<Product> createProduct (@ModelAttribute ProductRequestDto prodDto) throws Exception, ResourceNotFoundException {
        Product product = prodSer.createProduct(prodDto);

        return ResponseEntity.ok().body(product);
    }

    @GetMapping("/get")
    public ResponseEntity<Page<Product>> getAllProduct(@RequestParam (defaultValue = "0") int page,@RequestParam (defaultValue = "10") int size) {
        Page<Product> prodList = prodSer.getAllProducts(page, size);
        return ResponseEntity.ok().body(prodList);
    }
    

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> findProducts(
            // Make query and categoryId optional using required=false
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long categoryId,
            // Keep pagination parameters with defaults
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Product> productPage; // Use a more descriptive variable name

        // Use Spring's StringUtils.hasText() to check for non-null and non-empty/whitespace query
        boolean hasQuery = StringUtils.hasText(query);
        boolean hasCategoryId = categoryId != null; // Correct null check

        if (hasQuery && hasCategoryId) {
            // --- Decide how to handle both query and categoryId ---
            // Option 1: Search within the specific category (Requires a new service/repo method)
            // productPage = prodSer.searchProductsInCategory(query, categoryId, page, size);

            // Option 2: Prioritize one (e.g., prioritize search query)
             productPage = prodSer.searchProducts(query, page, size);

            // Option 3: Return an error (e.g., Bad Request)
            // return ResponseEntity.badRequest().body("Cannot specify both query and categoryId");

            // For this example, let's assume you want to search within the category
            // You would need to implement searchProductsInCategory in your service/repository
            // If you don't have this method yet, choose Option 2 or 3 for now.
             // Let's assume for now we prioritize query if both are present:
             productPage = prodSer.searchProducts(query, page, size);


        } else if (hasQuery) {
            // Only query is present
            productPage = prodSer.searchProducts(query, page, size);
        } else if (hasCategoryId) {
            // Only categoryId is present
            productPage = prodSer.getProductsByCategory(categoryId, page, size);
        } else {
            // Neither query nor categoryId is present, return all products paginated
            // Assuming you have a method like getAllProducts in your service
            productPage = prodSer.getAllProducts(page, size);
        }

        // productPage is guaranteed to be non-null here if your service methods don't return null
        return ResponseEntity.ok(productPage);
    }
    
    @PostMapping("/update")
    public ResponseEntity<Product> updateProduct (@ModelAttribute ProductRequestDto prodDTO) throws ResourceNotFoundException, AccessDeniedException{
        Product product = prodSer.updateProduct(prodDTO);
        
        return ResponseEntity.ok().body(product);
    }
    
    @PatchMapping("/stockUpdate")
    public ResponseEntity<Product> updateStock(@RequestParam Long Id, @RequestParam int quantity) throws ResourceNotFoundException, InsufficientStockException, AccessDeniedException{
        Product product = prodSer.updateStock(Id, quantity);
        return ResponseEntity.ok().body(product);
    }
    
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProduct(@RequestParam Long id) throws ResourceNotFoundException, AccessDeniedException{
        prodSer.deleteProduct(id);
        return ResponseEntity.ok().body("Product Deleted");
    }
}

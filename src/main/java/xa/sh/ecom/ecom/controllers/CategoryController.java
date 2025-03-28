package xa.sh.ecom.ecom.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import xa.sh.ecom.ecom.exception.ResourceNotFoundException;
import xa.sh.ecom.ecom.product.models.Category;
import xa.sh.ecom.ecom.product.service.impl.CategoryRequestDto;
import xa.sh.ecom.ecom.product.service.impl.CategoryServiceImpl;


@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryServiceImpl catSer;


    @PreAuthorize("ADMIN")
    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(CategoryRequestDto catDTO) throws Exception{
        Category category  = catSer.createCategory(catDTO);
        return ResponseEntity.ok(category);
    }
    
    @PostMapping("/update")
    public ResponseEntity<Category> updateCategory(CategoryRequestDto catDTO) throws ResourceNotFoundException{
        Category category  = catSer.updateCategory(catDTO);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAllCategory() throws ResourceNotFoundException{
        return ResponseEntity.ok(catSer.getAllCategories());
    }

}

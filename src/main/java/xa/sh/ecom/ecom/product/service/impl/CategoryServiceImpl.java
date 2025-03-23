package xa.sh.ecom.ecom.product.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xa.sh.ecom.ecom.controllers.AuthController;
import xa.sh.ecom.ecom.exception.ResourceNotFoundException;
import xa.sh.ecom.ecom.product.models.Category;
import xa.sh.ecom.ecom.product.repo.CategoryRepo;

@Service
public class CategoryServiceImpl {

    private final AuthController authController;


    @Autowired
    private CategoryRepo catRepo;

    CategoryServiceImpl(AuthController authController) {
        this.authController = authController;
    }

    public Category createCategory (CategoryRequestDto cateDTO) throws Exception{
        Category category = new Category();
        Category nCategory = catRepo.findByName(cateDTO.getName());
        if (nCategory!=null) {
            throw new Exception("category already exists by the name");
        }
        category.setName(cateDTO.getName());
        category.setDescription(cateDTO.getDescription());
         return catRepo.save(category);
        

    }

    public Category updateCategory (CategoryRequestDto cateDTO) throws ResourceNotFoundException{
        //Category category = new Category();
        Optional<Category> category = catRepo.findById(cateDTO.getId());
        if (category.isEmpty()) {
            throw new ResourceNotFoundException("Category not found");
        }
        Category nCategory = category.get();
        nCategory.setName(cateDTO.getName());
        nCategory.setDescription(cateDTO.getDescription());
        return catRepo.save(nCategory);


    }

    public List<Category> getAllCategories (){
        return catRepo.findAll();
    }

    public void deleteCategory (Long categoryId){
        catRepo.deleteById(categoryId);
    }

}

package com.simran.ecom_proj.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.simran.ecom_proj.Model.Product;
import com.simran.ecom_proj.Repository.ProductRepo;

@Service
public class ProductService {

    @Autowired
    private ProductRepo repo;

    public List<Product> getAllProducts(){
        return repo.findAll();
    }

    public Product getProductById(int id){
        return repo.findById(id).orElse(null);
    }

    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
        product.setImageName((imageFile.getOriginalFilename()));
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        return repo.save(product);
    }

    public Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException {
    if (imageFile != null) {
        product.setImageData(imageFile.getBytes());
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
    } else {
        // Retain existing image data if no new image is uploaded
        Product existing = repo.findById(id).orElse(null);
        if (existing != null) {
            product.setImageData(existing.getImageData());
            product.setImageName(existing.getImageName());
            product.setImageType(existing.getImageType());
        }
    }
    return repo.save(product);
}

    public void deleteProduct(int id) {
       repo.deleteById(id);
    }

    public List<Product> searchProducts(String keyword) {
       return repo.searchProducts(keyword);
    }

}

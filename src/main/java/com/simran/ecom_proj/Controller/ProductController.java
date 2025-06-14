package com.simran.ecom_proj.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.simran.ecom_proj.Model.Product;
import com.simran.ecom_proj.Service.ProductService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService service;

    @RequestMapping("/")
    public String greet(){
        return "Hello, World!";
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(){
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProduct(@PathVariable int id){

        Product product = service.getProductById(id);

        if(product != null){return new ResponseEntity<>(product, HttpStatus.OK);}
        else
        {
            return new ResponseEntity<>("Product not found",HttpStatus.NOT_FOUND);
        }
        
    }

    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile){
        try{
            Product product1 = service.addProduct(product, imageFile);
            return new ResponseEntity<>(product1, HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products/{productId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId){
        Product product = service.getProductById(productId);
        byte[] imageData = product.getImageData();
        return ResponseEntity.ok()
                .header("Content-Type", product.getImageType() != null ? product.getImageType() : "image/jpeg")
                .body(imageData);
    }  
    
    @PutMapping("/products/{id}")
    public ResponseEntity<Map<String, String>> updateProduct(
    @PathVariable int id,
    @RequestPart Product product,
    @RequestPart(required = false) MultipartFile imageFile) throws IOException {
    Product product1 = service.updateProduct(id, product, imageFile);
    if(product1 != null){
        return new ResponseEntity<>(Map.of("message", "Updated"), HttpStatus.OK);
    } else {
        return new ResponseEntity<>(Map.of("message", "Failed to update"), HttpStatus.BAD_REQUEST);
    }
}
      
    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id){
        Product product = service.getProductById(id);
        if(product != null){
            service.deleteProduct(id);
            return new ResponseEntity<Map<String, String>>(Map.of("message", "Deleted"), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<Map<String, String>>(Map.of("message", "Product not found"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/product/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword){
        System.out.println("searching with" + keyword);
        List<Product> products = service.searchProducts(keyword);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
   
}




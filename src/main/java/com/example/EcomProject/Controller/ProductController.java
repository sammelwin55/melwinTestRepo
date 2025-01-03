package com.example.EcomProject.Controller;

import com.example.EcomProject.Model.Product;
import com.example.EcomProject.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/product")
    public ResponseEntity<List<Product>>  getAllProduct(){
        return new ResponseEntity<>(productService.getAllProduct(), HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product>  getProductById(@PathVariable int prodId){
        if (productService.getProductById(prodId)!=null)
        return new ResponseEntity<> (productService.getProductById(prodId), HttpStatus.OK);

        else
            return new ResponseEntity<> ( HttpStatus.NOT_FOUND);
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile) throws IOException {

        Product product1 = productService.addProduct(product,imageFile);
        try{
            return new ResponseEntity<>(product1,HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/product/{prodId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int prodId){

        Product product = productService.getProductById(prodId);
        byte[] imageFile = product.getImageData();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(product.getImageType()))
                .body(imageFile);

    }


    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id,@RequestPart Product product,
                                                @RequestPart MultipartFile imageFile)
    {
        Product product1 = null;
        try {
            product1 = productService.updateProduct(id,product,imageFile);
        } catch (IOException e) {
           // throw new RuntimeException(e);
            return new ResponseEntity<>("error occurs while updating",HttpStatus.BAD_REQUEST);
        }
        if(product1 != null){
            return new ResponseEntity<>("updated",HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("error occurs while updating",HttpStatus.BAD_REQUEST);
        }
      }


      @DeleteMapping("/product/{prodId}")
      public ResponseEntity<String> deleteProduct(@PathVariable int prodId){
        Product product = productService.getProductById(prodId);
        if(product != null){
            productService.deleteProductById(prodId);
            return new ResponseEntity<>("product is deleted ",HttpStatus.OK);
        }
          return new ResponseEntity<>("product is not found ",HttpStatus.NOT_FOUND);
      }

      @GetMapping("/product/search/")
      public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword){
        System.out.println("searching with keyword....");
        List<Product> product = productService.searchProduct(keyword);
        return new ResponseEntity<>(product,HttpStatus.OK);

      }

}

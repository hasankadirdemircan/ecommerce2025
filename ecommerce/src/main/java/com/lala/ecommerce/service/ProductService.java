package com.lala.ecommerce.service;

import com.lala.ecommerce.exception.ProductNotFoundException;
import com.lala.ecommerce.model.Product;
import com.lala.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final String UPLOAD_DIR = "uploads";

    private final ProductRepository productRepository;

    public Product createProduct(MultipartFile file, Product product) {
        if(Objects.nonNull(file)) {
            String imagePath = saveFile(file, product.getName());
            product.setImage(imagePath);
        }else {
            Product existProduct = productRepository.findById(product.getId()).orElseThrow(() -> new ProductNotFoundException("Product not found, id : " + product.getId()));
            product.setImage(existProduct.getImage());
        }
        return productRepository.save(product);
    }

    private String saveFile(MultipartFile file, String productName) {
        productName = productName.replaceAll("\\s", "");
        String fileName = productName + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
        Path uploadPath = Path.of(UPLOAD_DIR);
        Path filePath;
        try{
            Files.createDirectories(uploadPath);
            filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return filePath.toString();
    }
}

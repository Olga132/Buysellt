package com.example.buysell.services;

import com.example.buysell.models.Image;
import com.example.buysell.models.Product;
import com.example.buysell.models.User;
import com.example.buysell.repositories.ProductRepository;
import com.example.buysell.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final MailSenderService mailSender;

    // получение всех товаров
    public List<Product> listProducts(String title, String city) {

        if (title != null && !city.equals("")) {
            List<Product> list = productRepository.findByTitleContainingIgnoreCase(title);
            List<Product> searchList = new ArrayList<>();
            for (Product p : list) {
                if (p.getCity().equals(city)) {
                    searchList.add(p);
                }
            }
            return searchList;

        } else if (city != null && title.equals("")) {
            return productRepository.findByCity(city);

        } else if (title != null && city.equals("")) {
            return productRepository.findByTitleContainingIgnoreCase(title);
        }
        return productRepository.findAll();
    }

    // создание товара
    public void saveProduct(Principal principal, Product product, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        product.setUser(getUserByPrincipal(principal));
        Image image1;
        Image image2;
        Image image3;
        if (file1.getSize() != 0) {
            image1 = createImage(file1);
            image1.setPreviewImage(true);
            product.addImageToProduct(image1);
        }
        if (file2.getSize() != 0) {
            image2 = createImage(file2);
            product.addImageToProduct(image2);
        }
        if (file3.getSize() != 0) {
            image3 = createImage(file3);
            product.addImageToProduct(image3);
        }
        log.info("Saving new Product. Title: {}; Author email: {}", product.getTitle(), product.getUser().getEmail());
        Product productFromDb = productRepository.save(product);
        productFromDb.setPreviewImageId(productFromDb.getImages().get(0).getId());
        productRepository.save(product);
    }

    // получение пользователя из контекста безопасности
    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }

    // добавление картинки
    private Image createImage(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    // удаление товара
    public void deleteProduct(User user, Long id) {
        productRepository.deleteById(id);
        Product product = getProductById(id);
        if (product != null) {
            if (product.getUser().getId().equals(user.getId())) {

                log.info("Product with id = {} was deleted", id);
            } else {
                log.error("User: {} haven't this product with id = {}", user.getEmail(), id);
            }
        } else {
            log.error("Product with id = {} is not found", id);
        }
    }

    // получение продукта по id
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    // отправка сообщения продавцу товара
    public void messageToSellerProduct(Long id, String nameFrom, String phoneNumber, String email, String message){
        Product product = getProductById(id);
        User userTo = product.getUser();
        String draft = String.format("Здравствуйте, %s! \n" +
                "Вам пришло сообщение от пользователя сервиса BuySell.\n" +
                "Тема: %s \n" + "От кого: %s, телефон %s, почта %s.\n"+
                "Сообщение: %s.", userTo.getName(), product.getTitle(), nameFrom, phoneNumber, email, message);
        mailSender.sendEmail(userTo.getEmail(),"Сообщение BuySell", draft);
    }

}

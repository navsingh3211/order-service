package com.ecommerce.order.services;

import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.models.CartItem;
import com.ecommerce.order.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductProviderWebClient productProviderWebClient;
    public boolean addToCart(Long userId, CartItemRequest request) {
//        Optional<Product> productOpt = productRepository.findById(request.getProductId());
//        if(productOpt.isEmpty())
//            return false;
//        Product product = productOpt.get();
//        if(product.getStockQuantity()<request.getQuantity())
//            return false;
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//        if(userOpt.isEmpty())
//            return false;
//        User user = userOpt.get();

        //Validating product by id using webclient from product microservice
        System.out.println(productProviderWebClient.getProductById(request.getProductId()).block()+" myresponse");
        boolean isProductExist = Boolean.TRUE.equals(productProviderWebClient.getProductById(request.getProductId()).block());
        if(!isProductExist){
            return false;
        }
        //--end

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId,request.getProductId());
        if(existingCartItem!=null){
            //Update the quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(BigDecimal.valueOf(100.00));
            cartItemRepository.save(existingCartItem);
        }else{
            //Create anew cart item
            CartItem cartItem = new CartItem();
            cartItem.setProductId(Long.valueOf(request.getProductId()));
            cartItem.setUserId(Long.valueOf(userId));
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(100.00));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public boolean deleteItemFromCart(Long userId, Long productId) {
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);
        if(cartItem!=null){
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }

    public List<CartItem> getUserCart(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}

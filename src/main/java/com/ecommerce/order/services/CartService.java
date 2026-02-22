package com.ecommerce.order.services;

import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.dto.ProductResponse;
import com.ecommerce.order.dto.UserResponse;
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
    private final UserProviderWebClient userProviderWebClient;
    public boolean addToCart(String userId, CartItemRequest request) {

//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//        if(userOpt.isEmpty())
//            return false;
//        User user = userOpt.get();

        //Validating user by id using http interface client webclient from user microservice
        UserResponse userResponse = userProviderWebClient.getUserById(userId);
        System.out.println(userResponse+" userData");
        if(userResponse == null){
            return false;
        }
        //-- end

        //Validating product by id using http interface client webclient from product microservice
        ProductResponse product = productProviderWebClient.getProductById(request.getProductId());
        System.out.println(product+" product");
        if(product == null){
            return false;
        }
        if(product.getStockQuantity()<request.getQuantity())
          return false;
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
            cartItem.setUserId(userId);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(100.00));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public boolean deleteItemFromCart(String userId, Long productId) {
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);
        if(cartItem!=null){
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }

    public List<CartItem> getUserCart(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}

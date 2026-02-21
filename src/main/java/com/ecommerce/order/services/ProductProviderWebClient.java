package com.ecommerce.order.services;


import com.ecommerce.order.dto.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

//@Service
//@RequiredArgsConstructor
//public class ProductProviderWebClient {
//    private final WebClient webClient;
//
//    public Mono<Boolean> getProductById(Long id){
//        return webClient.get()
//                .uri("/api/products/{id}",id)
//                .retrieve()
//                .toBodilessEntity()
//                .map(response -> response.getStatusCode().is2xxSuccessful())
//                .onErrorResume(ex -> Mono.just(false));
//    }
//}
@HttpExchange("/api/products")
public interface ProductProviderWebClient {
    @GetExchange("/{id}")
    ProductResponse getProductById(@PathVariable Long id);
}

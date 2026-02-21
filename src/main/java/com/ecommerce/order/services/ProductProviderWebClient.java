package com.ecommerce.order.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductProviderWebClient {
    private final WebClient webClient;

    public Mono<Boolean> getProductById(Long id){
        return webClient.get()
                .uri("/api/products/{id}",id)
                .retrieve()
                .toBodilessEntity()
                .map(response -> response.getStatusCode().is2xxSuccessful())
                .onErrorResume(ex -> Mono.just(false));
    }
}

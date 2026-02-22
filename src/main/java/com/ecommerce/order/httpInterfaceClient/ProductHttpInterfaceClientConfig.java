package com.ecommerce.order.httpInterfaceClient;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

@Configuration
public class ProductHttpInterfaceClientConfig {

    @Bean
    public ProductProviderWebClient webClientHttpInterface(WebClient.Builder webClientBuilder){
        WebClient webClient = webClientBuilder.baseUrl("http://product-service")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError,response -> Mono.empty() )
                .build();

        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter).build();

        ProductProviderWebClient service = factory.createClient(ProductProviderWebClient.class);
        return service;
    }
//    @Bean
//    @LoadBalanced
//    public WebClient.Builder loadBalancedWebClient(){
//        return WebClient.builder();
//    }
//    @Bean
//    public WebClient webClient(WebClient.Builder builder){
//        return builder.baseUrl("http://product-service").build();
//    }
}

package com.ecommerce.order.httpInterfaceClientconfig;

import com.ecommerce.order.services.UserProviderWebClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

@Configuration
public class UserHttpInterfaceClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientLoadBalancer(){
        return WebClient.builder();
    }

    @Bean
    public UserProviderWebClient webClientHttpUserInterface(WebClient.Builder webClientBuilder){
        WebClient webClient = webClientBuilder.baseUrl("http://userservice")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, response -> Mono.empty() )
                .build();

        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter).build();

        UserProviderWebClient service = factory.createClient(UserProviderWebClient.class);
        return service;
    }
}

package com.ecommerce.order.httpInterfaceClient;

import com.ecommerce.order.dto.UserResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/api/users")
public interface UserProviderWebClient {
    @GetExchange("/{id}")
    UserResponse getUserById(@PathVariable String id);
}

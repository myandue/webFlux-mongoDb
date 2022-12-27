package com.example.webFluxmongoDb.repository;

import com.example.webFluxmongoDb.domain.Cart;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CartReactiveRepository extends ReactiveCrudRepository<Cart, String> {
}

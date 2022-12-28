package com.example.webFluxmongoDb.service;

import com.example.webFluxmongoDb.domain.Cart;
import com.example.webFluxmongoDb.domain.Item;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CartService {

    public Flux<Item> itemSearchName(String name, String description, boolean isSuit);
    public Mono<Cart> delToCartCount(String cartId, String itemId);
    public Mono<Cart> delToCartAll(String cartId, String itemId);
    public Mono<Cart> addToCart(String cartId, String itemId);
}

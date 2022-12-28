package com.example.webFluxmongoDb.controller;

import com.example.webFluxmongoDb.domain.Cart;
import com.example.webFluxmongoDb.repository.CartReactiveRepository;
import com.example.webFluxmongoDb.repository.ItemReactiveRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
public class BaseController {

    private ItemReactiveRepository itemReactiveRepository;
    private CartReactiveRepository cartReactiveRepository;

    @GetMapping("/")
    Mono<Rendering> base(){
        return Mono.just(Rendering.view("base.html")
                .modelAttribute("items", itemReactiveRepository.findAll())
                .modelAttribute("cart", cartReactiveRepository.findById("My Cart")
                        .defaultIfEmpty(new Cart("My Cart")))
                .build());
    }
}

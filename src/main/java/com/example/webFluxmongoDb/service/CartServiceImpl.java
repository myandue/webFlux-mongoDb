package com.example.webFluxmongoDb.service;

import com.example.webFluxmongoDb.domain.Cart;
import com.example.webFluxmongoDb.domain.Item;
import com.example.webFluxmongoDb.domain.vo.CartItem;
import com.example.webFluxmongoDb.repository.CartReactiveRepository;
import com.example.webFluxmongoDb.repository.ItemReactiveRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final ItemReactiveRepository itemReactiveRepository;
    private final CartReactiveRepository cartReactiveRepository;

    //itemSearchName 메서드는 ReactiveQueryByExampleExecutor 기능 사용한듯?
    @Override
    public Flux<Item> itemSearchName(String name, String description, boolean isSuit) {
        //isSuit가 F일때, name 또는 description이 null로 들어오는 것이 가능하다.
        //둘다 null일 경우 전체 검색이 되고, 하나만 null일 경우 나머지 하나의 조건으로 검색.

        Item item = new Item(name, description, 0.0);

        //isSuit: T->정확히 매칭, F->name을 포함하고 description을 포함하는 것 매칭
        //withIgnorePaths: 에 적힌 값(price)은 제외하고
        ExampleMatcher matcher = (isSuit
                ? ExampleMatcher.matchingAll().withIgnorePaths("price")
                : ExampleMatcher.matching()
                .withMatcher("name", contains())
                .withMatcher("description", contains())
                .withIgnorePaths("price"));

        //matcher와 매칭되는 item을 찾아서 리스트로 리턴?
        Example<Item> itemExample = Example.of(item, matcher);
        return itemReactiveRepository.findAll(itemExample);
    }

    @Override
    public Mono<Cart> delToCartCount(String cartId, String itemId) {
        return cartReactiveRepository.findById(cartId)
                //내 cart가 없을 경우 새로 생성
                .defaultIfEmpty(new Cart(cartId))
                //flatMap은 들어온 값을 다르게 바꿔줌..?
                .flatMap(cart ->
                        //내 카트에서 item list를 가져와서 줄세움
                        cart.getCartItemList().stream()
                                //item list의 cart item들 중에서 내가 가져온 item id와 같은 것을 어쨌든 찾음
                                //찾는것이 없을 경우 밑의 orElseGet을 이용해 빈통을 return 해줌
                                .filter(cartItem -> cartItem.getItem().getId().equals(itemId)).findAny()
                                //찾은 cart item이 하나일 경우 아예 cart에서 제거를 하고, 아닐경우 개수를 줄임
                                .map(cartItem -> {
                                    if(cartItem.isOne()){
                                        cart.removeItem(cartItem);
                                    }else{
                                        cartItem.decrement();
                                    }
                                    //그리고 해당 cart를 돌려줌
                                    return Mono.just(cart);
                                }).orElseGet(()->{
                                    return Mono.empty();
                                })
                //빈통이건 수정된 cart건, 돌려받은 cart를 repository에 저장함
                ).flatMap(cart -> cartReactiveRepository.save(cart));
    }

    @Override
    public Mono<Cart> delToCartAll(String cartId, String itemId) {
        return cartReactiveRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .flatMap(
                        cart -> cart.getCartItemList().stream()
                                .filter(cartItem -> cartItem.getItem().getId().equals(itemId)).findAny()
                                .map(cartItem -> {
                                    cart.removeItem(cartItem);
                                    return Mono.just(cart);
                                }).orElseGet(()->{
                                    return Mono.empty();
                                })
                ).flatMap(cart -> cartReactiveRepository.save(cart));
    }

    @Override
    public Mono<Cart> addToCart(String cartId, String itemId) {
        return cartReactiveRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .flatMap(
                        cart -> cart.getCartItemList().stream()
                                .filter(cartItem -> cartItem.getItem().getId().equals(itemId)).findAny()
                                .map(cartItem -> {
                                    cartItem.increment();
                                    return Mono.just(cart);
                                }).orElseGet(() -> itemReactiveRepository.findById(itemId)
                                        //id로 찾아낸 결과인 item을 갖고 CartItem을 새로 생성
                                        .map(item -> new CartItem(item))
                                        //생성 후, 기존 내 cart list에 생성한 cartItem을 집어넣음
                                        .doOnNext(cartItem -> cart.getCartItemList().add(cartItem))
                                        //하기 코드는 cartItem을 사용하던 function에서 cart를 사용하는 function으로 전환해준듯?
                                        .map(cartItem -> cart))
                ).flatMap(cart -> cartReactiveRepository.save(cart));
    }
}

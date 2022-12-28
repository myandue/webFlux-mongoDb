package com.example.webFluxmongoDb.service;

import com.example.webFluxmongoDb.domain.Item;
import com.example.webFluxmongoDb.repository.ItemReactiveRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServiceTest {

    @Autowired
    CartService cartService;
    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    public Long itemCnt;

    @BeforeEach
    void setUp(){
        //하기 코드(StepVerifier.create)의 기능을 통해 비동기 테스트 진행
        StepVerifier.create(
                itemReactiveRepository.deleteAll()
        ).verifyComplete();

        Item item1 = new Item("lego", "made in usa", 20.00);
        Item item2 = new Item("lego", "made in china", 10.00);
        Item item3 = new Item("rc car", "made in usa", 40.00);
        Item item4 = new Item("rc car", "made in china", 20.00);
        Item item5 = new Item("rc car", "made in india", 15.00);
        Item item6 = new Item("drone", "made in korea", 100.00);
        List<Item> itemList = Arrays.asList(item1,item2,item3,item4,item5,item6);

        itemCnt = Long.valueOf(itemList.size());
        StepVerifier.create(
                itemReactiveRepository.saveAll(itemList)
        ).expectNextMatches(item -> {
            System.out.println(item.toString());
            return true;
        }).expectNextCount(itemCnt-1).verifyComplete();
        //expectNextCount: 예상되는 개수 -> 테스트 결과 수치로 확인하는 방법
        //next를 몇번 호출하냐이기때문에 '예상개수-1'
    }

    @Test
    public void itemSearchNameTrue(){
        StepVerifier.create(
                cartService.itemSearchName("drone", "made in korea", true)
        ).expectNextMatches(item -> {
            assertThat(item.getId()).isNotNull();
            assertThat(item.getName()).isEqualTo("drone");
            assertThat(item.getDescription()).isEqualTo("made in korea");
            assertThat(item.getPrice()).isEqualTo(100.00);
            return true;
        }).verifyComplete();
    }

    @Test
    public void itemSearchNameFalse(){
        StepVerifier.create(
                cartService.itemSearchName(null, null, false).count()
        ).expectNextMatches(cnt -> {
            assertThat(cnt).isEqualTo(6);
            return true;
        }).verifyComplete();
    }
}

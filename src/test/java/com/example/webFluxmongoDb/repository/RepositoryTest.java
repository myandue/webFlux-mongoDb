package com.example.webFluxmongoDb.repository;

import com.example.webFluxmongoDb.domain.Item;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
public class RepositoryTest {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;
    @Autowired
    CartReactiveRepository cartReactiveRepository;

    public Long itemCnt;

    //테스트 전 초기화 작업
    @BeforeEach
    void setUp(){
        //하기 코드의 기능을 통해 비동기 테스트 진행
        StepVerifier.create(itemReactiveRepository.deleteAll()).verifyComplete();

        Item item1 = new Item("lego", "made in usa", 20.00);
        Item item2 = new Item("lego", "made in china", 10.00);
        Item item3 = new Item("rc car", "made in usa", 40.00);
        Item item4 = new Item("rc car", "made in china", 20.00);
        Item item5 = new Item("rc car", "made in india", 15.00);
        Item item6 = new Item("drone", "made in korea", 100.00);
        List<Item> itemList = Arrays.asList(item1,item2,item3,item4,item5,item6);
    }
}

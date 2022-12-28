package com.example.webFluxmongoDb.repository;

import com.example.webFluxmongoDb.domain.Item;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

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
    public void itemRepositoryCount(){
        StepVerifier.create(
                itemReactiveRepository.findAll().count()
        ).expectNextMatches(cnt -> {
            assertThat(cnt).isEqualTo(itemCnt);
            return true;
        }).verifyComplete();
    }

    @Test
    public void itemRepositoryCount2(){
        StepVerifier.create(
                itemReactiveRepository.findByNameContaining("rc").count()
        ).expectNextMatches(cnt -> {
            assertThat(cnt).isEqualTo(3);
            return true;
        }).verifyComplete();
    }
    @Test
    public void itemSearchName(){
        StepVerifier.create(
                itemReactiveRepository.findByNameContaining("rc")
        ).expectNextMatches(item -> {
            //log 찍히는게 이상한디,,
            System.out.println(item.toString());
            return true;
        }).expectNextCount(2).verifyComplete();
    }

    @Test
    public void itemSearchName2(){
        StepVerifier.create(
                itemReactiveRepository.findByName("drone")
        ).expectNextMatches(item -> {
            assertThat(item.getId()).isNotNull();
            assertThat(item.getName()).isEqualTo("drone");
            assertThat(item.getDescription()).isEqualTo("made in korea");
            assertThat(item.getPrice()).isEqualTo(100.00);
            return true;
        }).verifyComplete();
    }
}

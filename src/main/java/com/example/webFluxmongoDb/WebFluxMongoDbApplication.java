package com.example.webFluxmongoDb;

import com.example.webFluxmongoDb.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class WebFluxMongoDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebFluxMongoDbApplication.class, args);
	}

	@Autowired
	MongoOperations mongoOperations;
	//블록킹 방식으로 crud 할 수 있는 것..?

	//시작 되자마자 기본 데이터를 집어 넣어주는 일
	@EventListener(ApplicationReadyEvent.class)
	public void doSomething(){
		Item item1 = new Item("lego", "made in usa", 20.00);
		Item item2 = new Item("lego", "made in china", 10.00);
		Item item3 = new Item("rc car", "made in usa", 40.00);
		Item item4 = new Item("rc car", "made in china", 20.00);
		Item item5 = new Item("rc car", "made in india", 15.00);
		Item item6 = new Item("drone", "made in korea", 100.00);
		mongoOperations.save(item1);
		mongoOperations.save(item2);
		mongoOperations.save(item3);
		mongoOperations.save(item4);
		mongoOperations.save(item5);
		mongoOperations.save(item6);
	}
	//react mongodb 일 경우에 행이 걸리는 일이 발생할 수 있으므로 블로킹으로 값을 넣어준다..?
}

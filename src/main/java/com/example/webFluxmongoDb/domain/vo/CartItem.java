package com.example.webFluxmongoDb.domain.vo;

import com.example.webFluxmongoDb.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    private Item item;
    private int quantity;

    //처음 생성될 때 수량 1
    public CartItem(Item item){
        this.item=item;
        this.quantity=1;
    }

    public void increment(){
        this.quantity+=1;
    }

    public void decrement(){
        this.quantity-=1;
    }

    //한개 존재 하는지 여부 확인
    public boolean isOne(){
        if(this.quantity==1){
            return true;
        }else{
            return false;
        }
    }
}

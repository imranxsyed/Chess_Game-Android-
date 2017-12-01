package com.example.ptafo.chessgame42;

import java.io.Serializable;


public class Move implements Serializable {

    private static final long serialVersionUID = -6752796506062987749L;

    String fromIndex;
    String toIndex;
    String promotion;
    Move(String fromIndex, String toIndex){
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }
    public void setPromotion(String promo){
        promotion = promo;
    }


}

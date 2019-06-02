package com.ferrup.espresser.model;

import java.util.ArrayList;

public class CoffeeMachine {
    private int makingTime;

    private ArrayList<Integer> outputs = new ArrayList<>();

    public CoffeeMachine(int outputsCount, int makingTime) {
        for (int i = 0; i < outputsCount; i++) {
            outputs.add(0);
        }
        this.makingTime = makingTime;
    }
}

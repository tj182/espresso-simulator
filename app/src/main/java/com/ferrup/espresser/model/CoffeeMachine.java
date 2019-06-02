package com.ferrup.espresser.model;

import java.util.ArrayList;

public class CoffeeMachine {
    private int outputsCount;
    private int makingTime;

    private ArrayList<Integer> outputs = new ArrayList<>();

    public CoffeeMachine(int outputsCount, int makingTime) {
        this.outputsCount = outputsCount;
        this.makingTime = makingTime;
    }

    public void tick(int secs) {
        for (int i = 0; i < outputs.size(); i++) {
            outputs.set(i, outputs.get(i) + secs);
        }
    }

    public int popReadyCoffees() {
        ArrayList<Integer> readyCoffees = new ArrayList<>();
        for (Integer coffee : outputs) {
            if (coffee >= makingTime) {
                readyCoffees.add(coffee);
            }
        }
        for (Integer coffee : readyCoffees) {
            outputs.remove(coffee);
        }
        return readyCoffees.size();
    }

    public int getEmptyOutputs() {
        return outputsCount - outputs.size();
    }

    public void startMakingCoffee() {
        outputs.add(0);
    }
}

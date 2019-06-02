package com.ferrup.espresser.model;

public class Employee {
    private int chanceOfSuperBusy;
    private int periodOfSuperBusy;
    private int coffeeInterval;

    private boolean isSuperBusy = false;
    private int superBusyTime = 0;
    private int coffeeTime = 0;

    public Employee(int chanceOfSuperBusy, int periodOfSuperBusy, int coffeeInterval) {
        this.chanceOfSuperBusy = chanceOfSuperBusy;
        this.periodOfSuperBusy = periodOfSuperBusy;
        this.coffeeInterval = coffeeInterval;
    }
}

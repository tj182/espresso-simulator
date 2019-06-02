package com.ferrup.espresser.model;

import android.util.Log;

import java.util.ArrayList;

public class Data {
    public ArrayList<Employee> office = new ArrayList<>();
    public ArrayList<Employee> normalQueue = new ArrayList<>();
    public ArrayList<Employee> superBusyQueue = new ArrayList<>();
    public CoffeeMachine coffeeMachine;

    public Data(AppSettings appSettings) {
        for (int i = 0; i < appSettings.getEmployeesCount(); i++) {
            office.add(new Employee(
                    appSettings.getChanceOfSuperbusyness(),
                    appSettings.getPeriodOfSuperbusyness(),
                    appSettings.getCoffeeInterval()));
        }
        coffeeMachine = new CoffeeMachine(appSettings.getOutputsCount(), appSettings.getMakingTime());
    }
}

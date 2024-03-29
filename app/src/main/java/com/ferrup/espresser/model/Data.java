package com.ferrup.espresser.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Data {
    public AppSettings appSettings;
    public ArrayList<Employee> office = new ArrayList<>();
    public ArrayList<Employee> normalQueue = new ArrayList<>();
    public ArrayList<Employee> superBusyQueue = new ArrayList<>();
    public CoffeeMachine coffeeMachine;
    public long date = System.currentTimeMillis();

    public Data(AppSettings appSettings) {
        this.appSettings = appSettings;
        for (int i = 0; i < appSettings.getEmployeesCount(); i++) {
            office.add(new Employee(
                    appSettings.getChanceOfSuperbusyness(),
                    appSettings.getPeriodOfSuperbusyness(),
                    appSettings.getCoffeeInterval(),
                    appSettings.getCoffeeIntervalError()));
        }
        coffeeMachine = new CoffeeMachine(appSettings.getOutputsCount(), appSettings.getMakingTime());
    }

    public void updateDate() {
        date = System.currentTimeMillis();
    }

    public int getSuperBusyCountInOffice() {
        int count = 0;
        for (Employee employee : office) {
            if (employee.isSuperBusy())
                count++;
        }
        return count;
    }

    public String formatDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(date));
    }
}

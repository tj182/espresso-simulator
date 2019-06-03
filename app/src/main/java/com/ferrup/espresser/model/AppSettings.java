package com.ferrup.espresser.model;

public class AppSettings {
    private int employeesCount;
    private int chanceOfSuperbusyness;
    private int periodOfSuperbusyness;
    private int coffeeInterval;
    private int coffeeIntervalError;
    private int makingTime;
    private int outputsCount;

    public int getEmployeesCount() {
        return employeesCount;
    }

    public void setEmployeesCount(int employeesCount) {
        this.employeesCount = employeesCount;
    }

    public int getChanceOfSuperbusyness() {
        return chanceOfSuperbusyness;
    }

    public void setChanceOfSuperbusyness(int chanceOfSuperbusyness) {
        this.chanceOfSuperbusyness = chanceOfSuperbusyness;
    }

    public int getPeriodOfSuperbusyness() {
        return periodOfSuperbusyness;
    }

    public void setPeriodOfSuperbusyness(int periodOfSuperbusyness) {
        this.periodOfSuperbusyness = periodOfSuperbusyness;
    }

    public int getCoffeeInterval() {
        return coffeeInterval;
    }

    public void setCoffeeInterval(int coffeeInterval) {
        this.coffeeInterval = coffeeInterval;
    }

    public int getCoffeeIntervalError() {
        return coffeeIntervalError;
    }

    public void setCoffeeIntervalError(int coffeeIntervalError) {
        this.coffeeIntervalError = coffeeIntervalError;
    }

    public int getMakingTime() {
        return makingTime;
    }

    public void setMakingTime(int makingTime) {
        this.makingTime = makingTime;
    }

    public int getOutputsCount() {
        return outputsCount;
    }

    public void setOutputsCount(int outputsCount) {
        this.outputsCount = outputsCount;
    }
}

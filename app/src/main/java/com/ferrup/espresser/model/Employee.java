package com.ferrup.espresser.model;

public class Employee {
    private float chanceOfSuperBusy;
    private int periodOfSuperBusy;
    private int coffeeInterval;

    private boolean superBusy = false;
    private int superBusyTime = 0;
    private int coffeeTime = 0;

    public Employee(int chanceOfSuperBusy /* % in 1 hour */, int periodOfSuperBusy /* hours */, int coffeeInterval /* mins */) {
        this.chanceOfSuperBusy = ((float) chanceOfSuperBusy) / 3600F / 100F;
        this.periodOfSuperBusy = periodOfSuperBusy * 3600;
        this.coffeeInterval = coffeeInterval * 60;
    }

    public boolean isSuperBusy() {
        return superBusy;
    }

    public void setSuperBusy(boolean value) {
        superBusy = value;
    }

    public void tick(int secs) {
        coffeeTime += secs;
        superBusyTime += secs;
    }

    public boolean isBecameSuperBusy(float random) {
        return chanceOfSuperBusy >= random;
    }

    public boolean inNotSuperBusyAnymore() {
        return superBusyTime >= periodOfSuperBusy;
    }

    public boolean isWantCoffee() {
        return coffeeTime >= coffeeInterval;
    }

    public void resetCoffeeTime() {
        coffeeTime = 0;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Employee)) return false;
        Employee this2 = (Employee) object;
        return this.chanceOfSuperBusy == this2.chanceOfSuperBusy &&
                this.periodOfSuperBusy == this2.periodOfSuperBusy &&
                this.coffeeInterval == this2.coffeeInterval &&
                this.superBusy == this2.superBusy &&
                this.superBusyTime == this2.superBusyTime &&
                this.coffeeTime == this2.coffeeTime;
    }
}

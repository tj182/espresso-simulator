package com.ferrup.espresser;

import android.content.Context;
import android.util.Log;

import com.ferrup.espresser.model.AppSettings;
import com.ferrup.espresser.model.Data;
import com.ferrup.espresser.model.Employee;
import com.ferrup.espresser.utils.Prefs;

import java.util.ArrayList;
import java.util.Random;

public class Simulator {
    public static final String TAG = Simulator.class.getSimpleName();
    public static final int UPDATES_LIMIT = 100;
    public static final int SIMPLIFIER_BUFFER = 50;
    public static final Object sLock = new Object();

    private Context context;

    private Data data;
    private int speed = 1;

    private Random random = new Random();
    private Thread worker;
    private State state;

    private OnStateChangeListener onStateChangeListener;
    private OnDataChangeListener onDataChangeListener;

    private Runnable process = () -> {
        final int tick = 1;
        int simplifier = 0;
        while (state != State.IDLE) {
            if (state == State.RUN) {
                long startTime = System.nanoTime();
                boolean dataChanged = false;
                synchronized (sLock) {
                    // tick time to employees
                    for (Employee employee : data.office) {
                        employee.tick(tick);
                    }

                    // tick time to coffee machine outputs
                    data.coffeeMachine.tick(tick);

                    // check to update ui for each tick
                    if (speed <= UPDATES_LIMIT) {
                        dataChanged = true;
                    } else {
                        if (simplifier++ >= SIMPLIFIER_BUFFER) {
                            simplifier = 0;
                            dataChanged = true;
                        }
                    }

                    // check employee is going to be super-busy
                    for (Employee employee : data.office) { // in the office
                        if (employee.isBecameSuperBusy(random.nextFloat())) {
                            employee.setSuperBusy(true);
                        }
                    }
                    for (Employee employee : data.normalQueue) { // in normal queue
                        if (employee.isBecameSuperBusy(random.nextFloat())) {
                            employee.setSuperBusy(true);
                        }
                    }

                    // move super-busy employees from non-super-busy queue
                    ArrayList<Employee> movableEmployees = new ArrayList<>();
                    for (Employee employee : data.normalQueue) {
                        if (employee.isSuperBusy()) {
                            movableEmployees.add(employee);
                            dataChanged = true;
                        }
                    }
                    data.superBusyQueue.addAll(movableEmployees);
                    for (Employee employee : movableEmployees) {
                        data.normalQueue.remove(employee);
                    }

                    // check employee want coffee
                    ArrayList<Employee> coffeeWantedNormalEmployees = new ArrayList<>();
                    ArrayList<Employee> coffeeWantedSuperBusyEmployees = new ArrayList<>();
                    for (Employee employee : data.office) {
                        if (employee.isWantCoffee()) {
                            if (employee.isSuperBusy()) {
                                coffeeWantedSuperBusyEmployees.add(employee);
                            } else {
                                coffeeWantedNormalEmployees.add(employee);
                            }
                            dataChanged = true;
                        }
                    }
                    data.superBusyQueue.addAll(coffeeWantedSuperBusyEmployees);
                    data.normalQueue.addAll(coffeeWantedNormalEmployees);
                    for (Employee employee : coffeeWantedSuperBusyEmployees) {
                        data.office.remove(employee);
                    }
                    for (Employee employee : coffeeWantedNormalEmployees) {
                        data.office.remove(employee);
                    }

                    // give coffee from outputs if ready
                    int coffeesReady = data.coffeeMachine.popReadyCoffees();

                    // remove first employee from queues
                    ArrayList<Employee> coffeedEmployees = new ArrayList<>();
                    for (Employee employee : data.superBusyQueue) {
                        if (coffeesReady > 0) {
                            coffeedEmployees.add(employee);
                            dataChanged = true;
                            coffeesReady--;
                        } else break;
                    }
                    for (Employee employee : data.normalQueue) {
                        if (coffeesReady > 0) {
                            coffeedEmployees.add(employee);
                            dataChanged = true;
                            coffeesReady--;
                        } else break;
                    }
                    data.office.addAll(coffeedEmployees);
                    for (Employee employee : coffeedEmployees) {
                        data.superBusyQueue.remove(employee);
                        data.normalQueue.remove(employee);
                        employee.resetCoffeeTime();
                    }

                    // start make coffee if NEED and CAN
                    int emptyOutputs = data.coffeeMachine.getEmptyOutputs();
                    if (emptyOutputs > 0) {
                        for (Employee employee : data.superBusyQueue) {
                            if (emptyOutputs > 0) {
                                data.coffeeMachine.startMakingCoffee();
                                dataChanged = true;
                                emptyOutputs--;
                            } else break;
                        }
                        for (Employee employee : data.normalQueue) {
                            if (emptyOutputs > 0) {
                                data.coffeeMachine.startMakingCoffee();
                                dataChanged = true;
                                emptyOutputs--;
                            } else break;
                        }
                    }

                    // check employee is gone to be super-busy
                    for (Employee employee : data.office) {
                        if (employee.inNotSuperBusyAnymore()) employee.setSuperBusy(false);
                    }
                    for (Employee employee : data.superBusyQueue) { // but not remove him from this queue
                        if (employee.inNotSuperBusyAnymore()) employee.setSuperBusy(false);
                    }
                }

                // calculate speed
                if (dataChanged && onDataChangeListener != null) {
                    onDataChangeListener.onDataChange(data);
                }
                long endTime = System.nanoTime();
                long diff = endTime - startTime;
                long sleepInMillis = (long) ((1000F / speed) - (diff / 1000000F));
                int sleepInNanos = 0;
                if (sleepInMillis <= 0) {
                    sleepInMillis = 0;
                    sleepInNanos = (int) ((1000000000F / speed) - diff);
                    if (sleepInNanos < 0) sleepInNanos = 0;
                }
                try {
                    Thread.sleep(sleepInMillis, sleepInNanos);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    Log.e(TAG, ex.getMessage());
                }
            } else if (state == State.PAUSE) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    Log.e(TAG, ex.getMessage());
                }
            }
        }

    };

    public enum State {
        IDLE,
        RUN,
        PAUSE,
    }

    public interface OnStateChangeListener {
        void onChangeState(State state);
    }

    public interface OnDataChangeListener {
        void onDataChange(Data data);
    }

    public Simulator(Context context) {
        this.context = context;
        state = State.IDLE;
        initData();
    }

    public void initData() {
        Prefs prefs = new Prefs(context);
        AppSettings appSettings = new AppSettings();
        appSettings.setEmployeesCount(prefs.getEmployeesCount());
        appSettings.setChanceOfSuperbusyness(prefs.getChanceOfSuperBusyness());
        appSettings.setPeriodOfSuperbusyness(prefs.getPeriodOfSuperBusyness());
        appSettings.setCoffeeInterval(prefs.getCoffeeInterval());
        appSettings.setMakingTime(prefs.getMakingTime());
        appSettings.setOutputsCount(prefs.getOutputsCount());
        data = new Data(appSettings);
    }

    public void setSpeed(int value) {
        speed = value;
    }

    public int getSpeed() {
        return speed;
    }

    public void start() {
        if (state == State.IDLE || state == State.PAUSE) {
            setState(State.RUN);
            worker = new Thread(process,"SimulatorThread");
            worker.start();
        }
    }

    public void pause() {
        if (state == State.RUN) {
            setState(State.PAUSE);
        } else if (state == State.PAUSE) {
            setState(State.RUN);
        }
    }

    public void stop() {
        if (state == State.PAUSE || state == State.RUN) {
            setState(State.IDLE);
            initData();
        }
    }

    private void setState(State state) {
        this.state = state;
        if (onStateChangeListener != null) {
            onStateChangeListener.onChangeState(state);
        }
    }

    public State getState() {
        return state;
    }

    public Data getData() {
        return data;
    }

    public void setOnStateChangeListener(OnStateChangeListener listener) {
        onStateChangeListener = listener;
    }

    public void setOnDataChangeListener(OnDataChangeListener listener) {
        onDataChangeListener = listener;
    }
}

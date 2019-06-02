package com.ferrup.espresser.model;

import android.content.Context;

import com.ferrup.espresser.utils.Prefs;

public class Simulator {
    private Context context;

    private AppSettings appSettings;
    private int speed = 1;

    private Thread worker;
    private State state;

    private OnStateChangeListener onStateChangeListener;

    private Runnable process = () -> {
        if (state == State.RUN) {
            // check employee is become to be super-busy
            // move super-busy employees from non-super-busy queue
            // check employee want coffee
            // give coffee from output if ready
            // remove first employee from queues
            // start make coffee if NEED and CAN
            // check employee is gone to be super-busy
        } else if (state == State.PAUSE) {
            // TODO: wait until run
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

    public Simulator(Context context) {
        this.context = context;
        state = State.IDLE;
        initAppSettings();
    }

    private void initAppSettings() {
        Prefs prefs = new Prefs(context);
        appSettings = new AppSettings();
        appSettings.setEmployeesCount(prefs.getEmployeesCount());
        appSettings.setChanceOfSuperbusyness(prefs.getChanceOfSuperBusyness());
        appSettings.setPeriodOfSuperbusyness(prefs.getPeriodOfSuperBusyness());
        appSettings.setCoffeeInterval(prefs.getCoffeeInterval());
        appSettings.setMakingTime(prefs.getMakingTime());
        appSettings.setOutputsCount(prefs.getOutputsCount());
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
            // TODO: reset data
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

    public void setOnStateChangeListener(OnStateChangeListener listener) {
        onStateChangeListener = listener;
    }
}

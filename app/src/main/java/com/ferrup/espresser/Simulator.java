package com.ferrup.espresser;

import android.content.Context;
import android.util.Log;

import com.ferrup.espresser.model.AppSettings;
import com.ferrup.espresser.model.Data;
import com.ferrup.espresser.utils.Prefs;

public class Simulator {
    public static final String TAG = Simulator.class.getSimpleName();

    private Context context;

    private Data data;
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

            // calculate speed
        } else if (state == State.PAUSE) {
             try {
                 Thread.sleep(100);
             } catch (InterruptedException ex) {
                 ex.printStackTrace();
                 Log.e(TAG, ex.getMessage());
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

    public void setOnStateChangeListener(OnStateChangeListener listener) {
        onStateChangeListener = listener;
    }
}

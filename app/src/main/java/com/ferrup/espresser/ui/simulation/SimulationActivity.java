package com.ferrup.espresser.ui.simulation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ferrup.espresser.App;
import com.ferrup.espresser.R;
import com.ferrup.espresser.Simulator;
import com.ferrup.espresser.model.Data;
import com.ferrup.espresser.ui.settings.SettingsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SimulationActivity extends AppCompatActivity {
    private static final int SETTINGS_ACTIVITY_RESULT = 100;

    Simulator simulator;

    @BindView(R.id.speed_seek_bar)
    SeekBar speedSeekBar;

    @BindView(R.id.speed_text)
    TextView speedText;

    @BindView(R.id.start_button)
    Button startButton;

    @BindView(R.id.pause_button)
    Button pauseButton;

    @BindView(R.id.stop_button)
    Button stopButton;

    @BindView(R.id.office_text)
    TextView officeText;

    @BindView(R.id.office_bar)
    ProgressBar officeBar;

    @BindView(R.id.normal_text)
    TextView normalText;

    @BindView(R.id.normal_bar)
    ProgressBar normalBar;

    @BindView(R.id.super_busy_text)
    TextView superBusyText;

    @BindView(R.id.super_busy_bar)
    ProgressBar superBusyBar;

    @BindView(R.id.coffee_machine_text)
    TextView coffeeMachineText;

    @BindView(R.id.timerText)
    TextView timerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_simulation);
        ButterKnife.bind(this);
        simulator = App.getInstance().getSimulator();
    }

    @Override
    protected void onStart() {
        super.onStart();


        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int getSpeed(int progress) {
                return (int) Math.pow(10, progress);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    speedText.setText(getString(R.string.simulation_speed, getSpeed(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                simulator.setSpeed(getSpeed(seekBar.getProgress()));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        speedText.setText(getString(R.string.simulation_speed, simulator.getSpeed()));
        speedSeekBar.setProgress((int) Math.log10(simulator.getSpeed()));
        initProgressBars();

        onSimulatorStateChange(simulator.getState());
        simulator.setOnStateChangeListener(this::onSimulatorStateChange);
        onDataChange(simulator.getData());
        simulator.setOnDataChangeListener(this::onDataChange);
    }

    private void initProgressBars() {
        officeBar.setMax(simulator.getData().appSettings.getEmployeesCount());
        normalBar.setMax(simulator.getData().appSettings.getEmployeesCount());
        superBusyBar.setMax(simulator.getData().appSettings.getEmployeesCount());
    }

    private void onDataChange(Data data) {
        runOnUiThread(() -> {
            synchronized (Simulator.sLock) {
                officeText.setText(getString(R.string.office_employees, data.office.size(), data.appSettings.getEmployeesCount(), data.getSuperBusyCountInOffice()));
                officeBar.setProgress(data.office.size());

                normalText.setText(getString(R.string.normal_queue, data.normalQueue.size()));
                normalBar.setProgress(data.normalQueue.size());

                superBusyText.setText(getString(R.string.super_busy_queue, data.superBusyQueue.size()));
                superBusyBar.setProgress(data.superBusyQueue.size());

                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < data.coffeeMachine.getOutputs().size(); i++) {
                    if (i != 0) builder.append('\n');
                    builder.append(getString(R.string.coffee_progress,
                            i + 1,
                            data.coffeeMachine.getOutputs().get(i),
                            data.appSettings.getMakingTime()));
                }
                coffeeMachineText.setText(builder.toString());
                timerText.setText(getString(R.string.timer, data.formatDate()));
            }
        });
    }

    private void onSimulatorStateChange(Simulator.State simulatorState) {
        switch (simulatorState) {
            case IDLE:
                startButton.setEnabled(true);
                pauseButton.setEnabled(false);
                stopButton.setEnabled(false);
                break;
            case PAUSE:
                startButton.setEnabled(false);
                pauseButton.setEnabled(true);
                stopButton.setEnabled(true);
                break;
            case RUN:
                startButton.setEnabled(false);
                pauseButton.setEnabled(true);
                stopButton.setEnabled(true);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        simulator.setOnStateChangeListener(null);
        simulator.setOnDataChangeListener(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SETTINGS_ACTIVITY_RESULT) {
            simulator.initData();
        }
    }

    @OnClick(R.id.settings_button)
    public void onSettingsClicked() {
        simulator.stop();
        startActivityForResult(new Intent(this, SettingsActivity.class), SETTINGS_ACTIVITY_RESULT);
    }

    @OnClick(R.id.start_button)
    public void onStartClicked() {
        simulator.start();
    }

    @OnClick(R.id.pause_button)
    public void onPauseClicked() {
        simulator.pause();
    }

    @OnClick(R.id.stop_button)
    public void onStopClicked() {
        simulator.stop();
        onDataChange(simulator.getData());
    }
}

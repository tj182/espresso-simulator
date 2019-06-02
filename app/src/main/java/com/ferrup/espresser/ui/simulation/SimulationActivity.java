package com.ferrup.espresser.ui.simulation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ferrup.espresser.App;
import com.ferrup.espresser.R;
import com.ferrup.espresser.Simulator;
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

        speedText.setText(getString(R.string.simulation_speed, simulator.getSpeed()));
        speedSeekBar.setProgress((int) Math.log10(simulator.getSpeed()));
        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int getSpeed(int progress) {
                return (int) Math.pow(10, progress);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
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

        onSimulatorStateChange(simulator.getState());
        simulator.setOnStateChangeListener(this::onSimulatorStateChange);

        // TODO: setup views with data
    }

    private void onSimulatorStateChange(Simulator.State simulatorState) {
        switch (simulatorState) {
            case IDLE:
                startButton.setEnabled(true);
                pauseButton.setEnabled(false);
                stopButton.setEnabled(false);
                // stop visual simulation
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
    }
}

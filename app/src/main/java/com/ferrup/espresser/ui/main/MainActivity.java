package com.ferrup.espresser.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ferrup.espresser.R;
import com.ferrup.espresser.ui.settings.SettingsActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.settings_button)
    public void onSettingsClicked() {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}

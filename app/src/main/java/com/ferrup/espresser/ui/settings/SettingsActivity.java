package com.ferrup.espresser.ui.settings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ferrup.espresser.R;
import com.ferrup.espresser.utils.Prefs;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {
    private Prefs prefs;
    private SectionListAdapter settingsAdapter;

    @BindView(R.id.settings_list)
    RecyclerView settingsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        prefs = new Prefs(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ArrayList<Object> settings = new ArrayList<>();
        settings.add(getString(R.string.company));
        settings.add(new Pair<>(getString(R.string.employees_count), prefs.getEmployeesCount()));
        settings.add(new Pair<>(getString(R.string.chance_of_super_busy), prefs.getChanceOfSuperBusyness()));
        settings.add(new Pair<>(getString(R.string.period_of_super_busy), prefs.getPeriodOfSuperBusyness()));
        settings.add(new Pair<>(getString(R.string.coffee_interval), prefs.getCoffeeInterval()));
        settings.add(getString(R.string.coffee_machine));
        settings.add(new Pair<>(getString(R.string.making_time), prefs.getMakingTime()));
        settings.add(new Pair<>(getString(R.string.outputs_count), prefs.getOutputsCount()));
        settingsAdapter = new SectionListAdapter(settings);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        settingsList.addItemDecoration(dividerItemDecoration);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        settingsList.setLayoutManager(layoutManager);
        settingsList.setAdapter(settingsAdapter);
    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        this.onBackPressed();
    }
}

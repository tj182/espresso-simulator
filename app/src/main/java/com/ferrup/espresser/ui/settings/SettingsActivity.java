package com.ferrup.espresser.ui.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
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

    private SectionListAdapter.OnItemClickListener listener = (position, itemData) -> {
        if (itemData.second instanceof Integer) {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_value, null);
            EditText editText = view.findViewById(R.id.dialog_value);
            editText.setText(String.valueOf(itemData.second));
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(String.valueOf(itemData.first));
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), ((dialog, which) -> {
                Pair newItemData = new Pair<>(itemData.first, Integer.valueOf(editText.getText().toString()));
                changePref(newItemData);
                settingsAdapter.setItemData(position, newItemData);
            }));
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), ((dialog, which) -> dialog.dismiss()));
            alertDialog.setView(view);
            alertDialog.show();
            editText.requestFocus();
        } else if (itemData.second instanceof Boolean) {
            Pair newItemData = new Pair<>(itemData.first, !((boolean) itemData.second));
            changePref(newItemData);
            settingsAdapter.setItemData(position, newItemData);
        }
    };

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
        settings.add(new Pair<>(getString(R.string.coffee_interval_error), prefs.getCoffeeIntervalError()));
        settings.add(getString(R.string.coffee_machine));
        settings.add(new Pair<>(getString(R.string.making_time), prefs.getMakingTime()));
        settings.add(new Pair<>(getString(R.string.outputs_count), prefs.getOutputsCount()));
        settingsAdapter = new SectionListAdapter(settings);
        settingsAdapter.setOnItemClickListener(listener);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        settingsList.addItemDecoration(dividerItemDecoration);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        settingsList.setLayoutManager(layoutManager);
        settingsList.setAdapter(settingsAdapter);
    }

    private void changePref(Pair itemData) {
        String pref = String.valueOf(itemData.first);
        if (pref.equals(getString(R.string.employees_count))) {
            prefs.setEmployeesCount((int) itemData.second);
        } else if (pref.equals(getString(R.string.chance_of_super_busy))) {
            prefs.setChanceOfSuperBusyness((int) itemData.second);
        } else if (pref.equals(getString(R.string.period_of_super_busy))) {
            prefs.setPeriodOfSuperBusyness((int) itemData.second);
        } else if (pref.equals(getString(R.string.coffee_interval))) {
            prefs.setCoffeeInterval((int) itemData.second);
        } else if (pref.equals(getString(R.string.coffee_interval_error))) {
            prefs.setCoffeeIntervalError((int) itemData.second);
        } else if (pref.equals(getString(R.string.making_time))) {
            prefs.setMakingTime((int) itemData.second);
        } else if (pref.equals(getString(R.string.outputs_count))) {
            prefs.setOutputsCount((int) itemData.second);
        }
    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        this.onBackPressed();
    }
}

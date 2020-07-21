package com.lab.androidalarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_NOTIFY = "key_notify";
    private static final String KEY_INTERVAL = "key_interval";
    private static final String KEY_HOUR = "key_hour";
    private static final String KEY_MINUTE = "key_minute";

    private TimePicker timePicker;
    private Button btnNotify;
    private EditText editMinutes;

    private int interval;
    private int hour;
    private int minute;
    private boolean isActivated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timePicker = findViewById(R.id.time_picker);
        btnNotify = findViewById(R.id.btn_notify);
        editMinutes = findViewById(R.id.edit_number_interval);

        final SharedPreferences storage = getSharedPreferences("storage", Context.MODE_PRIVATE);
        isActivated = storage.getBoolean(KEY_NOTIFY, false);

        if(isActivated) {
            btnNotify.setText(R.string.pause);
            btnNotify.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_green_light));

            editMinutes.setText(String.valueOf(storage.getInt(KEY_INTERVAL, 0)));
            timePicker.setCurrentHour(storage.getInt(KEY_HOUR, timePicker.getCurrentHour()));
            timePicker.setCurrentMinute(storage.getInt(KEY_MINUTE, timePicker.getCurrentMinute()));

        }   else {
            btnNotify.setText(R.string.notify);
            btnNotify.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
        }

        timePicker.setIs24HourView(true);

        btnNotify.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!isActivated){
                    String sInterval = editMinutes.getText().toString();

                    if (sInterval.isEmpty()){
                        Toast.makeText(MainActivity.this, R.string.toastMessage, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    interval = Integer.parseInt(sInterval);
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                    Log.d("Test", String.format("%d, %d, %d", interval, hour, minute));

                    btnNotify.setText(R.string.pause);
                    btnNotify.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_green_light));

                    SharedPreferences.Editor edit = storage.edit();
                    edit.putBoolean(KEY_NOTIFY, true);
                    edit.putInt(KEY_INTERVAL, interval);
                    edit.putInt(KEY_HOUR, hour);
                    edit.putInt(KEY_MINUTE, minute);
                    edit.apply();

                    isActivated = true;
                } else {
                    btnNotify.setText(R.string.notify);
                    btnNotify.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));

                    SharedPreferences.Editor edit = storage.edit();
                    edit.putBoolean(KEY_NOTIFY, false);
                    edit.remove(KEY_INTERVAL);
                    edit.remove(KEY_HOUR);
                    edit.remove(KEY_MINUTE);
                    edit.apply();

                    isActivated = false;
                }
            }
        });
    }
}
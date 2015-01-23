package com.ai.alarmnotifications.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ai.alarmnotifications.notification_type.NotifData;
import com.ai.alarmnotifications.notification_type.NotificationType;
import com.ai.alarmnotifications.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends BaseActivity {

    @SuppressWarnings("unused")
    private static final String TAG = "MainActivity";
    private NotificationType currNotifType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        initSpinner();
        initButtons();
    }

    private void initSpinner() {
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_notif_types);
        List<NotificationType> list = new ArrayList<>
                (Arrays.asList(NotificationType.values()));
        if (!list.isEmpty()) {
            ArrayAdapter<NotificationType> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            if (!adapter.isEmpty()) {
                currNotifType = adapter.getItem(0);
            }
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    currNotifType = (NotificationType) parent.getItemAtPosition(pos);
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    private void initButtons() {
        final Button enabledButton = (Button) findViewById(R.id.button_isEnabled);
        final TextView enabledTextView = (TextView) findViewById(R.id.textView_isEnabled);
        enabledButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enabledTextView.setText
                        (NotifData.getInstance().isEnabled(currNotifType) ? "true" : "false");
            }
        });
        final Button delaysButton = (Button) findViewById(R.id.button_getDelays);
        final ListView delaysListView = (ListView) findViewById(R.id.listView_getDelays);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, 0);
        delaysListView.setAdapter(adapter);
//      delaysListView.setEmptyView(findViewById(android.R.id.empty));
        delaysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                if (currNotifType != null) {
                    long[] delays = NotifData.getInstance().getDelays(currNotifType);
                    if (delays != null) {
                        for (long delay : delays) {
                            adapter.add(delayToTime(delay));
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private static String delayToTime(long delay) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(delay),
                TimeUnit.MILLISECONDS.toMinutes(delay) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(delay) % TimeUnit.MINUTES.toSeconds(1));
    }
}

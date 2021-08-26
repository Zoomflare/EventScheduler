package com.nova.eventscheduler;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventActivity extends AppCompatActivity implements DatePicker.OnDateChangedListener {
    Context context;
    TimePicker timePicker;
    TextView dateTimeTextView;
    ImageView getDateImageView;
    Events event;
    EditText eventNameEt, eventDecriptionEt;
    DataManagement dataManager = new DataManagement();
    Button setButton, cancelBtn;
    private int pos;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        context=this;
        timePicker = findViewById(R.id.timePicker);
        dateTimeTextView = findViewById(R.id.calendarText);
        getDateImageView = findViewById(R.id.dateText);
        setButton = findViewById(R.id.setBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        eventDecriptionEt = findViewById(R.id.eventDescriptionEdit);
        eventNameEt = findViewById(R.id.eventNameEdit);
        getData();

        Calendar now = Calendar.getInstance();
        View.OnClickListener onClickListener= v ->  {
            Button button = (Button) v;


            if(button.getId() == R.id.cancelBtn){
                delete();
            }
            else
                save();
        };
        setButton.setOnClickListener(onClickListener);
        cancelBtn.setOnClickListener(onClickListener);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                event.getCalendarV().set(Calendar.HOUR_OF_DAY, hourOfDay);
                event.getCalendarV().set(Calendar.MINUTE, minute);
                event.getCalendarV().set(Calendar.SECOND, 0);
                updateView(event.getCalendarV());


            }
        });

        getDateImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        event.getCalendarV().set(Calendar.YEAR, year);
                        event.getCalendarV().set(Calendar.MONTH, month);
                        event.getCalendarV().set(Calendar.DAY_OF_MONTH,dayOfMonth);

                        if(event.getCalendarV().before(Calendar.getInstance())) {
                            Toast.makeText(context, "Enter a valid Time", Toast.LENGTH_SHORT).show();
                            event.setCalendarV(Calendar.getInstance());
                            return;
                        }
                        updateView(event.getCalendarV());
                    }
                },event.getCalendarV().get(Calendar.YEAR),event.getCalendarV().get(Calendar.MONTH),event.getCalendarV().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getData() {
        Intent intent = getIntent();

        pos=intent.getIntExtra("Pos", -1);
        List<Events> events = new ArrayList<>();
        if(dataManager.getData(context) != null && pos !=-1) {
            events.addAll(dataManager.getData(context));
            event=events.get(pos);
            eventDecriptionEt.setText(event.getEventDesc());
            eventNameEt.setText(event.getEventName());
            timePicker.setHour(event.getCalendarV().get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(event.getCalendarV().get(Calendar.MINUTE));
        }
        else
            event = new Events();

    }

    private void delete() {


        List<Events> events = new ArrayList<>();
        if(dataManager.getData(context) == null)
            finish();
        else {
            events.addAll(dataManager.getData(context));
            events.remove(pos);
            dataManager.saveData(events, context);
            finish();
        }
    }

    private void save() {


        if(eventNameEt.getText().length()==0){
            Toast.makeText(context, "Enter a valid name", Toast.LENGTH_SHORT).show();

            return;
        }
        if(event.getCalendarV().before(Calendar.getInstance())) {
            Toast.makeText(context, "Scheduled for Tomorrow", Toast.LENGTH_SHORT).show();
            event.getCalendarV().add(Calendar.DATE, 1);

        }
        event.setAlarmOn(true);
        event.setEventName(eventNameEt.getText().toString());
        event.setEventDesc(eventDecriptionEt.getText().toString());
        List<Events> events = new ArrayList<>();
        if(dataManager.getData(context) != null)
            events.addAll(dataManager.getData(context));
        events.add(event);
        dataManager.saveData(events,context);
        setAlarm();
        finish();

    }

    private void setAlarm() {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationManagement.class);
        intent.putExtra("Name",event.getEventName());
        intent.putExtra("Desc",event.getEventDesc());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,pos,intent,0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC, event.getCalendarV().getTimeInMillis(), pendingIntent);

        }
        else
            alarmManager.set(AlarmManager.RTC, event.getCalendarV().getTimeInMillis(), pendingIntent);

    }
    private void cancelAlarm(Calendar calendar){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationManagement.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,pos,intent,0);
        alarmManager.cancel(pendingIntent);
    }

    private void updateView(Calendar c) {

        dateTimeTextView.setText("Event Scheduled for "+DateFormat.getDateInstance(DateFormat.DEFAULT).format(c.getTime()) +" "+ DateFormat.getTimeInstance(DateFormat.DEFAULT).format(c.getTime()));
    }


    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        event.getCalendarV().set(Calendar.YEAR, year);
        event.getCalendarV().set(Calendar.MONTH, monthOfYear);
        event.getCalendarV().set(Calendar.DAY_OF_MONTH,dayOfMonth);
        updateView(event.getCalendarV());
    }


}

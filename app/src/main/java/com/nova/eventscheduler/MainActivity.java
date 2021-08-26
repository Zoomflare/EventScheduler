package com.nova.eventscheduler;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.OnCardListener {

    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;


    DataManagement dataManager = new DataManagement();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatingActionButton = findViewById(R.id.floatingActionBtn);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent(-1);
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        init_recycler();
    }

    private void init_recycler() {
        List<Events>eventsList = new ArrayList<>();
        List<Events>eventsListBackup = new ArrayList<>();
        eventsList = (dataManager.getData(this));

        if(eventsList == null)
            return;

        recyclerView = findViewById(R.id.recyclerView);

        for(Events event: eventsList){
            if(!event.getCalendarV().before(Calendar.getInstance())){
                eventsListBackup.add(event);
            }
        }
        dataManager.saveData(eventsListBackup, this);
        ArrayList<Events>eventsArrayList = new ArrayList<>();
        eventsArrayList.addAll(eventsListBackup);

        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(this,eventsArrayList, this::onCardClick);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }



    private void addEvent(int pos) {
        Intent it_goToEvent = new Intent(MainActivity.this, EventActivity.class);
        it_goToEvent.putExtra("Pos",pos);
        startActivity(it_goToEvent);

    }

    @Override
    public void onCardClick(int position) {
        addEvent(position);
    }




}
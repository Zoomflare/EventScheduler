package com.nova.eventscheduler;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<Events> eventsArrayList;
    private Context context;
    OnCardListener onCardListener;

    public RecyclerAdapter(Context context, ArrayList<Events> eventArrayList, OnCardListener onCardListener) {
        this.eventsArrayList = eventArrayList;
        this.context = context;
        this.onCardListener = onCardListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, onCardListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Events event = eventsArrayList.get(position);
        holder.nameTextView.setText(event.getEventName());
        holder.descriptionTextView.setText(event.getEventDesc());
        holder.timeTextView.setText(DateFormat.getTimeInstance(DateFormat.DEFAULT).format(event.getCalendarV().getTime())  + " " + DateFormat.getDateInstance(DateFormat.DEFAULT).format(event.getCalendarV().getTime()));



    }

    @Override
    public int getItemCount() {
        if(!eventsArrayList.isEmpty())
            return eventsArrayList.size();

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnCardListener onCardListener;
        ConstraintLayout layoutParent;
        TextView nameTextView, descriptionTextView, timeTextView;
        Switch toggle;
        public ViewHolder(View itemView, OnCardListener onCardListener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.eventNamee);
            layoutParent = itemView.findViewById(R.id.layoutParent);
            descriptionTextView = itemView.findViewById(R.id.eventDescprition);
            timeTextView = itemView.findViewById(R.id.eventTime);
            this.onCardListener = onCardListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCardListener.onCardClick(getAdapterPosition());
        }


    }

    public interface OnCardListener{
        void onCardClick(int position);

    }


}

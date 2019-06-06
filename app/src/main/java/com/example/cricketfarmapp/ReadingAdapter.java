package com.example.cricketfarmapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ReadingAdapter extends RecyclerView.Adapter<ReadingAdapter.ViewHolder> {

    private ArrayList<Reading> readingList;

    public ReadingAdapter(){
        readingList = new ArrayList<Reading>();
    }

    public void addToList(Reading reading){
        readingList.add(reading);
    }

    public void sortList(){
        Collections.sort(readingList);
    }

    public boolean contains(Reading reading){
        return readingList.contains(reading);
    }

    public void remove(Reading reading){
        readingList.remove(reading);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.reading_list_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.date.setText(readingList.get(i).getReadingDate());
        viewHolder.temp.setText("Temperature : " + readingList.get(i).getTemperature() + " °C");
        viewHolder.eggHum.setText("Egg Zone Humidity : " + readingList.get(i).getEggZoneHumidity() + "%");
        viewHolder.adultHum.setText("Adult Zone Humidity : " + readingList.get(i).getAdultZoneHumidity() + "%");
        viewHolder.spongeHum.setText("Sponge Humidity : " + readingList.get(i).getSpongeHumidity() +"%");
        if(readingList.get(i).verifyConditions()){
            viewHolder.status.setImageResource(R.drawable.successicon);
        }
        else{
            viewHolder.status.setImageResource(R.drawable.warningicon);
        }
    }



    @Override
    public int getItemCount() {
        return readingList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView temp;
        TextView eggHum;
        TextView adultHum;
        TextView spongeHum;
        ImageView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.readingItemDate);
            temp = itemView.findViewById(R.id.readingItemTemp);
            eggHum = itemView.findViewById(R.id.readingItemEggHum);
            adultHum = itemView.findViewById(R.id.readingItemAdultHum);
            status = itemView.findViewById(R.id.readingItemStatus);
            spongeHum = itemView.findViewById(R.id.readingItemSpongeHum);
        }
    }
}

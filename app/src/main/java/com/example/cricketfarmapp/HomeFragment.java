package com.example.cricketfarmapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    TextView lastReadingSpongeHumView;
    TextView lastReadingEggHumView;
    TextView lastReadingAdultHumView;
    TextView lastReadingTempView;
    TextView lastReadingDateView;
    FirebaseDatabase database;
    Reading lastReading;

    SharedPreferences sharedPreferences;

    //Need to use onCreate to prevent a crash when I receive a notification while the app is in the background
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle bundle){
        View rootView = layoutInflater.inflate(R.layout.fragment_home,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lastReadingAdultHumView = view.findViewById(R.id.lastReadingAdultZoneHumidity);
        lastReadingEggHumView = view.findViewById(R.id.lastReadingEggZoneHumidity);
        lastReadingTempView = view.findViewById(R.id.lastReadingTemperature);
        lastReadingSpongeHumView = view.findViewById(R.id.lastReadingSpongeHumidity);
        lastReadingDateView = view.findViewById(R.id.lastReadingDate);

        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("readings");

        //This listener is listening on the child of the "readings" node in the database
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Reading newReading = dataSnapshot.getValue(Reading.class);
                //The first adequate reading becomes the last reading
                if(newReading.getSystemKey().equals(sharedPreferences.getString("systemKey","")) && lastReading==null){
                    lastReading=dataSnapshot.getValue(Reading.class);
                    bindLastReadingData();
                }
                else {
                    // If the date of the new reading is superior to the current LastReading, then the new one becomes the last reading
                    if(newReading.getSystemKey().equals(sharedPreferences.getString("systemKey","")) && newReading.compareTo(lastReading)>0){
                        lastReading = newReading;
                        bindLastReadingData();
                    }
                }

            }

           @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        ref.addChildEventListener(childEventListener);
    }

    public void bindLastReadingData() {
        lastReadingDateView.setText(lastReading.getReadingDate());
        lastReadingEggHumView.setText(lastReading.getEggZoneHumidity() + "%");
        lastReadingTempView.setText(lastReading.getTemperature() + " °C");
        lastReadingAdultHumView.setText(lastReading.getAdultZoneHumidity() + "%");
        lastReadingSpongeHumView.setText(lastReading.getSpongeHumidity() + "%");
    }
}

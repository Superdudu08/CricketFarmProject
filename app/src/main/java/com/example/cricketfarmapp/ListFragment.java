package com.example.cricketfarmapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;

public class ListFragment extends Fragment {

    RecyclerView readingList;
    ReadingAdapter readingAdapter;
    FirebaseDatabase database;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle bundle){
        View rootView = layoutInflater.inflate(R.layout.fragment_list,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        readingList = this.getView().findViewById(R.id.listRecyclerView);
        readingList.hasFixedSize();
        readingList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        readingList.addItemDecoration(new DividerItemDecoration(readingList.getContext(),DividerItemDecoration.VERTICAL));
        readingAdapter = new ReadingAdapter();
        readingList.setAdapter(readingAdapter);

        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("readings");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Reading newReading = dataSnapshot.getValue(Reading.class);
                if(newReading.getSystemKey().equals(sharedPreferences.getString("systemKey",""))) {
                    readingAdapter.addToList(dataSnapshot.getValue(Reading.class));
                    //SortList sorts the list from the oldest reading to the newest
                    readingAdapter.sortList();
                    //NotifyDataSetChanged makes the adapter updates his view so that it displays the new reading
                    readingAdapter.notifyDataSetChanged();
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
}

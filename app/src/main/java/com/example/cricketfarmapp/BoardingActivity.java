package com.example.cricketfarmapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BoardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarding);
        getSupportFragmentManager().beginTransaction().replace(R.id.boardingFrame,new TutorialFragment()).commit();
    }
}

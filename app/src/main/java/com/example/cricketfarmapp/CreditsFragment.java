package com.example.cricketfarmapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CreditsFragment extends Fragment {
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle bundle){
        View rootView = layoutInflater.inflate(R.layout.fragment_credits,container,false);
        return rootView;
    }
}

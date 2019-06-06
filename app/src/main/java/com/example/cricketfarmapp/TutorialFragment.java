package com.example.cricketfarmapp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v17.leanback.app.OnboardingSupportFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TutorialFragment extends OnboardingSupportFragment {

    private ImageView middlePic;

    private int[] titles = {
            R.string.tutorialTitleSetUp,
            R.string.tutorialTitleDayToDay,
            R.string.tutorialTitleDoorControl,
            R.string.tutorialTitlePickUp,
            R.string.tutorialTitlePickUp
    };

    private int[] descriptions = {
            R.string.tutorialDescriptionSetUp,
            R.string.tutorialDescriptionDayToDay,
            R.string.tutorialDescriptionDoorControl,
            R.string.tutorialDescriptionPickUp1,
            R.string.tutorialDescriptionPickUp2
    };

    private int[] images = {
            R.drawable.tutorialimagesetup,
            R.drawable.tutorialimagedaytoday,
            R.drawable.tutorialimagedoorcontrol,
            R.drawable.tutorialimagepickup1,
            R.drawable.tutorialimagepickup2
    };
    @Override
    protected int getPageCount() {
        return titles.length;
    }

    @Override
    protected CharSequence getPageTitle(int i) {
        return getString(titles[i]);
    }

    @Override
    protected CharSequence getPageDescription(int i) {
        return getString(descriptions[i]);
    }

    @Nullable
    @Override
    protected View onCreateBackgroundView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        View background = new View(getActivity());
        background.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        return background;
    }

    @Nullable
    @Override
    protected View onCreateContentView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        middlePic = new ImageView(getActivity());
        middlePic.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        middlePic.setPadding(0,32,0,32);
        middlePic.setImageDrawable(getResources().getDrawable(R.drawable.tutorialimagesetup));
        return middlePic;
    }

    @Override
    protected void onPageChanged(int newPage, int previousPage) {
        middlePic.setImageDrawable(getResources().getDrawable(images[newPage]));
    }

    @Nullable
    @Override
    protected View onCreateForegroundView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return null;
    }

    @Override
    protected void onFinishFragment() {
        super.onFinishFragment();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        editor.putBoolean("boardingDone",true).apply();
        getActivity().finish();
    }

    @Override
    public int onProvideTheme() {
        return R.style.Theme_Leanback_Onboarding;
    }
}

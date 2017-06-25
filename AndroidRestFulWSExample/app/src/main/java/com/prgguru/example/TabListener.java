package com.prgguru.example;
import android.app.ActionBar.Tab;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;


/**
 * Created by PinkalJay on 4/2/15.
 */
public class TabListener implements ActionBar.TabListener {

    private Fragment fragment;

    // The contructor.
    public TabListener(Fragment fragment) {
        this.fragment = fragment;
    }

    // When a tab is tapped, the FragmentTransaction replaces
    // the content of our main layout with the specified fragment;
    // that's why we declared an id for the main layout.
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        ft.replace(R.id.tab_mainlayout, fragment);
    }

    // When a tab is unselected, we have to hide it from the user's view.
    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        ft.remove(fragment);
    }

    // Nothing special here. Fragments already did the job.
    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {

    }
}

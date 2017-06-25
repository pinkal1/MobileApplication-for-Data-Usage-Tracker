package com.prgguru.example;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by PinkalJay on 4/1/15.
 */
public class Tab_mainActivity extends Activity {

    // Declaring our tabs and the corresponding fragments.
    ActionBar.Tab settingTab, manageTab, reportTab;
    Fragment settingFragmentTab = new SettingFragmentTab();
    Fragment manageFragmentTab = new ManageFragmentTab();
    Fragment reportFragmentTab = new ReportFragmentTab();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_mainlayout);

        // Asking for the default ActionBar element that our platform supports.
        ActionBar actionBar = getActionBar();

        // Screen handling while hiding ActionBar icon.
        actionBar.setDisplayShowHomeEnabled(false);

        // Screen handling while hiding Actionbar title.
        actionBar.setDisplayShowTitleEnabled(false);

        // Creating ActionBar tabs.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Setting custom tab icons.
        settingTab=actionBar.newTab().setText("Setting");
        manageTab=actionBar.newTab().setText("Manage");
        reportTab=actionBar.newTab().setText("Report");
        //settingTab= actionBar.newTab().setIcon(R.drawable.bmw_logo);
        // manageTab = actionBar.newTab().setIcon(R.drawable.toyota_logo);
        //reportTab = actionBar.newTab().setIcon(R.drawable.ford_logo);

        // Setting tab listeners.
        settingTab.setTabListener(new TabListener(settingFragmentTab));
        manageTab.setTabListener(new TabListener(manageFragmentTab));
        reportTab.setTabListener(new TabListener(reportFragmentTab));

        // Adding tabs to the ActionBar.
        actionBar.addTab(settingTab);
        actionBar.addTab(manageTab);
        actionBar.addTab(reportTab);
    }
}
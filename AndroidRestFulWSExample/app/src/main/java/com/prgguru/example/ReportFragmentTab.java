package com.prgguru.example;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by PinkalJay on 4/1/15.
 */
public class ReportFragmentTab  extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.report_layout, container, false);
        Intent i=new Intent(getActivity(),Family_rep.class);
        startActivity(i);
        return rootView;


    }
}
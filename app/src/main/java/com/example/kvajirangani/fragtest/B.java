package com.example.kvajirangani.fragtest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class B extends Fragment {


    TextView txt;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_b, container, false);

      //  txt = (TextView)view.findViewById(R.id.txtTitle);
        //clickText = (TextView) findViewById(R.id.txtTitle);
/*
        RelativeLayout rl;

        rl= (RelativeLayout) container.findViewById(R.id.relative_LayoutRV);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"hello",Toast.LENGTH_SHORT).show();
            }
        }); */


    }




}

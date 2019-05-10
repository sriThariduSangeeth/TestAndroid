package com.example.kvajirangani.fragtest;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/*
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
*/

public class MainActivity extends AppCompatActivity {
    private String task;

   // RelativeLayout rl;
    private EditText input;
    private Context context=this;
  //  private TextView clickText;
    private Button submitBtn;
    ArrayList<String> taskList = new ArrayList<String>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.textBox);
        submitBtn = (Button) findViewById(R.id.addbtn);
   //     clickText = (TextView) findViewById(R.id.txtTitle);

    //    setContentView(R.id.relative_LayoutRV);
       // clickText.setOnClickListener((View.OnClickListener) this);
      //  rl = (RelativeLayout) findViewById(R.id.relative_LayoutRV);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = input.getText().toString();
                taskList.add(task);

                RecyclerView myrecycler = findViewById(R.id.recyclerView);
                myrecycler.setLayoutManager(new LinearLayoutManager(context));
                //String[] languages = {"Java","Js"};
              //  Object[] array = taskList.toArray();
                myrecycler.setAdapter(new adapter(taskList));

               // showToast(task);
            }
        });

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_layout, new showtask());
        fragmentTransaction.commit();


        //  RecyclerView myrecycler = findViewById(R.id.recyclerView);

    //  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
     //   fragmentTransaction.add(R.id.)


        //RecyclerView myrecycler = findViewById(R.id.recyclerView);
       // myrecycler.setLayoutManager(new LinearLayoutManager(this));
        //String[] languages = {"Java","Js"};
        //myrecycler.setAdapter(new adapter(languages));
    }



}

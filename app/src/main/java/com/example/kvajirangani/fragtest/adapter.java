package com.example.kvajirangani.fragtest;

//adapter adapts our data so that it can be displayed

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<adapter.recyclerViewHolder >
{

    //data to show in the recycler view is in an array
    private ArrayList<String> data; //to keep the array data

    public adapter(ArrayList<String> data)
    {

        this.data = data;
    }


    @Override
    //create viewholder and store
    public recyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycle_view, parent, false);
       // MyViewHolder vh = new MyViewHolder(v);


        return new recyclerViewHolder(v);
       // LayoutInflater inflater = LayoutInflater.from(parent.getContext());
       // View view = inflater.inflate(R.layout.layout_recycle_view, parent, false);

       // return new recyclerViewHolder(view);
    }

    @Override
    //views bind with data
    public void onBindViewHolder(recyclerViewHolder holder, int position) {
       // String title = data[position];
       //  String title = data<position>;

      //  holder.txtTitle.setText(title);

        holder.txtTitle.setText(data.get(position));


    }

    @Override
    public int getItemCount() {
        return data.size();


    }

    public class recyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle;

        public recyclerViewHolder(View itemView) {
            super(itemView);
            txtTitle =  itemView.findViewById(R.id.txtTitle);



            //text view click
            txtTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  //  FragmentTransaction fragmentTransaction = getSupportFragmentManager();
            //        FragmentTransaction fr = getFragmentManager().


//


                    txtTitle.setText("hello");


                 //   FragmentTransaction fragmentTransaction = getSupportFragmentManager().
//                    showtask fragment1 = new showtask();
//                    FragmentManager fragmentManager = getFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(android.R.id, fragment1);
//                    fragmentTransaction.commit();
                    /*
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction()
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                            .show(somefrag)
                            .commit();  */
                }
            });
        }
    }

}

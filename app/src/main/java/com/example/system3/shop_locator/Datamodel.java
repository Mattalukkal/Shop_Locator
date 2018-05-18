package com.example.system3.shop_locator;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by System 3 on 5/14/2018.
 */

public class Datamodel extends RecyclerView.Adapter<Datamodel.ViewHolder>{
    private ArrayList<String> countries;

    public Datamodel(ArrayList<String> countries){
        this.countries = countries;
    }
    @Override
    public Datamodel.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.job_card_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Datamodel.ViewHolder viewHolder, int i) {

        viewHolder.type.setText(countries.get(i));

    }

    @Override
    public int getItemCount() {

        return countries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView type;
        public ViewHolder(View itemView) {
            super(itemView);

             type =(TextView) itemView.findViewById(R.id.type);
        }
    }
}

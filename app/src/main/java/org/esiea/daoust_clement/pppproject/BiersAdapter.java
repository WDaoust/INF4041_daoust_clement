package org.esiea.daoust_clement.pppproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.Collections;
import java.util.List;



/**
 * Created by william on 30/12/2015.
 */
public class BiersAdapter extends RecyclerView.Adapter<BiersAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    List<Information> data = Collections.emptyList();
    public BiersAdapter(Context context,List<Information> data){
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row, parent, false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Information current=data.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconId);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.listText);
            icon=(ImageView)itemView.findViewById(R.id.listIcon);
            icon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(getPosition()==0){
                Intent myIntent = new Intent(v.getContext(), recoActivity.class);
                v.getContext().startActivity(myIntent);
            }
            if(getPosition()==1){
                Intent myIntent = new Intent(v.getContext(), top5Activity.class);
                v.getContext().startActivity(myIntent);
            }
            if(getPosition()==3){
                Toast.makeText(context, "Not yet implemented", Toast.LENGTH_SHORT).show();
            }
            if(getPosition()==2){
                Toast.makeText(context, "Not yet implemented", Toast.LENGTH_SHORT).show();
            }
            if(getPosition()==4){
                Intent myIntent = new Intent(v.getContext(), HelpActivity.class);
                v.getContext().startActivity(myIntent);
            }
        }
    }
}

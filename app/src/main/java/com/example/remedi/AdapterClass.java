package com.example.remedi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MyViewHolder> {
    ArrayList<DataSetFire> mlist;
    Context context;

    public AdapterClass( Context context,   ArrayList<DataSetFire> mlist){

        this.mlist = mlist;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.username.setText(mlist.get(position).getUsername());
        holder.status.setText(mlist.get(position).getFullname());
       // holder.profile..setIcon(dataSnapshot.child("photoUrl").getValue(String.class));

        Picasso.with(context).load(mlist.get(position).getProfileimage()).placeholder(R.drawable.profile).into(holder.profile);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    class  MyViewHolder extends RecyclerView.ViewHolder{
        TextView username,status,progressDate,progressdetails;
        CircleImageView profile;
        RelativeLayout expandeblelo;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_text);
            status = itemView.findViewById(R.id.status_text);
            profile = itemView.findViewById(R.id.users_profile_image);

/*
            progressDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataSetFire dataSetFire = mlist.get(getAdapterPosition());
                    dataSetFire.setExpanded(!dataSetFire.isExpanded());
                }
            });*/

        }
    }

}

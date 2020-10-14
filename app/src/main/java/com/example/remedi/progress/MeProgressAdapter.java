package com.example.remedi.progress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remedi.AdapterClass;
import com.example.remedi.DataSetFire;
import com.example.remedi.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MeProgressAdapter extends RecyclerView.Adapter<MeProgressAdapter.MyViewHolder> {
        ArrayList<DataSetFire> mlist;
        Context context;

        public MeProgressAdapter( Context context,   ArrayList<DataSetFire> mlist){

            this.mlist = mlist;
            this.context = context;
        }

        @NonNull
        @Override
        public MeProgressAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_row,parent,false);
            return new MyViewHolder(view);
        }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.progressDate.setText(mlist.get(position).getDate());
        boolean isExpanded = mlist.get(position).isExpanded();
        holder.expandeblelo.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.progressdetails.setText(mlist.get(position).getDetails());

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
                expandeblelo = itemView.findViewById(R.id.progressExpandeble);
                progressDate = itemView.findViewById(R.id.progressdate);
                progressdetails = itemView.findViewById(R.id.progress_details);

            progressDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataSetFire dataSetFire = mlist.get(getAdapterPosition());
                    dataSetFire.setExpanded(!dataSetFire.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

            }
        }

    }


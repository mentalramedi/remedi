package com.example.remedi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class FirebaseViewHolder extends RecyclerView.ViewHolder {

    public TextView memberName,memberUsername,deviceName,deviceStatus;
    public CircleImageView memberDp;
    public Button removeMember;
    public FirebaseViewHolder(@NonNull View itemView) {
        super(itemView);
        memberName = itemView.findViewById(R.id.allowed_member_fullname);
        memberDp = itemView.findViewById(R.id.memberDp);
        removeMember = itemView.findViewById(R.id.removeMemberbtn);
        //username = itemView.findViewById(R.id.username_text);
        memberUsername = itemView.findViewById(R.id.allowed_member_username);
        deviceName = itemView.findViewById(R.id.devicename);
        deviceStatus = itemView.findViewById(R.id.devicestatus);

    }


}

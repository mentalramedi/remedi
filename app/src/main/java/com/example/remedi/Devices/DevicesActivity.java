package com.example.remedi.Devices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.remedi.DataSetFire;
import com.example.remedi.FirebaseViewHolder;
import com.example.remedi.signup_in_setup.LoginActivity;
import com.example.remedi.MainActivity;
import com.example.remedi.members.MembersActivity;
import com.example.remedi.ProfileActivity;
import com.example.remedi.progress.ProgressActivity;
import com.example.remedi.R;
import com.example.remedi.TestsActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DevicesActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private FirebaseRecyclerOptions<DataSetFire> deviceoptions;
    private FirebaseRecyclerAdapter<DataSetFire, FirebaseViewHolder> adapter;
    private DatabaseReference databaseReference;
    private CircleImageView NavProfileImage;
    private TextView NavProfileUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.devices_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Devices");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(DevicesActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        String currentUserID = mAuth.getCurrentUser().getUid();

        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");


        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("fullname")) {
                        String fullname = dataSnapshot.child("fullname").getValue().toString();
                        NavProfileUserName.setText(fullname);

                    }
                    if(dataSnapshot.hasChild("profileimage"))
                    {
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.with(DevicesActivity.this).load(image).placeholder(R.drawable.profile).into(NavProfileImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);
        NavProfileImage = (CircleImageView) navView.findViewById(R.id.nav_profile_image);
        NavProfileUserName = (TextView) navView.findViewById(R.id.nav_user_full_name);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                UserMenuSelector(item);
                return false;
            }
        });

        mAuth = FirebaseAuth.getInstance();


        Button addDeviceBtn = (Button) findViewById(R.id.addDeviceBtn);
        addDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addDeviceintent = new Intent(DevicesActivity.this,AddDeviceActivity.class);
                startActivity(addDeviceintent);
            }
        });
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(DevicesActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {

        }

        return true;
    }


    private void UserMenuSelector(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_home:
                SendUserToMainActivity();
                break;
            case R.id.nav_profile:
                SendUserToProfileActivity();
                break;
            case R.id.nav_devices:
                SendUserToDevicesActivity();
                break;
            case R.id.nav_members:
                SendUserToMembersActivity();
                break;
            case R.id.nav_progress:
                SendUserToProgressActivity();
                break;
            case R.id.nav_tests:
                SendUserToTestsActivity();
                break;

            case R.id.nav_logout:
                mAuth.signOut();
                SendUserToLoginActivity();
                break;

        }
    }

    private void SendUserToTestsActivity() {

        Intent testsIntent = new Intent(DevicesActivity.this, TestsActivity.class);
        startActivity(testsIntent);
        finish();
    }

    private void SendUserToProgressActivity() {
        Intent progressIntent = new Intent(DevicesActivity.this, ProgressActivity.class);
        startActivity(progressIntent);
        finish();
    }

    private void SendUserToProfileActivity() {
        Intent profileIntent = new Intent(DevicesActivity.this, ProfileActivity.class);
        startActivity(profileIntent);
        finish();
    }

    private void SendUserToMembersActivity() {

        Intent membersIntent = new Intent(DevicesActivity.this, MembersActivity.class);
        startActivity(membersIntent);
        finish();
    }

    private void SendUserToDevicesActivity() {

        Intent devicesIntent = new Intent(DevicesActivity.this, DevicesActivity.class);
        startActivity(devicesIntent);
        finish();
    }

    private void SendUserToMainActivity() {

        Intent mainIntent = new Intent(DevicesActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }
}

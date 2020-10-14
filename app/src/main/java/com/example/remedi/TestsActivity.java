package com.example.remedi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.remedi.Devices.DevicesActivity;
import com.example.remedi.members.MembersActivity;
import com.example.remedi.progress.ProgressActivity;
import com.example.remedi.signup_in_setup.LoginActivity;
import com.example.remedi.testCategories.QuestionnairesActivity;
import com.example.remedi.testCategories.VideosActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TestsActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private CircleImageView NavProfileImage;
    private TextView NavProfileUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.tests_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Tests");


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(TestsActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);

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
                        Picasso.with(TestsActivity.this).load(image).placeholder(R.drawable.profile).into(NavProfileImage);
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
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(TestsActivity.this, LoginActivity.class);
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

        Intent testsIntent = new Intent(TestsActivity.this, TestsActivity.class);
        startActivity(testsIntent);
        finish();
    }

    private void SendUserToProgressActivity() {
        Intent progressIntent = new Intent(TestsActivity.this, ProgressActivity.class);
        startActivity(progressIntent);
        finish();
    }

    private void SendUserToProfileActivity() {
        Intent profileIntent = new Intent(TestsActivity.this, ProfileActivity.class);
        startActivity(profileIntent);
        finish();
    }

    private void SendUserToMembersActivity() {

        Intent membersIntent = new Intent(TestsActivity.this, MembersActivity.class);
        startActivity(membersIntent);
        finish();
    }

    private void SendUserToDevicesActivity() {

        Intent testsIntent = new Intent(TestsActivity.this, DevicesActivity.class);
        startActivity(testsIntent);
        finish();
    }

    private void SendUserToMainActivity() {

        Intent mainIntent = new Intent(TestsActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }

    public void progressActivity(View view) {
        SendUserToProgressActivity();
    }


    public void videosActivity(View view) {
        Intent videosIntent = new Intent(TestsActivity.this, VideosActivity.class);
        startActivity(videosIntent);
    }

    public void audiosActivity(View view) {

    }

    public void gamesActivity(View view) {
    }

    public void questionnairesActivity(View view) {
        Intent qnaire = new Intent(TestsActivity.this, QuestionnairesActivity.class);
        startActivity(qnaire);
    }
}

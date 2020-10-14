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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.example.remedi.Devices.DevicesActivity;
import com.example.remedi.members.MembersActivity;
import com.example.remedi.progress.ProgressActivity;
import com.example.remedi.signup_in_setup.LoginActivity;
import com.example.remedi.signup_in_setup.SetupActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {


    private Toolbar mToolbar;
    private WebView webview;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;

    private CircleImageView NavProfileImage;
    private TextView NavProfileUserName;
    String currentUserID;

    @Override
    protected void onStart() {
        super.onStart();

        if(CheckNetwork.isInternetAvailable(MainActivity.this)) //returns true if internet available
        {

            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser == null) {
                SendUserToLoginActivity();
            }
            else{
                CheckUserExistence();
            }

            //do something. loadwebview.
        }
        else
        {
            Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Remedi");





            //do something. loadwebview.


        mAuth = FirebaseAuth.getInstance();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                //Go to login
                SendUserToLoginActivity();
        }
        else{
            currentUserID = mAuth.getCurrentUser().getUid();
            UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");


            UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if(dataSnapshot.exists())
                    {
                        if(dataSnapshot.hasChild("fullname"))
                        {
                            String fullname = dataSnapshot.child("fullname").getValue().toString();
                            NavProfileUserName.setText(fullname);
                        }

                        if(dataSnapshot.hasChild("profileimage"))
                        {
                            String image = dataSnapshot.child("profileimage").getValue().toString();
                            Picasso.with(MainActivity.this).load(image).placeholder(R.drawable.profile).into(NavProfileImage);
                        }
                        else
                        {
                          //  Toast.makeText(MainActivity.this, "Profile name do not exists...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }



        if(CheckNetwork.isInternetAvailable(MainActivity.this)) //returns true if internet available
        {
        webview = (WebView) findViewById(R.id.cocis_news_webview);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webview.loadUrl("https://www.who.int/mental_health/en/");
        webview.canGoBackOrForward(4);
        webview.goBackOrForward(4);

        }
        else
        {
            Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        navigationView.setItemIconTintList(null);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




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



    }



    private void CheckUserExistence() {

        final String current_user_id = mAuth.getCurrentUser().getUid();
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if((!dataSnapshot.hasChild(current_user_id)||!dataSnapshot.child(current_user_id).hasChild("username")||!dataSnapshot.child(current_user_id).hasChild("fullname")||!dataSnapshot.child(current_user_id).hasChild("contact")||!dataSnapshot.child(current_user_id).hasChild("dob")||!dataSnapshot.child(current_user_id).hasChild("profileimage")||!dataSnapshot.child(current_user_id).hasChild("gender"))){
                    SendUserToSetupActivity();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void SendUserToSetupActivity() {

        Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }


    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
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

        Intent testsIntent = new Intent(MainActivity.this, TestsActivity.class);
        startActivity(testsIntent);
        finish();
    }

    private void SendUserToProgressActivity() {
        Intent progressIntent = new Intent(MainActivity.this, ProgressActivity.class);
        startActivity(progressIntent);
        finish();
    }

    private void SendUserToProfileActivity() {
        Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(profileIntent);
        finish();
    }

    private void SendUserToMembersActivity() {

        Intent membersIntent = new Intent(MainActivity.this, MembersActivity.class);
        startActivity(membersIntent);
        finish();
    }

    private void SendUserToDevicesActivity() {

        Intent testsIntent = new Intent(MainActivity.this, DevicesActivity.class);
        startActivity(testsIntent);
        finish();
    }    

    private void SendUserToMainActivity() {

        Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }


}

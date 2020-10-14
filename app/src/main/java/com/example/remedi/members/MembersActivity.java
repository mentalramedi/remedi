package com.example.remedi.members;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.remedi.DataSetFire;
import com.example.remedi.Devices.DevicesActivity;
import com.example.remedi.FirebaseViewHolder;
import com.example.remedi.signup_in_setup.LoginActivity;
import com.example.remedi.MainActivity;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class MembersActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private ArrayList<DataSetFire> arrayList;
    private FirebaseRecyclerOptions<DataSetFire> options;
    private FirebaseRecyclerAdapter<DataSetFire, FirebaseViewHolder> adapter;
    private DatabaseReference databaseReference,mUserDatabase;
    private Button addMemberBtn;
    String currentUserID;
    private CircleImageView NavProfileImage;
    private TextView NavProfileUserName;


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.member_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Members");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MembersActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        String currentUserID = mAuth.getCurrentUser().getUid();

        RelativeLayout relativeLayout = findViewById(R.id.memberRelativeLayout);
        final ProgressBar progressBar = new ProgressBar(MembersActivity.this,null,android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayout.addView(progressBar,params);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);



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
                        Picasso.with(MembersActivity.this).load(image).placeholder(R.drawable.profile).into(NavProfileImage);
                    }
                }
//COULD BE IN THE WRONG PLACE
                progressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


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

        //mAuth = FirebaseAuth.getInstance();


//Recycler to get members from the firebase database

        recyclerView = findViewById(R.id.membersrecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        arrayList = new ArrayList<DataSetFire>();

        currentUserID = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        databaseReference = mUserDatabase.child(currentUserID).child("Added_members");
        databaseReference.keepSynced(true);
        options = new FirebaseRecyclerOptions.Builder<DataSetFire>().setQuery(databaseReference,DataSetFire.class).build();
        mAuth = FirebaseAuth.getInstance();

        adapter = new FirebaseRecyclerAdapter<DataSetFire, FirebaseViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FirebaseViewHolder firebaseViewHolder, int i, @NonNull final DataSetFire model) {

                Picasso.with(MembersActivity.this).load(model.getProfileimage()).placeholder(R.drawable.profile).into(firebaseViewHolder.memberDp);

                firebaseViewHolder.memberUsername.setText(model.getUsername());
                firebaseViewHolder.memberName.setText(model.getFullname());
                firebaseViewHolder.removeMember.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //int pos = (int)v.getTag();
                        removemember(model);
                    }
                });

                firebaseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MembersActivity.this, AddmemberActivity.class);
                        startActivity(intent);

                    }
                });

            }

            @NonNull
            @Override
            public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                return new FirebaseViewHolder(LayoutInflater.from(MembersActivity.this).inflate(R.layout.membersrow,viewGroup,false));
            }
        };


        recyclerView.setAdapter(adapter);


        addMemberBtn = (Button) findViewById(R.id.addMemberbtn);
        addMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addmemberIntent = new Intent(MembersActivity.this,AddmemberActivity.class);
            startActivity(addmemberIntent);
            }
        });

    }

    private void removemember(DataSetFire model) {
        Query removeUname = databaseReference.orderByChild("username").equalTo(model.getUsername());

        removeUname.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot foodSnapshot: dataSnapshot.getChildren()) {
                    foodSnapshot.getRef().removeValue();
                    Toast.makeText(MembersActivity.this, ""+model.getUsername() + " removed from members", Toast.LENGTH_SHORT).show();
                   /* Intent refresh = new Intent(context, CartActivity.class);
                    context.startActivity(refresh);//Start the same Activity
                    ((Activity)context).recreate();

//the CartActivity doesnt remove the item soon enaf
/need to refresh
*/
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(MembersActivity.this, LoginActivity.class);
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

        Intent testsIntent = new Intent(MembersActivity.this, TestsActivity.class);
        startActivity(testsIntent);
        finish();
    }

    private void SendUserToProgressActivity() {
        Intent progressIntent = new Intent(MembersActivity.this, ProgressActivity.class);
        startActivity(progressIntent);
        finish();
    }

    private void SendUserToProfileActivity() {
        Intent profileIntent = new Intent(MembersActivity.this, ProfileActivity.class);
        startActivity(profileIntent);
        finish();
    }

    private void SendUserToMembersActivity() {

        Intent membersIntent = new Intent(MembersActivity.this, MembersActivity.class);
        startActivity(membersIntent);
        finish();
    }

    private void SendUserToDevicesActivity() {

        Intent testsIntent = new Intent(MembersActivity.this, DevicesActivity.class);
        startActivity(testsIntent);
        finish();
    }

    private void SendUserToMainActivity() {

        Intent mainIntent = new Intent(MembersActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }

}

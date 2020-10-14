package com.example.remedi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.remedi.Devices.DevicesActivity;
import com.example.remedi.members.MembersActivity;
import com.example.remedi.progress.ProgressActivity;
import com.example.remedi.signup_in_setup.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class ProfileActivity extends AppCompatActivity {


    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef,CurrentUserRef,allUsersRef;
    String currentUserID;
    final static int Gallery_Pick = 1;
    private static final String TAG = "MainActivity";
    private StorageReference UserProfileImageRef;

    private Button update_info;
    private CircleImageView NavProfileImage;
    private TextView NavProfileUserName;
    private CircleImageView profileimage;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private ProgressDialog loadingBar;
    private EditText profileUsername,profilefullname,profilecontact,profilegender,profiledob;
    private TextView profilemembers,profiledevices;

    String username;
    String fullname;
    String DOB;
    String phone;
    String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile);

            Toolbar mToolbar = (Toolbar) findViewById(R.id.profile_page_toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle("Profile");

        loadingBar = new ProgressDialog(this);
        update_info = (Button) findViewById(R.id.update_information_button);
        profileUsername = (EditText) findViewById(R.id.profile_username);
            profilefullname = (EditText) findViewById(R.id.profile_fullname);
            profilecontact = (EditText) findViewById(R.id.profile_contact);
            profilegender = (EditText) findViewById(R.id.profile_gender);
            profiledob = (EditText) findViewById(R.id.profile_DOB);
            profilemembers = (TextView) findViewById(R.id.profile_members);
            profiledevices = (TextView) findViewById(R.id.profile_devices);
             profileimage = (CircleImageView) findViewById(R.id.profile_image);

       username = profileUsername.getText().toString();
        fullname = profilefullname.getText().toString();
        DOB = profiledob.getText().toString();
        phone = profilecontact.getText().toString();
        gender = profilegender.getText().toString();

        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");




        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(ProfileActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        currentUserID = mAuth.getCurrentUser().getUid();



        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        CurrentUserRef = UsersRef.child(currentUserID);
        update_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                allUsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
                allUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        // Toast.makeText(SetupActivity.this, UserName.getText().toString(), Toast.LENGTH_SHORT).show();
                        String inputUsername = profileUsername.getText().toString();

                        if (checkif_usernameexists(inputUsername,dataSnapshot)){
                            allUsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists())
                                    {

                                        if(dataSnapshot.hasChild("username"))
                                        {
                                            String currentusername = dataSnapshot.child("username").getValue().toString();
                                            if (currentusername.equals(inputUsername)){
                                                UpdateAccountInformation();
                                            }
                                            else {
                                                Toast.makeText(ProfileActivity.this, " uname already exists.. please choose another", Toast.LENGTH_SHORT).show();
                                            }
                                        }}
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }
                        else {
                            UpdateAccountInformation();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);
            }
        });

        profiledob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ProfileActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
               Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                profiledob.setText(date);
            }
        };

        RelativeLayout relativeLayout = findViewById(R.id.profilerootlo);
        final ProgressBar progressBar = new ProgressBar(ProfileActivity.this,null,android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayout.addView(progressBar,params);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.hasChild("fullname"))
                    {
                        String fullname = dataSnapshot.child("fullname").getValue().toString();
                        profilefullname.setText(fullname);
                        NavProfileUserName.setText(fullname);

                    }
                    if(dataSnapshot.hasChild("username"))
                    {
                        String username = dataSnapshot.child("username").getValue().toString();
                        profileUsername.setText(username);
                    }
                    if(dataSnapshot.hasChild("contact"))
                    {
                        String contact = dataSnapshot.child("contact").getValue().toString();
                        profilecontact.setText(contact);
                    }
                    if(dataSnapshot.hasChild("gender"))
                    {
                        String gender = dataSnapshot.child("gender").getValue().toString();
                        profilegender.setText(gender);
                    }
                    if(dataSnapshot.hasChild("dob"))
                    {
                        String dob = dataSnapshot.child("dob").getValue().toString();
                        profiledob.setText(dob);
                    }
                    if(dataSnapshot.hasChild("members"))
                    {
                        String member = dataSnapshot.child("members").getValue().toString();
                        profilemembers.setText(member);
                    }
                    if(dataSnapshot.hasChild("devices"))
                    {
                        String devices = dataSnapshot.child("devices").getValue().toString();
                        profiledevices.setText(devices);
                    }
                    if(dataSnapshot.hasChild("profileimage"))
                    {
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.profile).into(profileimage);
                        Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.profile).into(NavProfileImage);
                    }

                }

                progressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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


    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(ProfileActivity.this, LoginActivity.class);
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

        Intent testsIntent = new Intent(ProfileActivity.this, TestsActivity.class);
        startActivity(testsIntent);
        finish();
    }

    private void SendUserToProgressActivity() {
        Intent progressIntent = new Intent(ProfileActivity.this, ProgressActivity.class);
        startActivity(progressIntent);
        finish();
    }

    private void SendUserToProfileActivity() {
        Intent profileIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
        startActivity(profileIntent);
        finish();
    }

    private void SendUserToMembersActivity() {

        Intent membersIntent = new Intent(ProfileActivity.this, MembersActivity.class);
        startActivity(membersIntent);
        finish();
    }

    private void SendUserToDevicesActivity() {

        Intent testsIntent = new Intent(ProfileActivity.this, DevicesActivity.class);
        startActivity(testsIntent);
        finish();
    }



    public boolean checkif_usernameexists(String username1,DataSnapshot dataSnapshot1){
        DataSetFire memusername = new DataSetFire();
        for (DataSnapshot ds : dataSnapshot1.getChildren()){
            memusername.setUsername(ds.getValue(DataSetFire.class).getUsername());
            if (memusername.getUsername().equals(username1)){

          //some glitch when you try changing username

               return true;
            }
        }

        return false;
    }

    private void SendUserToMainActivity() {

        Intent mainIntent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_Pick && resultCode==RESULT_OK && data!=null)
        {
            Uri ImageUri = data.getData();

            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK)
            {
                Uri resultUri = result.getUri();
                loadingBar.setTitle("Profile Image");
                loadingBar.setMessage("Please wait, while we updating your profile image...");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);



                StorageReference filePath = UserProfileImageRef.child(currentUserID + ".jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task)
                    {
                        if(task.isSuccessful())
                        {

                            Task<Uri> result = task.getResult().getMetadata().getReference().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String downloadUrl =uri.toString();

                                    CurrentUserRef.child("profileimage").setValue(downloadUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if(task.isSuccessful())
                                                    {

                                                        Intent selfIntent = new Intent(ProfileActivity.this, ProfileActivity.class);

                                                        startActivity(selfIntent);
                                                        Toast.makeText(ProfileActivity.this, "Profile Image stored to Firebase Database Successfully...", Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();
                                                    }
                                                    else
                                                    {
                                                        String message = task.getException().getMessage();
                                                        Toast.makeText(ProfileActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();
                                                    }
                                                }
                                            });

                                }
                            });
                            Toast.makeText(ProfileActivity.this, "Profile Image stored successfully to Firebase storage...", Toast.LENGTH_SHORT).show();


                        }
                    }
                });
            }
            else
            {
                Toast.makeText(this, "Error Occured: Image can not be cropped. Try Again.", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }
    }

    private void UpdateAccountInformation()
    {
        username = profileUsername.getText().toString();
        fullname = profilefullname.getText().toString();
        DOB = profiledob.getText().toString();
        phone = profilecontact.getText().toString();
        String newGender = profilegender.getText().toString();
        gender = newGender;


        if(TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Please write your username...", Toast.LENGTH_SHORT).show();
        } else
        if(TextUtils.isEmpty(fullname))
        {
            Toast.makeText(this, "Please write your full name...", Toast.LENGTH_SHORT).show();
        } else
        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone Number...", Toast.LENGTH_SHORT).show();
        }
        else if (phone.length()!=10){
            Toast.makeText(this, "The phone number you have inserted is invalid", Toast.LENGTH_SHORT).show();


        } else
        if (TextUtils.isEmpty(gender)){
            Toast.makeText(this, "Please select your gender...", Toast.LENGTH_SHORT).show();

        }else
            if (!(TextUtils.equals(gender,"female")||TextUtils.equals(gender,"male")||TextUtils.equals(gender,"Male")||TextUtils.equals(gender,"Female"))){
                Toast.makeText(this, "Please specify if you are male or female", Toast.LENGTH_SHORT).show();

            }
            else

        if (TextUtils.isEmpty(DOB)){
            Toast.makeText(this, "Please select your date of birth...", Toast.LENGTH_SHORT).show();

        }
        else
        {
            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please wait, while we are updating your data...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();
            userMap.put("username", username);
            userMap.put("fullname", fullname);
            userMap.put("contact",phone);
            userMap.put("gender", gender);
            userMap.put("dob", DOB);
            CurrentUserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(ProfileActivity.this, "Your profile has been updated", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message =  task.getException().getMessage();
                        Toast.makeText(ProfileActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    public void tomembersactivity(View view) {
        Intent memberActivity = new Intent(ProfileActivity.this,MembersActivity.class);
startActivity(memberActivity);
    }
    public void todevicesactivity(View view) {
        Intent dev = new Intent(ProfileActivity.this,DevicesActivity.class);
        startActivity(dev);
    }
}

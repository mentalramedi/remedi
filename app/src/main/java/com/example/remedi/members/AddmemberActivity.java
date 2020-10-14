package com.example.remedi.members;

import
        androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.remedi.AdapterClass;
import com.example.remedi.DataSetFire;
import com.example.remedi.FirebaseViewHolder;
import com.example.remedi.ProfileActivity;
import com.example.remedi.R;
import com.example.remedi.RecyclerTouchListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class AddmemberActivity extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView mResultList;
    private ArrayList<DataSetFire> mlist;
    private FirebaseRecyclerAdapter<DataSetFire, FirebaseViewHolder> adapter;
    private DatabaseReference mUserDatabase,addedMembersDB,select;
    String currentUserID;
    private FirebaseAuth mAuth;
    DataSetFire addedmem;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmember);


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        addedMembersDB = mUserDatabase.child(currentUserID).child("Added_members");
        select = addedMembersDB.getRef().child("username");


        //addedMembersDB = FirebaseDatabase.getInstance().getReference().child("Added_members");
        mResultList = (RecyclerView) findViewById(R.id.result_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mResultList.setLayoutManager(manager);
        mResultList.setHasFixedSize(true);
        searchView = (SearchView) findViewById(R.id.searchView);
        addedmem = new DataSetFire();

        mResultList.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mResultList, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                addedMembersDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       //Toast.makeText(AddmemberActivity.this, dataSnapshot.getValue().toString(), Toast.LENGTH_LONG).show();
                        if (checkif_usernameexists(mlist.get(position).getUsername(),dataSnapshot)){

                            Toast.makeText(AddmemberActivity.this, "This user is already in your list", Toast.LENGTH_SHORT).show();


                        } else {

                            String fullname = mlist.get(position).getFullname();
                            String username = mlist.get(position).getUsername();
                            String profileimage = mlist.get(position).getProfileimage();
                            //Toast.makeText(AddmemberActivity.this, username, Toast.LENGTH_SHORT).show();

                            addedmem.setUsername(username);
                            addedmem.setFullname(fullname);
                            addedmem.setProfileimage(profileimage);
                           // String alreadyselected = dataSnapshot.child("username").getValue().toString();

                            //Toast.makeText(AddmemberActivity.this,alreadyselected, Toast.LENGTH_SHORT).show();
                            addedMembersDB.push().setValue(addedmem);

                           Toast.makeText(AddmemberActivity.this, "Successful.", Toast.LENGTH_LONG).show();


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


/*
                postRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            if (data.child(busNum).exists()) {
                                //do ur stuff
                            } else {
                                //do something if not exists
                            }
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                /*
                    loadingBar.setTitle("Saving Information");
                    loadingBar.setMessage("Please wait, while we are creating your new Account...");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(true);
*/
                //HashMap userMap = new HashMap();
                //userMap.put("fullname",fullname);
                //userMap.put("username",username);
                // userMap.put("country", country);
               // userMap.put("price",price);
/*
                addedMembersDB.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task)
                    {
                        if(task.isSuccessful())
                        {
                            //SendUserToMainActivity();
                            Toast.makeText(AddmemberActivity.this, "Successful.", Toast.LENGTH_LONG).show();
                            //loadingBar.dismiss();
                        }
                        else
                        {
                            String message =  task.getException().getMessage();
                            Toast.makeText(AddmemberActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                            //loadingBar.dismiss();
                        }
                    }
                });*/
            }


            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        }

    public boolean checkif_usernameexists(String membername,DataSnapshot dataSnapshot){
        DataSetFire dataSetmembername = new DataSetFire();
        for (DataSnapshot ds : dataSnapshot.getChildren()){
            Log.d(TAG, "checkif_foodalreadyinCart: Datasnapshot"+ ds);
            dataSetmembername.setUsername(ds.getValue(DataSetFire.class).getUsername());
            if (dataSetmembername.getUsername().equals(membername)){

                //Toast.makeText(context, "The stuff exists", Toast.LENGTH_SHORT).show();
                return true;
            }
        }



        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        RelativeLayout relativeLayout = findViewById(R.id.addmemrootlo);
        final ProgressBar progressBar = new ProgressBar(AddmemberActivity.this,null,android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayout.addView(progressBar,params);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        if (mUserDatabase != null){
            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()){

                        mlist = new ArrayList<>();

                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            if (ds.getKey().equals(currentUserID)){

                            } else {

                                mlist.add(ds.getValue(DataSetFire.class));
                                AdapterClass adapterClass = new AdapterClass(getApplicationContext(),mlist);
                                mResultList.setAdapter(adapterClass);

                                progressBar.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            }

                        }


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(AddmemberActivity.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });

        }

        if (searchView!=null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
    }

    private void search(String newText) {
        ArrayList<DataSetFire> myList = new ArrayList<>();
        for (DataSetFire object : mlist){

            if (object.getUsername().toLowerCase().contains(newText.toLowerCase())){
                myList.add(object);
            }

        }
        AdapterClass adapterClass = new AdapterClass(getApplicationContext(),myList);
        mResultList.setAdapter(adapterClass);
    }
}


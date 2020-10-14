package com.example.remedi.progress;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remedi.DataSetFire;
import com.example.remedi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MeProgressFragment extends Fragment {
    private RecyclerView mResultList;
        String currentuserID;
private FirebaseAuth firebaseAuth;
    private ArrayList<DataSetFire> mlist;
    private DatabaseReference mUserDatabase;
    @Override
    public void onStart() {
        super.onStart();


        RelativeLayout relativeLayout = getView().findViewById(R.id.mefragrootlo);
        final ProgressBar progressBar = new ProgressBar(getContext(),null,android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayout.addView(progressBar,params);
        progressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        if (mUserDatabase != null){
            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()){

                        mlist = new ArrayList<>();

                        for (DataSnapshot ds : dataSnapshot.getChildren()){


                                mlist.add(ds.getValue(DataSetFire.class));
                                MeProgressAdapter adapterClass = new MeProgressAdapter(getActivity().getApplicationContext(),mlist);
                                mResultList.setAdapter(adapterClass);


                        }


                    }
                    progressBar.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(getContext(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(getContext(),"Database is null", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        }
    }

    public static MeProgressFragment newInstance(int index) {
        MeProgressFragment fragment = new MeProgressFragment();


      //  Bundle bundle = new Bundle();
       // bundle.putInt(ARG_SECTION_NUMBER, index);
       // fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);*/
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.fragment_me, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        currentuserID = firebaseAuth.getCurrentUser().getUid();
         mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentuserID).child("progress");
        mResultList = (RecyclerView) root.findViewById(R.id.me_progress_recycler);

        return root;
    }
}

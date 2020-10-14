package com.example.remedi.testCategories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.remedi.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class QuestionnairesActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private static final int QUESTIONNAIRE_REQUEST = 2018;
    Button resultButton;

    private EditText questionare;
    private Button submit;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionaire);

        /*
        submit = (Button) findViewById(R.id.submitbtn);
        questionare = (EditText) findViewById(R.id.qnET);
        mAuth = FirebaseAuth.getInstance();
        String currentUserID = mAuth.getCurrentUser().getUid();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  currentDateTimeString = DateFormat.getDateTimeInstance()
                        .format(new Date());
            }
        });*/
    }
}

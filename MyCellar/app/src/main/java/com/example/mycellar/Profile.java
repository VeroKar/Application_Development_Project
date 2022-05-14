package com.example.mycellar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    TextView textViewUserName;
    Button buttonLogOut, buttonTastedCollection, buttonUntastedCollection, buttonSuggestions, buttonShowLocation;
    private FirebaseUser user;
    private DatabaseReference reference;
    //we need this since each user has an ID inside the Firebase database
    private String userId;

    private static final String FILE_NAME = "myFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textViewUserName = (TextView) findViewById(R.id.welcomeTextView);
        buttonLogOut = (Button) findViewById(R.id.logOutButton);
        buttonTastedCollection = (Button) findViewById(R.id.tastedCollectionButton);
        buttonUntastedCollection = (Button) findViewById(R.id.untastedCollectionButton);
        buttonSuggestions = (Button) findViewById(R.id.wineSuggestionsButton);
        buttonShowLocation = (Button) findViewById(R.id.locationButton);
        user = FirebaseAuth.getInstance().getCurrentUser();
        //Gets a DatabaseReference for the database root node.
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();

        buttonLogOut.setOnClickListener(this);
        buttonTastedCollection.setOnClickListener(this);
        buttonUntastedCollection.setOnClickListener(this);
        buttonSuggestions.setOnClickListener(this);
        buttonShowLocation.setOnClickListener(this);

        //Welcoming message
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Create a user object
                User user = snapshot.getValue(User.class);
                //If the user exists, set its username text
                if (user != null) {
                    String name = user.username;
                    textViewUserName.setText("Welcome, " + name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.logOutButton:
                    //Sign out the user
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(Profile.this, MainActivity.class));
                    break;
                case R.id.tastedCollectionButton:
                    Intent intent = (new Intent(Profile.this, Collection.class));
                    String tasted = "tasted";
                    intent.putExtra("collectionType", tasted);
                    startActivity(intent);
                    break;
                case R.id.untastedCollectionButton:
                    Intent intent2 = (new Intent(Profile.this, Collection.class));
                    String untasted = "untasted";
                    intent2.putExtra("collectionType", untasted);
                    startActivity(intent2);
                    break;
                case R.id.locationButton:
                    startActivity(new Intent(Profile.this, GoogleLocation.class));
                    break;
                case R.id.wineSuggestionsButton:
                    startActivity(new Intent(Profile.this, WineSuggestionsActivity.class));
                    break;
            }
        }
}
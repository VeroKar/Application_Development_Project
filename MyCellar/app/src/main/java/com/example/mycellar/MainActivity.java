package com.example.mycellar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Name for our file
    private static final String FILE_NAME = "myFile";
    private TextView register;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;
    private CheckBox checkBoxRememberMe;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the NotificationChannel
        // the NotificationChannel class is new and not in the support library
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Notification", " My Notification Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        register = (TextView) findViewById(R.id.registerTextView);
        signIn = (Button) findViewById(R.id.signInButton);
        editTextEmail = (EditText) findViewById(R.id.emailEditText);
        editTextPassword = (EditText) findViewById(R.id.passwordEditText);
        checkBoxRememberMe = (CheckBox)findViewById(R.id.rememberMeCheckBox);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(this);
        signIn.setOnClickListener(this);
        progressBar.setVisibility(View.GONE);

        SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");
        editTextEmail.setText(email);
        editTextPassword.setText(password);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registerTextView:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.signInButton:
                userLogin();
                break;
        }
    }
    //Validate the user before login in
    private void userLogin() {

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        int minLen = 6;

        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < minLen){
            editTextPassword.setError("Password should be of at least six characters");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Redirect to user profile
                if(task.isSuccessful()){
                    //Verify the email
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        //If the email has been verified, check if remember me box is checked
                        //Use StoredDataUsingSharedPreferences to store your data
                        if(checkBoxRememberMe.isChecked()){
                            StoredDataUsingSharedPreferences(email, password);
                        }
                        Intent intent = (new Intent(MainActivity.this, Profile.class));
                        startActivity(intent);
                    } else{
                        //If email is not verified, send email verification by implementing the notification feature
                        user.sendEmailVerification();
                        sendNotificationMessage();
                        progressBar.setVisibility(View.GONE);
                    }

                }else{
                    Toast.makeText(MainActivity.this, "Failed to login. Please check your credentials", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        }

    //Send notification to check verify email account
    private void sendNotificationMessage() {
        //Build a notification by implementing the builder
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(MainActivity.this, "Notification");
        builder.setContentTitle("New Notification");
        builder.setContentText("Check your email to verify your account");
        builder.setSmallIcon(R.drawable.ic_notifications);
        builder.setAutoCancel(true);

        Intent notificationIntent = new Intent(this, Profile.class);

        PendingIntent contentIntent =
                PendingIntent.getActivity(this,0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        // notification Id is an unique int for each notification that must be defined
        manager.notify(0, builder.build());
    }

    private void StoredDataUsingSharedPreferences(String email, String password) {
        //MODE_PRIVATE no other app can change our preference
        //With editor, we will save the preferences
        SharedPreferences.Editor editor = getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }
}
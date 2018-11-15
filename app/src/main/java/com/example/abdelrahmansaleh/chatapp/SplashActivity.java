package com.example.abdelrahmansaleh.chatapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressBar br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splash );
        br=findViewById( R.id.progressBar );
        mAuth= FirebaseAuth.getInstance();
        new Handler(  ).postDelayed( new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser==null){
                    startActivity( new Intent( SplashActivity.this,LoginActivity.class ) );
                }else{
                    startActivity( new Intent( SplashActivity.this,MainActivity.class ) );
                }
                finish();
            }
        }, 2000);
    }
}

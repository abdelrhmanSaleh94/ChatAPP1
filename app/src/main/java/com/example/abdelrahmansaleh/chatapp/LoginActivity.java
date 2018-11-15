package com.example.abdelrahmansaleh.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class LoginActivity extends AppCompatActivity {
    private EditText email,pass;
    private TextView newAcc;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        setTitle( "تسجيل دخول " );
        email=findViewById( R.id.editTextEmail );
        pass=findViewById( R.id.editTextPassword );
        newAcc=findViewById( R.id.textViewNewAcc );

        mAuth = FirebaseAuth.getInstance();

        //new Account
        newAcc.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent( LoginActivity.this,NewAccount.class );
                startActivity( intent );
            }
        } );
    }

    public void logIn(View view) {
        String massage="";
        String email1=email.getText().toString();
        String pass1=pass.getText().toString();
        if (TextUtils.isEmpty( email1 )||TextUtils.isEmpty( pass1 )){
            massage="تأكد من المدخلات ";
            StyleableToast.makeText( LoginActivity.this,massage,R.style.NotFound ).show();
        }
        if (massage.equals( "" )){
            mAuth.signInWithEmailAndPassword( email1,pass1 )
                    .addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                startActivity( new Intent( LoginActivity.this,MainActivity.class ) );
                                StyleableToast.makeText( LoginActivity.this,"مرحبا ",R.style.Welcome ).show();
                                finish();
                            }
                            else {
                                StyleableToast.makeText( LoginActivity.this,"المستخدم غير موجود ",R.style.delete ).show();
                            }
                        }
                    } );

        }
    }
}

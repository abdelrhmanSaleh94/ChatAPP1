package com.example.abdelrahmansaleh.chatapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.HashMap;
import java.util.Map;

public class NewAccount extends AppCompatActivity {
private EditText email,pass,userName,userPhone;
    private FirebaseAuth mAuth;
    private Uri resultUri;
    private String userId;
    private FirebaseDatabase mMyDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_new_account );
        checkPermisson();
        setTitle( "تسجيل حساب جديد " );
        email=findViewById(R.id.editTextEmailNewAcc  );
        pass=findViewById( R.id.editTextPasswordNewAcc );
        userName=findViewById( R.id.editTextNameNewAcc );
        userPhone=findViewById( R.id.editTextPhoneNewAcc );
        mAuth=FirebaseAuth.getInstance();
        mMyDatabase= FirebaseDatabase.getInstance();
    }

    public void save(View view) {
        String massage="";
        String email1=email.getText().toString();
        String pass1=pass.getText().toString();
        final String userName1=userName.getText().toString();
        final String userPhone1=userPhone.getText().toString();
        final String userImage1="https://firebasestorage.googleapis.com/v0/b/chatapp-84ca5.appspot.com/o/deffultes_profile-pictures.png?alt=media&token=ef6f3e33-b950-4ea9-a890-5d1ee8b3d084";
        if (TextUtils.isEmpty( email1 )||TextUtils.isEmpty( pass1 )||TextUtils.isEmpty( userName1 )||TextUtils.isEmpty(userPhone1)){
            massage="تأكد من المدخلات ";
            StyleableToast.makeText( NewAccount.this,massage,R.style.NotFound ).show();
        }else{
            mAuth.createUserWithEmailAndPassword( email1,pass1 )
                    .addOnCompleteListener( NewAccount.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        userId=mAuth.getCurrentUser().getUid();
                        startActivity( new Intent(NewAccount.this ,MainActivity.class ) );
                       DatabaseReference myRef= mMyDatabase.getReference().child( "User" ).child( "Coustmer" ).child( userId );
                        Map userInfo=new HashMap(  );
                        userInfo.put( "name",userName1 );
                        userInfo.put( "phone",userPhone1 );
                        userInfo.put( "profileImage",userImage1 );
                        myRef.updateChildren( userInfo );
                        StyleableToast.makeText( NewAccount.this,"مرحبا "+userId,R.style.Welcome ).show();
                        finish();
                    }else {
                        StyleableToast.makeText( NewAccount.this,"فشل ف الادخال  ",R.style.delete ).show();
                    }
                }
            } );

        }

    }
    private void checkPermisson(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission( Manifest.permission.READ_EXTERNAL_STORAGE )!= PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    !=PackageManager.PERMISSION_GRANTED){
                requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},4 );
            }
        }
}
}

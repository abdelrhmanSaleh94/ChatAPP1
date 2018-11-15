package com.example.abdelrahmansaleh.chatapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {
    private ImageView imageView;
    private EditText phone,userName;
    private Uri resultUri;
    private FirebaseAuth mAuth;
    private String userId;
    private DatabaseReference mMyDatabase;
    private String name1;
    private String phone1;
    private String im;
    public static final int REQUEST_CODE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_edit );
        mAuth=FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();
        mMyDatabase= FirebaseDatabase.getInstance().getReference().child( "User" ).child( "Coustmer" ).child( userId );
        getUser();
        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_close );
        setTitle( "تعديل البيانات " );
        phone=findViewById( R.id.EditTextviewPhone );
        userName=findViewById( R.id.EditTextviewName );
        imageView=findViewById( R.id.imageViewProfileEdit );
        imageView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent( Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult( i,REQUEST_CODE );
            }
        } );



    }
    private  void getUser(){
        mMyDatabase.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()&&dataSnapshot.getChildrenCount()>0){
                    Map<String,Object> map=(Map<String, Object>)dataSnapshot.getValue();
                    if(map.get( "name" )!=null){
                        name1=map.get( "name" ).toString();
                        userName.setText( name1 );
                    }
                    if(map.get( "phone" )!=null){
                        phone1=map.get( "phone" ).toString();
                        phone.setText( phone1 );
                    }
                    if(map.get( "profileImage" )!=null){
                        im=map.get( "profileImage" ).toString();
                        Glide.with(getApplication()).load(im).into(imageView);

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_CODE &&resultCode==RESULT_OK){
            final Uri imageUri=data.getData();
            resultUri=imageUri;
            imageView.setImageURI( resultUri );
        }else{
            Toast.makeText( this, "No Image", Toast.LENGTH_SHORT ).show();
        }
    }
    public void save(View view) {
        name1=userName.getText().toString();
        phone1=phone.getText().toString();
        Map userInfo=new HashMap(  );
        userInfo.put( "name",name1 );
        userInfo.put( "phone",phone1 );
        mMyDatabase.updateChildren( userInfo );
        if (resultUri != null){
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImage").child(userId);
            Bitmap bitmap=null;
            try {
                bitmap= MediaStore.Images.Media.getBitmap( getApplication().getContentResolver(),resultUri );
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream stream=new ByteArrayOutputStream(  );
            bitmap.compress( Bitmap.CompressFormat.JPEG,20,stream );
            byte[] data=stream.toByteArray();
            UploadTask uploadTask=filepath.putBytes( data );
            filepath.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Uri download=uri;
                    Map userInfo=new HashMap(  );
                    userInfo.put( "profileImage",download.toString() );
                    mMyDatabase.updateChildren( userInfo );
                    finish();
                    return;
                }
            } ).addOnFailureListener( new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            } );
        }
        startActivity( new Intent(EditActivity.this,ProfileActivity.class ) );
        finish();
    }
}

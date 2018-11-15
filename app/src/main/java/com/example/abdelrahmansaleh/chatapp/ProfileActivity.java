package com.example.abdelrahmansaleh.chatapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView phone,userName,email;
    private Uri resultUri;
    private FirebaseAuth mAuth;
    private String userId;
    private DatabaseReference mMyDatabase;
    private String name1;
    private String phone1;
    private String im;
    private String email1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_profile );
        mAuth=FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();
        email1=mAuth.getCurrentUser().getEmail();
        mMyDatabase= FirebaseDatabase.getInstance().getReference().child( "User" ).child( "Coustmer" ).child( userId );

        getUser();

        imageView=findViewById( R.id.imageViewProfile );
        phone=findViewById( R.id.textviewPhone );
        userName=findViewById( R.id.textviewName );
        email=findViewById( R.id.textviewEmail );
        email.setText( email1 );


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
                        setTitle( name1+"مرحبا " );
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.edite_user_menu,menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_user:
                startActivity( new Intent( ProfileActivity.this,EditActivity.class ) );
                finish();
                break;
        }
        return super.onOptionsItemSelected( item );
    }


    @Override
    protected void onStart() {
        getUser();
        super.onStart();
    }
}

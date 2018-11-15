package com.example.abdelrahmansaleh.chatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.abdelrahmansaleh.chatapp.Adapter.AdapterChat;
import com.example.abdelrahmansaleh.chatapp.Model.SendNotifications;
import com.example.abdelrahmansaleh.chatapp.Model.User;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    FirebaseUser user ;
    AdapterChat adapter;
    private RecyclerView recyclerView;
    private int count=1;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        recyclerView=findViewById( R.id.rv );
        userList=new ArrayList<User>(  );
        setUp();
        Fresco.initialize(this);
        OneSignal.startInit( this ).init();
        OneSignal.setSubscription( true );
        OneSignal.idsAvailable( new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                FirebaseDatabase.getInstance().getReference().child( "User" ).child( "Coustmer" )
                        .child( mAuth.getUid()).child( "notificationKey" ).setValue( userId );
            }
        } );
        OneSignal.setInFocusDisplaying( OneSignal.OSInFocusDisplayOption.Notification );
    }

    private void setUp() {
        GridLayoutManager manager=new GridLayoutManager( this,count );
        recyclerView.setLayoutManager( manager );
        adapter=new AdapterChat( MainActivity.this,count );
        recyclerView.setAdapter( adapter );
        getAllUser();

    }

    private void getAllUser() {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child( "User" ).child( "Coustmer" );
        reference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot user2:dataSnapshot.getChildren()){
                        getDataSource(user2.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }


    private void getDataSource(final String user1) {
        DatabaseReference db=FirebaseDatabase.getInstance().getReference().child( "User" ).child( "Coustmer" ).child( user1 );
        db.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String name="";
                    String profileImageUrl="";
                    if (dataSnapshot.child( "name" ).getValue()!=null){
                        name=  dataSnapshot.child( "name" ).getValue().toString();
                    }
                    if (dataSnapshot.child( "profileImage" ).getValue()!=null){
                        profileImageUrl=dataSnapshot.child( "profileImage" ).getValue().toString();
                    }
                    if (user1.equals(user.getUid() )){

                    }else {
                        userList.add( new User( name, profileImageUrl, user1 ) );
                    }
                }
                Log.i(  "user1 " ,user1+ " ### "+user.getUid());
                adapter.setData( userList );
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );

    }
    public  void chatUser(User userInfo){
        Intent i=new Intent( this,ChatActivity.class );
        i.putExtra( "UserInfo",userInfo );
        startActivity( i );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu,menu );
        return super.onCreateOptionsMenu( menu );

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:
                startActivity( new Intent( MainActivity.this,ProfileActivity.class ) );
                break;
            case R.id.exit:
                mAuth.signOut();
                startActivity( new Intent( MainActivity.this,LoginActivity.class ) );
                finish();

                break;
        }
        return super.onOptionsItemSelected( item );
    }
}

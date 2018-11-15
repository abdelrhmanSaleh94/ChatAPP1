package com.example.abdelrahmansaleh.chatapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.abdelrahmansaleh.chatapp.Adapter.ChatRoomAdapter;
import com.example.abdelrahmansaleh.chatapp.Adapter.MediaAdapter;
import com.example.abdelrahmansaleh.chatapp.Model.ChatObject;
import com.example.abdelrahmansaleh.chatapp.Model.SendNotifications;
import com.example.abdelrahmansaleh.chatapp.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_INTENT = 200;
    int totalMediaUploaded = 0;
    ArrayList<String> mediaIdList = new ArrayList<>();
    private RecyclerView recyclerView1, mMedia;
    private EditText massages;
    private Button send1;
    private ArrayList<ChatObject> massageList;
    ChatRoomAdapter adapter1;
    DatabaseReference mDatabaseUser, mDatabaseChat;
    private String currentUserID, userAnother, chatId, profImage;
    private DatabaseReference mDatabaseUser2;
    private LinearLayoutManager manager1;
    private ImageView imageview1;
    private RecyclerView.LayoutManager mMediaLayoutManager;
    private MediaAdapter mMediaAdapter;
    private int PICK_FILES_INTENT = 300;
    private String mchat;
    String chatUserTalking;
    String userNotification;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_chat );

        recyclerView1 = findViewById( R.id.chatView );
        recyclerView1.setNestedScrollingEnabled( false );
        recyclerView1.setHasFixedSize( false );
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra( "UserInfo" )) {
            User data = intent.getExtras().getParcelable( "UserInfo" );
            userAnother = data.getUserId();
            profImage = data.getUrl();
            chatUserTalking = data.getName();
            setTitle( chatUserTalking );
        }

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child( "User" ).child( "Coustmer" ).child( currentUserID ).child( "connections" ).child( userAnother ).child( "ChatId" );

        mDatabaseUser2 = FirebaseDatabase.getInstance().getReference().child( "User" ).child( "Coustmer" ).child( userAnother ).child( "connections" ).child( currentUserID ).child( "ChatId" );

        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child( "User" ).child( "Chat" );

        massages = findViewById( R.id.massage );
        massages.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                massages.setWidth( 300 );
            }
        } );
        massageList = new ArrayList<ChatObject>();
        getChatId();
        send1 = findViewById( R.id.buttonsend );
        send1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMassage();
            }
        } );
        imageview1 = findViewById( R.id.imageViewMedia );
        imageview1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        } );
        initializeMedia();
        FirebaseDatabase.getInstance().getReference().child( "User" ).child( "Coustmer" ).child( userAnother )
                .child( "notificationKey" ).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userNotification = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        FirebaseDatabase.getInstance().getReference().child( "User" ).child( "Coustmer" ).child( currentUserID )
                .child( "name" ).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


    }


    public void setUp() {
        manager1 = new LinearLayoutManager( ChatActivity.this );
        manager1.setStackFromEnd( true );
        manager1.setSmoothScrollbarEnabled( true );
        recyclerView1.setLayoutManager( manager1 );
        adapter1 = new ChatRoomAdapter( ChatActivity.this );
        recyclerView1.setAdapter( adapter1 );
        getAllMassage();
    }


    public void sendMassage() {
        mchat = massages.getText().toString();
        String messageId = mDatabaseChat.push().getKey();
        final DatabaseReference newDB = mDatabaseChat.child( chatId ).push();
        final Map map1 = new HashMap<>();
        map1.put( "CreateByUser", currentUserID );
        if (!mchat.isEmpty()) {
            map1.put( "text", mchat );
            new SendNotifications( mchat, name, userNotification );

        }

        if (!mediaUriList.isEmpty()) {
            for (String mediaUri : mediaUriList) {
                String mediaId = newDB.child( "media" ).push().getKey();
                mediaIdList.add( mediaId );
                final StorageReference filePath = FirebaseStorage.getInstance().getReference().child( "chat" ).child( chatId ).child( messageId ).child( mediaId );
                UploadTask uploadTask = filePath.putFile( Uri.parse( mediaUri ) );

                uploadTask.addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                map1.put( "/media/" + mediaIdList.get( totalMediaUploaded ) + "/", uri.toString() );
                                Log.i( "me", "onSuccess2: " + mediaIdList.get( totalMediaUploaded ) );
                                totalMediaUploaded++;
                                if (totalMediaUploaded == mediaUriList.size())
                                    updateDatabaseWithNewMessage( newDB, map1 );
                            }

                        } );
                    }
                } );
            }
            new SendNotifications( mchat + "Image", name, userNotification );

        } else {
            if (!mchat.isEmpty())
                updateDatabaseWithNewMessage( newDB, map1 );

        }
    }

    private void updateDatabaseWithNewMessage(DatabaseReference newMessageDb, Map newMessageMap) {
        newMessageDb.updateChildren( newMessageMap );
        massages.setText( null );
        mediaUriList.clear();
        mediaIdList.clear();
        totalMediaUploaded = 0;
        mMediaAdapter.notifyDataSetChanged();

    }


    public void getChatId() {
        mDatabaseUser.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    chatId = dataSnapshot.getValue().toString();

                } else {

                    chatId = mDatabaseUser.push().getKey();
                    mDatabaseUser.setValue( chatId );
                }
                mDatabaseUser2.setValue( chatId );
                setUp();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }


    private void getAllMassage() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child( "User" ).child( "Chat" ).child( chatId );
        db.addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getRes( dataSnapshot.getKey() );
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getRes( dataSnapshot.getKey() );

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                getRes( dataSnapshot.getKey() );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );


    }

    private void getRes(final String key) {
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference().child( "User" ).child( "Chat" ).child( chatId ).child( key );
        db.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String massg = "";
                    String currentUser = "";
                    ArrayList<String> mediaUrList = new ArrayList<>();

                    if (dataSnapshot.child( "CreateByUser" ).getValue() != null) {
                        currentUser = dataSnapshot.child( "CreateByUser" ).getValue().toString();
                    }
                    if (dataSnapshot.child( "text" ).getValue() != null) {
                        massg = dataSnapshot.child( "text" ).getValue().toString();

                    }
                    if (dataSnapshot.child( "media" ).getChildrenCount() > 0) {
                        for (DataSnapshot snapshot : dataSnapshot.child( "media" ).getChildren())
                            mediaUrList.add( snapshot.getValue().toString() );

                    }
                    if (massg != null && currentUser!=null) {
                        boolean userCheck = false;
                        if (currentUser.equals( currentUserID )) {
                            userCheck = true;
                        }
                        massageList.add( new ChatObject( massg, userCheck, mediaUrList ) );
                        adapter1.setData( massageList );
                        adapter1.notifyDataSetChanged();
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }

    ArrayList<String> mediaUriList = new ArrayList<>();

    private void initializeMedia() {
        mediaUriList = new ArrayList<>();
        mMedia = findViewById( R.id.mediaList );
        mMedia.setNestedScrollingEnabled( false );
        mMedia.setHasFixedSize( false );
        mMediaLayoutManager = new LinearLayoutManager( getApplicationContext(), LinearLayout.HORIZONTAL, false );
        mMedia.setLayoutManager( mMediaLayoutManager );
        mMediaAdapter = new MediaAdapter( getApplicationContext(), mediaUriList );
        mMedia.setAdapter( mMediaAdapter );
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType( "image/*" );
        intent.putExtra( Intent.EXTRA_ALLOW_MULTIPLE, true );
        intent.setAction( intent.ACTION_GET_CONTENT );
        startActivityForResult( Intent.createChooser( intent, "Select Picture(s)" ), PICK_IMAGE_INTENT );

    }

    private void openFiles() {
        Intent intent = new Intent();
        intent.setType( "application/pdf" );
        intent.putExtra( Intent.EXTRA_ALLOW_MULTIPLE, true );
        intent.setAction( intent.ACTION_OPEN_DOCUMENT );
        intent.addCategory( Intent.CATEGORY_OPENABLE );
        startActivityForResult( Intent.createChooser( intent, "Select File(s)" ), PICK_FILES_INTENT );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_INTENT) {
                if (data.getClipData() == null) {
                    mediaUriList.add( data.getData().toString() );
                } else {
                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        mediaUriList.add( data.getClipData().getItemAt( i ).getUri().toString() );
                    }
                }

            }

            mMediaAdapter.notifyDataSetChanged();
        }
    }
}

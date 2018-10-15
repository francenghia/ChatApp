package com.example.franc.chatapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RequestFriendsActivity extends AppCompatActivity {
    Button btnRequestFriends, btnDecline;
    String CURRENT_STATE,receiveUser,currentUser ;
    DatabaseReference Friends, FriendsRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_friends);

        receiveUser = getIntent().getExtras().get("receiveUser").toString();
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Friends = FirebaseDatabase.getInstance().getReference("Friends");
        FriendsRequest = FirebaseDatabase.getInstance().getReference("FriendsRequest");
        btnRequestFriends = findViewById(R.id.btnRequestFriends);
        btnDecline = findViewById(R.id.btnDecline);

        CURRENT_STATE = "not_friend";

        btnDecline.setVisibility(View.INVISIBLE);
        btnDecline.setEnabled(false);
        if (!receiveUser.equals(currentUser)) {
            btnRequestFriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnRequestFriends.setEnabled(false);
                    if(CURRENT_STATE.equals("not_friend")){
                        SentRequestFriend();
                    }
                    if(CURRENT_STATE.equals("request_sent")){
                        CancleRequest();
                    }
                    
                }
            });

        } else {
            btnRequestFriends.setVisibility(View.INVISIBLE);
            btnDecline.setVisibility(View.INVISIBLE);
        }


        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        
        ChangeButton();
    }

    private void ChangeButton() {
        FriendsRequest.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(receiveUser)){
                    String type = dataSnapshot.child(receiveUser).child("request_type").getValue().toString();
                    if(type.equals("sent")){

                    }
                    else if(type.equals("receviced")){

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void CancleRequest() {
        FriendsRequest.child(currentUser).child(receiveUser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FriendsRequest.child(receiveUser).child(currentUser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                btnRequestFriends.setEnabled(true);
                                btnRequestFriends.setText("Request Friend");
                                CURRENT_STATE ="not_friend";
                                btnDecline.setVisibility(View.INVISIBLE);
                                btnDecline.setEnabled(false);
                            }
                        }
                    });
                }
            }
        });
    }


    private void SentRequestFriend() {
        FriendsRequest.child(currentUser).child(receiveUser)
                .child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FriendsRequest.child(receiveUser).child(currentUser)
                            .child("request_type").setValue("receviced").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                btnRequestFriends.setEnabled(true);
                                btnRequestFriends.setText("Cancle Request Friends");
                                CURRENT_STATE = "request_sent";
                                btnDecline.setVisibility(View.VISIBLE);
                                btnDecline.setEnabled(false);
                            }
                        }
                    });
                }
            }
        });
    }
}

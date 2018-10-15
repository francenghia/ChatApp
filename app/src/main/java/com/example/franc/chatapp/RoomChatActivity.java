package com.example.franc.chatapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.franc.chatapp.Adapter.ListUserViewHolder;
import com.example.franc.chatapp.Common.ItemOnClickListener;
import com.example.franc.chatapp.Model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RoomChatActivity extends AppCompatActivity {

    RecyclerView recyclerViewl;
    FirebaseAuth auth;
    DatabaseReference mUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_chat);

        mUser = FirebaseDatabase.getInstance().getReference("User");
        recyclerViewl = findViewById(R.id.recyclerView);
        //recyclerViewl.setHasFixedSize(true);
        recyclerViewl.setLayoutManager(new LinearLayoutManager(this));

        LoadUser();

    }

    private void LoadUser() {
        FirebaseRecyclerAdapter<User, ListUserViewHolder> adapter = new FirebaseRecyclerAdapter<User, ListUserViewHolder>(
                User.class,
                R.layout.item_user,
                ListUserViewHolder.class,
                mUser
        ) {
            @Override
            protected void populateViewHolder(final ListUserViewHolder viewHolder, User model, int position) {
                final String userId=getRef(position).getKey().toString();

                viewHolder.txtName.setText(model.getName());
                viewHolder.txtEmail.setText(model.getEmail());

                viewHolder.setItemOnClickListener(new ItemOnClickListener() {
                    @Override
                    public void OnClick(View view, int position, boolean longClick) {

                        AlertDialog.Builder dialog = new AlertDialog.Builder(RoomChatActivity.this);
                        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_request_friends,null);
                        TextView txtRequestFriends = v.findViewById(R.id.txtRequestFriends);
                        TextView txtChat = v.findViewById(R.id.txtChat);
                        txtRequestFriends.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent= new Intent(RoomChatActivity.this,RequestFriendsActivity.class);
                                intent.putExtra("receiveUser",userId);
                                startActivity(intent);
                            }
                        });
                        txtChat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent= new Intent(RoomChatActivity.this,ChatActivity.class);
                                intent.putExtra("receiveUser",userId);
                                startActivity(intent);
                            }
                        });
                        dialog.setView(v);
                        dialog.show();

                    }
                });
            }
        };
        recyclerViewl.setAdapter(adapter);
    }
}

package com.example.franc.chatapp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.franc.chatapp.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    Button btnLogin, btnRegister;
    FirebaseAuth auth;
    DatabaseReference mUser;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/DancingScript-Regular.ttf")
                .setFontAttrId(R.attr.fontPath).build());
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        mUser = FirebaseDatabase.getInstance().getReference("User");

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginWithEmail();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterEmail();
            }
        });

    }

    private void RegisterEmail() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("LOGIN");
        View view = LayoutInflater.from(this).inflate(R.layout.layout_register, null);
        final MaterialEditText edtEmail = view.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = view.findViewById(R.id.edtPassword);
        final MaterialEditText edtName = view.findViewById(R.id.edtName);


        dialog.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                auth.createUserWithEmailAndPassword(
                        edtEmail.getText().toString()
                        , edtPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                User user = new User();
                                user.setEmail(edtEmail.getText().toString());
                                user.setPassword(edtPassword.getText().toString());
                                user.setName(edtName.getText().toString());

                                if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "email", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "password", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (TextUtils.isEmpty(edtName.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "name", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                mUser.child(auth.getCurrentUser().getUid()).setValue(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(MainActivity.this, "Ok", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
        dialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();

    }

    private void LoginWithEmail() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("LOGIN");
        View view = LayoutInflater.from(this).inflate(R.layout.layout_login, null);
        final MaterialEditText edtEmail = view.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = view.findViewById(R.id.edtPassword);

        dialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                auth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Intent intent = new Intent(MainActivity.this,RoomChatActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.setView(view);
        dialog.show();
    }
}

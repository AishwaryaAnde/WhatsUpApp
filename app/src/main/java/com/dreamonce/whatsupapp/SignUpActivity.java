package com.dreamonce.whatsupapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamonce.whatsupapp.Models.Users;
import com.dreamonce.whatsupapp.R;
import com.dreamonce.whatsupapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseDatabase database;
    ProgressDialog dialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        Context context;
        dialog = new ProgressDialog(SignUpActivity.this);
        dialog.setTitle("Creating Account");
        dialog.setMessage("We are Creating your Account...");

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.editNameSignUp.getText().toString().isEmpty()) {
                    binding.editNameSignUp.setError("Enter Name");
                    return;
                }
                if (binding.editEmailSignUp.getText().toString().isEmpty()) {
                    binding.editEmailSignUp.setError("Enter Email");
                    return;
                }
                if (binding.editPasswordSignUp.getText().toString().isEmpty()) {
                    binding.editPasswordSignUp.setError("Enter password");
                    return;
                }
                dialog.show();
                mAuth.createUserWithEmailAndPassword(
                        binding.editEmailSignUp.getText().toString(), binding.editPasswordSignUp.getText().toString()).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                dialog.dismiss();
                                if (task.isSuccessful()) {

                                    Users users = new Users(binding.editNameSignUp.getText().toString(),
                                            binding.editEmailSignUp.getText().toString(),
                                            binding.editPasswordSignUp.getText().toString());

                                    String id = task.getResult().getUser().getUid();
                                    database.getReference().child("Users").child(id).setValue(users);

                                    Toast.makeText(SignUpActivity.this, "User Created Successfuly", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        binding.txtAlready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }
}
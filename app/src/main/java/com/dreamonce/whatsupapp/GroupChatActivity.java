
package com.dreamonce.whatsupapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;

import com.dreamonce.whatsupapp.Adapters.ChatAdapter;
import com.dreamonce.whatsupapp.Models.MessageModel;
import com.dreamonce.whatsupapp.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {

    ActivityGroupChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(GroupChatActivity.this,MainActivity.class);
                startActivity(intent1);
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<MessageModel> msgModel = new ArrayList<>();

        final String senderId = FirebaseAuth.getInstance().getUid();
        binding.txtUserName.setText("Friends Chat");

        final ChatAdapter adapter = new ChatAdapter(msgModel,this);
        binding.grpChatRecyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.grpChatRecyclerView.setLayoutManager(manager);

        database.getReference().child("Group chat")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        msgModel.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            MessageModel model = dataSnapshot.getValue(MessageModel.class);
                            msgModel.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.editMessage.getText().toString().isEmpty()) {
                    binding.editMessage.setError("Enter Name");
                    return;
                }
                final String message = binding.editMessage.getText().toString();
                final MessageModel model = new MessageModel(senderId,message);
                model.setTimestamp(new Date().getTime());
                binding.editMessage.setText("");

                database.getReference().child("Group Chat")
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
            }
        });
    }
}
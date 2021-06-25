package com.dreamonce.whatsupapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dreamonce.whatsupapp.Models.MessageModel;
import com.dreamonce.whatsupapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {

    ArrayList<MessageModel> msgModel;
    Context context;
    String recId;

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public ChatAdapter(ArrayList<MessageModel> msgModel, Context context) {
        this.msgModel = msgModel;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessageModel> msgModel, Context context, String recId) {
        this.msgModel = msgModel;
        this.context = context;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == SENDER_VIEW_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_reciever, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (msgModel.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())) {
            return SENDER_VIEW_TYPE;
        } else {
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel msgModels = msgModel.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("Delete Message")
                        .setMessage("You want to delete this Mssage")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderRoom = FirebaseAuth.getInstance().getUid() + recId;
                                database.getReference().child("chats").child(senderRoom)
                                        .child(msgModels.getMessageID())
                                        .setValue(null);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

                return false;
            }
        });

        if (holder.getClass() == SenderViewHolder.class) {
            ((SenderViewHolder) holder).txtSenderMsg.setText(msgModels.getMessage());
        } else {
            ((ReceiverViewHolder) holder).txtReceiverMsg.setText(msgModels.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return msgModel.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView txtReceiverMsg, txtReceiverTime;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            txtReceiverMsg = (TextView) itemView.findViewById(R.id.txtReceiverMsg);
            txtReceiverTime = (TextView) itemView.findViewById(R.id.txtReceiverTime);
        }
    }


    public class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView txtSenderMsg, txtSenderTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSenderMsg = (TextView) itemView.findViewById(R.id.txtSenderMsg);
            txtSenderTime = (TextView) itemView.findViewById(R.id.txtSenderTime);
        }
    }
}

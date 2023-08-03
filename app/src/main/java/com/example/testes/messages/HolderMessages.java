package com.example.testes.messages;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testes.R;
import com.example.testes.groups.Groups;

public class HolderMessages extends RecyclerView.ViewHolder{

    private TextView messageDate;
    private TextView nameContato;
    private TextView messageText;

    public HolderMessages(@NonNull View itemView) {
        super(itemView);
        messageDate = itemView.findViewById(R.id.messageDate);
        nameContato = itemView.findViewById(R.id.nameContato);
        messageText = itemView.findViewById(R.id.messageText);
    }

    public void bind(Messages mensagens) {
        messageText.setText(mensagens.getText());
        messageDate.setText((CharSequence) mensagens.getTime());
        nameContato.setText(mensagens.getUser_nickname());
    }

}

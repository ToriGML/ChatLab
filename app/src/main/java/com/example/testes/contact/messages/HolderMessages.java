package com.example.testes.contact.messages;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testes.R;

import java.util.Calendar;
import java.util.Date;

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
//        Date time = mensagens.getTime();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(time);
//
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//        if(minute < 10){
//            messageDate.setText(hour + ":0" + minute);
//        }else{
//            messageDate.setText(hour + ":" + minute);
//        }

        nameContato.setText(mensagens.getUser_nickname());
    }

}

package com.example.testes.contact;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testes.R;

public class Holder extends RecyclerView.ViewHolder{

    private TextView nameContato;
    private ImageView imageView;

    public Holder(@NonNull View itemView) {
        super(itemView);
        nameContato = itemView.findViewById(R.id.nameContato);
        imageView = itemView.findViewById(R.id.imagem_contato);
    }

    public void bind(Contact contato) {
        nameContato.setText(contato.getNome());
        imageView.setImageResource(contato.getImagemId());
    }

}

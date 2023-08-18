package com.example.testes.groups;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testes.R;

public class HolderGroups extends RecyclerView.ViewHolder{

    private ImageView imageView;

    public HolderGroups(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imagem_contato);
    }

    public void bind(Groups grupo) {
        imageView.setImageResource(grupo.getImagemId());
    }

}

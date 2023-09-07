package com.example.testes.contact.messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testes.R;

import java.util.List;

public class AdapterMessages extends RecyclerView.Adapter<HolderMessages>{

    private List<Messages> listaItens;

    public AdapterMessages() {
    }

    public AdapterMessages(List<Messages> listaItens) {
        System.out.println("aaaaa"+listaItens);
        this.listaItens = listaItens;
    }

    @NonNull
    @Override
    public HolderMessages onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages, parent, false);
        return new HolderMessages(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMessages holder, int position) {

        holder.bind(listaItens.get(position));
    }

    @Override
    public int getItemCount() {
        return listaItens.size();
    }

    public Messages getItem(int position) {
        return listaItens.get(position);
    }


}

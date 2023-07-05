package com.example.testes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Holder>{

    private List<Contact> listaItens;

    public Adapter(List<Contact> listaItens) {
        this.listaItens = listaItens;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Contact contato = listaItens.get(position);
        holder.bind(contato);
    }

    @Override
    public int getItemCount() {
        return listaItens.size();
    }

}

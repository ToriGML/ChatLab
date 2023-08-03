package com.example.testes.groups;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testes.R;

import java.util.List;

public class AdapterGroups extends RecyclerView.Adapter<HolderGroups>{

    private List<Groups> listaItens;

    public AdapterGroups() {
    }

    public AdapterGroups(List<Groups> listaItens) {
        this.listaItens = listaItens;
    }

    @NonNull
    @Override
    public HolderGroups onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.groups, parent, false);
        return new HolderGroups(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderGroups holder, int position) {
        Groups grupo = listaItens.get(position);
        holder.bind(grupo);
    }

    @Override
    public int getItemCount() {
        return listaItens.size();
    }

    public Groups getItem(int position) {
        return listaItens.get(position);
    }


}

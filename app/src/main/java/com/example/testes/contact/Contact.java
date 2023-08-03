package com.example.testes.contact;

import java.time.LocalTime;

public class Contact {
    private String nome;
    private Integer imageId;

    public Contact(String nome, Integer imageId) {
        this.nome = nome;
        this.imageId = imageId;
    }

    public String getNome() {
        return nome;
    }
    public int getImagemId() {
        return imageId;
    }
}

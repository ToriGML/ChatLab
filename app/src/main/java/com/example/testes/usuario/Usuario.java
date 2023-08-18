package com.example.testes.usuario;

public class Usuario {

    private String id;

    private String email;

    private String imagemUrl;

    public Usuario(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }
}

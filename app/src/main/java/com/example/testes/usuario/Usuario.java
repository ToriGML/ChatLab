package com.example.testes.usuario;

import java.util.Map;

public class Usuario {

    private String id;

    private String email;

    private String imagemUrl;

    public Usuario(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public Usuario(String id, String email, String imagemUrl) {
        this(id, email);
        this.imagemUrl = imagemUrl;
    }

    public Usuario() {
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

    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", imagemUrl='" + imagemUrl + '\'' +
                '}';
    }
}

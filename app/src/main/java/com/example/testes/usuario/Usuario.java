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

    public Usuario(Map<String, Object> data) {
        this.id = (String) data.get("id");
        this.email = (String) data.get("email");
        // Set other fields if necessary
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

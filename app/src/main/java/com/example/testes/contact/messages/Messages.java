package com.example.testes.contact.messages;

import com.example.testes.usuario.Usuario;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public class Messages {
    private String text;
    private Date time;
    private String user_nickname;
    private Usuario user;

    public Messages(String text, Date time, String user_nickname, Usuario user) {
        this.text = text;
        this.time = time;
        this.user_nickname = user_nickname;
        this.user = user;
    }

    public Messages(){}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }


    @Override
    public String toString() {
        return "Messages{" +
                "text='" + text + '\'' +
                ", time=" + time +
                ", user_nickname='" + user_nickname + '\'' +
                '}';
    }
}

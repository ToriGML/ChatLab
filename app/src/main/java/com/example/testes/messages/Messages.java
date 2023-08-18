package com.example.testes.messages;

import java.util.Date;

public class Messages {
    private String text;
    private Date time;
    private String user_nickname;

    public Messages(String text, Date time, String user_nickname) {
        this.text = text;
        this.time = time;
        this.user_nickname = user_nickname;
    }

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
}

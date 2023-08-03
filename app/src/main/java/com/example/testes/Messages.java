package com.example.testes;

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
}

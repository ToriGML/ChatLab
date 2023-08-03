package com.example.testes.groups;

import com.example.testes.Messages;

import java.util.List;

public class Groups {

    private Integer imageId;
    private List<Messages> messagesList;

    public Groups(Integer imageId, List<Messages> messagesList) {
        this.imageId = imageId;
        this.messagesList = messagesList;
    }
    public int getImagemId() {
        return imageId;
    }

    public List<Messages> getMessagesList() {
        return messagesList;
    }

}

package com.example.testes.groups;

import com.example.testes.messages.Messages;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.UUID;

public class Groups {

    private UUID uuid;
    private Integer imageId;
    private List<Messages> messagesList;
    private List<FirebaseUser> users;

    public Groups(UUID uuid, Integer imageId, List<Messages> messagesList, List<FirebaseUser> users) {
        this.uuid = uuid;
        this.imageId = imageId;
        this.messagesList = messagesList;
        this.users = users;
    }

    public boolean verifyUser(String uuid){
        for (FirebaseUser user : this.getUsers()) {
            if (user.getUid() == uuid){
                return true;
            }
        }
        return false;
    }

    public List<FirebaseUser> getUsers() {
        return users;
    }

    public int getImagemId() {
        return imageId;
    }

    public List<Messages> getMessagesList() {
        return messagesList;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public void setMessagesList(List<Messages> messagesList) {
        this.messagesList = messagesList;
    }
}

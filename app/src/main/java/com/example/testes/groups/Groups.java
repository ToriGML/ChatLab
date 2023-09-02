package com.example.testes.groups;

import com.example.testes.contact.messages.Messages;
import com.example.testes.usuario.Usuario;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.UUID;

public class Groups {

    private String uuid;
    private Integer imageId;
    private List<Messages> messagesList;
    private List<Usuario> users;

    public Groups(String uuid, Integer imageId, List<Messages> messagesList, List<Usuario> users) {
        this.uuid = uuid;
        this.imageId = imageId;
        this.messagesList = messagesList;
        this.users = users;
    }

    public Groups(){}

    public List<Usuario> getUsers() {
        return users;
    }

//    public int getImagemId() {
//        return imageId;
//    }


    public void setUsers(List<Usuario> users) {
        this.users = users;
    }

    public List<Messages> getMessagesList() {
        return messagesList;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setMessagesList(List<Messages> messagesList) {
        this.messagesList = messagesList;
    }

    @Override
    public String toString() {
        return "Groups{" +
                "uuid=" + uuid +
                ", imageId=" + imageId +
                ", messagesList=" + messagesList +
                ", users=" + users +
                '}';
    }
}

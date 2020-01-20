package com.aohui.btcorg.entity;

import javax.persistence.*;

@Entity
@Table(name = "message")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String send;
    private String rec_id;
    private String message_id;
    private String statue;

    @Id
    @Column
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
    }

    public String getRec_id() {
        return rec_id;
    }

    public void setRec_id(String rec_id) {
        this.rec_id = rec_id;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }

    @Override
    public String toString() {
        return "MessageEntity{" +
                "id=" + id +
                ", send='" + send + '\'' +
                ", rec_id='" + rec_id + '\'' +
                ", message_id='" + message_id + '\'' +
                ", statue='" + statue + '\'' +
                '}';
    }
}

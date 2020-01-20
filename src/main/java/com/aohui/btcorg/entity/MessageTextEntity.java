package com.aohui.btcorg.entity;

import javax.persistence.*;

@Entity
@Table(name = "messagetext")
public class MessageTextEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String message;
    private String send;
    private String rec_id;
    private String pd_ate;
    private String status;

    public String getRec_id() {
        return rec_id;
    }

    public void setRec_id(String rec_id) {
        this.rec_id = rec_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPd_ate() {
        return pd_ate;
    }

    public void setPd_ate(String pd_ate) {
        this.pd_ate = pd_ate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
    }

    @Override
    public String toString() {
        return "MessageTextEntity{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", send='" + send + '\'' +
                ", rec_id='" + rec_id + '\'' +
                ", pd_ate='" + pd_ate + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

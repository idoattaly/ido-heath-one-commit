package com.dchealth.entity.rare;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/11/9.
 */
@Entity
@Table(name = "Message", schema = "emhbase", catalog = "")
public class Message {
    private String id;
    private String sendId;
    private String recId;
    private String messageId;
    private String status;
    private Timestamp createDate;
    private Timestamp modifyDate;

    @Id
    @Column(name = "id")
    @GenericGenerator(name="generator",strategy = "uuid.hex")
    @GeneratedValue(generator = "generator")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "sendId", nullable = true, length = 64)
    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    @Basic
    @Column(name = "recId", nullable = true, length = 64)
    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    @Basic
    @Column(name = "messageId", nullable = true, length = 64)
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Basic
    @Column(name = "status", nullable = true, length = 2)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "create_date", nullable = false)
    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "modify_date", nullable = false)
    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (id != null ? !id.equals(message.id) : message.id != null) return false;
        if (sendId != null ? !sendId.equals(message.sendId) : message.sendId != null) return false;
        if (recId != null ? !recId.equals(message.recId) : message.recId != null) return false;
        if (messageId != null ? !messageId.equals(message.messageId) : message.messageId != null) return false;
        if (status != null ? !status.equals(message.status) : message.status != null) return false;
        if (createDate != null ? !createDate.equals(message.createDate) : message.createDate != null) return false;
        if (modifyDate != null ? !modifyDate.equals(message.modifyDate) : message.modifyDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (sendId != null ? sendId.hashCode() : 0);
        result = 31 * result + (recId != null ? recId.hashCode() : 0);
        result = 31 * result + (messageId != null ? messageId.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (modifyDate != null ? modifyDate.hashCode() : 0);
        return result;
    }
}

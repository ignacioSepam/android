package com.bitlove.fetlife.nativeapp.model.pojos.fetlife.dbjson;

import com.bitlove.fetlife.nativeapp.model.db.FetLifeDatabase;
import com.bitlove.fetlife.nativeapp.model.pojos.fetlife.json.MessageEntities;
import com.bitlove.fetlife.nativeapp.util.DateUtil;
import com.bitlove.fetlife.nativeapp.util.StringUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONObject;

import java.util.Map;

@Table(database = FetLifeDatabase.class)
public class Message extends BaseModel {

    //Db Only
    @Column
    @PrimaryKey(autoincrement = false)
    @JsonIgnore
    private String clientId;

    @Column
    @JsonIgnore
    private String conversationId;

    @Column
    @JsonIgnore
    private long date;

    @Column
    @JsonIgnore
    private boolean failed;

    @Column
    @JsonIgnore
    private boolean pending;

    @Column
    @JsonIgnore
    private String senderId;

    @Column
    @JsonIgnore
    private String senderNickname;

    //Db and Json
    @Column
    @JsonProperty("body")
    private String body;

    @Column
    @JsonProperty("id")
    private String id;

    @Column
    @JsonProperty("created_at")
    private String createdAt;

    @Column
    @JsonProperty("is_new")
    private boolean newMessage;

    @JsonProperty("member")
    private Member sender;

    @JsonProperty("body_entities")
    private Object bodyEntities;

    @Column
    private String entitiesJson;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
        this.htmlBody = null;
    }

    @JsonIgnore
    private CharSequence htmlBody;

    public CharSequence getHtmlBody() {
        if (htmlBody == null && body != null) {
            MessageEntities messageEntities = StringUtil.getMessageEntities(entitiesJson);
            this.htmlBody = StringUtil.parseMarkedHtmlWithMentions(body.trim(), messageEntities.getMentions());
        }
        return htmlBody;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @JsonIgnore
    public String getConversationId() {
        return conversationId;
    }

    @JsonIgnore
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        if (createdAt != null) {
            try {
                setDate(DateUtil.parseDate(createdAt));
            } catch (Exception e) {
            }
        }
    }

    @JsonIgnore
    public long getDate() {
        return date;
    }

    @JsonIgnore
    public void setDate(long date) {
        this.date = date;
    }

    @JsonIgnore
    public boolean getFailed() {
        return isFailed();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getNewMessage() {
        return isNewMessage();
    }

    @JsonIgnore
    public boolean getPending() {
        return isPending();
    }

    public Member getSender() {
        return sender;
    }

    public void setSender(Member sender) {
        this.sender = sender;
        if (sender != null) {
            setSenderId(sender.getId());
            setSenderNickname(sender.getNickname());
        }
    }

    @JsonIgnore
    public String getSenderId() {
        return senderId;
    }

    @JsonIgnore
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    @JsonIgnore
    public String getSenderNickname() {
        return senderNickname;
    }

    @JsonIgnore
    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    public boolean isFailed() {
        return failed;
    }

    @JsonIgnore
    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public boolean isNewMessage() {
        return newMessage;
    }

    public void setNewMessage(boolean isNew) {
        this.newMessage = isNew;
    }

    public boolean isPending() {
        return pending;
    }

    @JsonIgnore
    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public Object getBodyEntities() {
        return bodyEntities;
    }

    public void setBodyEntities(Object bodyEntities) {
        this.bodyEntities = bodyEntities;
        if (bodyEntities != null) {
            entitiesJson = new JSONObject((Map)bodyEntities).toString();
        }
    }

    public String getEntitiesJson() {
        return entitiesJson;
    }

    public void setEntitiesJson(String entitiesJson) {
        this.entitiesJson = entitiesJson;
    }
}
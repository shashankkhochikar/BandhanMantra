
package com.bandhan.mantra.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActivityLog implements Serializable
{

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("EntityId")
    @Expose
    private Integer entityId;
    @SerializedName("EntityType")
    @Expose
    private String entityType;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("UserId")
    @Expose
    private Object userId;
    @SerializedName("PartnerId")
    @Expose
    private Object partnerId;
    @SerializedName("OperationType")
    @Expose
    private String operationType;
    @SerializedName("ClientId")
    @Expose
    private Object clientId;
    @SerializedName("User")
    @Expose
    private String user;
    @SerializedName("Partner")
    @Expose
    private String partner;
    @SerializedName("Client")
    @Expose
    private String client;
    @SerializedName("EntityName")
    @Expose
    private String entityName;
    private final static long serialVersionUID = -2390316246690903506L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public Object getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Object partnerId) {
        this.partnerId = partnerId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Object getClientId() {
        return clientId;
    }

    public void setClientId(Object clientId) {
        this.clientId = clientId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

}

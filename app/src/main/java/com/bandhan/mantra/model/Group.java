
package com.bandhan.mantra.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Group implements Serializable
{

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("ClientID")
    @Expose
    private Integer clientID;
    @SerializedName("ContactCount")
    @Expose
    private Integer contactCount;
    @SerializedName("Contacts")
    @Expose
    private Object contacts;
    private final static long serialVersionUID = 4340463390777988308L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public Integer getContactCount() {
        return contactCount;
    }

    public void setContactCount(Integer contactCount) {
        this.contactCount = contactCount;
    }

    public Object getContacts() {
        return contacts;
    }

    public void setContacts(Object contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", name).append("clientID", clientID).append("contactCount", contactCount).append("contacts", contacts).toString();
    }

}


package com.bandhan.mantra.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAccessPrivileges implements Serializable
{

    @SerializedName("Groups")
    @Expose
    private Boolean groups;
    @SerializedName("Contacts")
    @Expose
    private Boolean contacts;
    @SerializedName("ImportContacts")
    @Expose
    private Boolean importContacts;
    @SerializedName("Templates")
    @Expose
    private Boolean templates;
    @SerializedName("Users")
    @Expose
    private Boolean users;
    @SerializedName("Locations")
    @Expose
    private Boolean locations;
    @SerializedName("Campaigns")
    @Expose
    private Boolean campaigns;
    @SerializedName("CreditRequests")
    @Expose
    private Boolean creditRequests;
    @SerializedName("Coupons")
    @Expose
    private Boolean coupons;
    @SerializedName("Settings")
    @Expose
    private Boolean settings;
    @SerializedName("Redeems")
    @Expose
    private Boolean redeems;
    @SerializedName("SenderCode")
    @Expose
    private Boolean senderCode;
    private final static long serialVersionUID = -722385257752186682L;

    public Boolean getGroups() {
        return groups;
    }

    public void setGroups(Boolean groups) {
        this.groups = groups;
    }

    public Boolean getContacts() {
        return contacts;
    }

    public void setContacts(Boolean contacts) {
        this.contacts = contacts;
    }

    public Boolean getImportContacts() {
        return importContacts;
    }

    public void setImportContacts(Boolean importContacts) {
        this.importContacts = importContacts;
    }

    public Boolean getTemplates() {
        return templates;
    }

    public void setTemplates(Boolean templates) {
        this.templates = templates;
    }

    public Boolean getUsers() {
        return users;
    }

    public void setUsers(Boolean users) {
        this.users = users;
    }

    public Boolean getLocations() {
        return locations;
    }

    public void setLocations(Boolean locations) {
        this.locations = locations;
    }

    public Boolean getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(Boolean campaigns) {
        this.campaigns = campaigns;
    }

    public Boolean getCreditRequests() {
        return creditRequests;
    }

    public void setCreditRequests(Boolean creditRequests) {
        this.creditRequests = creditRequests;
    }

    public Boolean getCoupons() {
        return coupons;
    }

    public void setCoupons(Boolean coupons) {
        this.coupons = coupons;
    }

    public Boolean getSettings() {
        return settings;
    }

    public void setSettings(Boolean settings) {
        this.settings = settings;
    }

    public Boolean getRedeems() {
        return redeems;
    }

    public void setRedeems(Boolean redeems) {
        this.redeems = redeems;
    }

    public Boolean getSenderCode() {
        return senderCode;
    }

    public void setSenderCode(Boolean senderCode) {
        this.senderCode = senderCode;
    }

}

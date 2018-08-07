
package com.bandhan.mantra.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CampaignsListItem implements Serializable
{

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("RecipientsNumber")
    @Expose
    private String recipientsNumber;
    @SerializedName("RecipientsCount")
    @Expose
    private Integer recipientsCount;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("IsScheduled")
    @Expose
    private Boolean isScheduled;
    @SerializedName("ScheduledDate")
    @Expose
    private String scheduledDate;
    @SerializedName("ScheduledTime")
    @Expose
    private String scheduledTime;
    @SerializedName("IPAddress")
    @Expose
    private String iPAddress;
    @SerializedName("MessageCount")
    @Expose
    private Integer messageCount;
    @SerializedName("RequiredCredits")
    @Expose
    private Integer requiredCredits;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Remark")
    @Expose
    private String remark;
    @SerializedName("ClientId")
    @Expose
    private Integer clientId;
    @SerializedName("CreatedBy")
    @Expose
    private Integer createdBy;
    @SerializedName("GroupId")
    @Expose
    private Integer groupId;
    @SerializedName("GroupName")
    @Expose
    private String groupName;
    @SerializedName("IsUnicode")
    @Expose
    private Boolean isUnicode;
    @SerializedName("LanguageCode")
    @Expose
    private String languageCode;
    @SerializedName("GroupContactCount")
    @Expose
    private Object groupContactCount;
    @SerializedName("ForAllContact")
    @Expose
    private Boolean forAllContact;
    @SerializedName("ConsumedCredits")
    @Expose
    private Integer consumedCredits;
    @SerializedName("CreditsDiffrence")
    @Expose
    private Integer creditsDiffrence;
    @SerializedName("IsReconcile")
    @Expose
    private Boolean isReconcile;
    @SerializedName("ReconcileDate")
    @Expose
    private String reconcileDate;
    @SerializedName("RecurringCampaignId")
    @Expose
    private Object recurringCampaignId;
    @SerializedName("TemplateDTO")
    @Expose
    private Object templateDTO;
    private final static long serialVersionUID = -1253810744212191891L;

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

    public String getRecipientsNumber() {
        return recipientsNumber;
    }

    public void setRecipientsNumber(String recipientsNumber) {
        this.recipientsNumber = recipientsNumber;
    }

    public Integer getRecipientsCount() {
        return recipientsCount;
    }

    public void setRecipientsCount(Integer recipientsCount) {
        this.recipientsCount = recipientsCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getIsScheduled() {
        return isScheduled;
    }

    public void setIsScheduled(Boolean isScheduled) {
        this.isScheduled = isScheduled;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getIPAddress() {
        return iPAddress;
    }

    public void setIPAddress(String iPAddress) {
        this.iPAddress = iPAddress;
    }

    public Integer getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Integer messageCount) {
        this.messageCount = messageCount;
    }

    public Integer getRequiredCredits() {
        return requiredCredits;
    }

    public void setRequiredCredits(Integer requiredCredits) {
        this.requiredCredits = requiredCredits;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Boolean getIsUnicode() {
        return isUnicode;
    }

    public void setIsUnicode(Boolean isUnicode) {
        this.isUnicode = isUnicode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public Object getGroupContactCount() {
        return groupContactCount;
    }

    public void setGroupContactCount(Object groupContactCount) {
        this.groupContactCount = groupContactCount;
    }

    public Boolean getForAllContact() {
        return forAllContact;
    }

    public void setForAllContact(Boolean forAllContact) {
        this.forAllContact = forAllContact;
    }

    public Integer getConsumedCredits() {
        return consumedCredits;
    }

    public void setConsumedCredits(Integer consumedCredits) {
        this.consumedCredits = consumedCredits;
    }

    public Integer getCreditsDiffrence() {
        return creditsDiffrence;
    }

    public void setCreditsDiffrence(Integer creditsDiffrence) {
        this.creditsDiffrence = creditsDiffrence;
    }

    public Boolean getIsReconcile() {
        return isReconcile;
    }

    public void setIsReconcile(Boolean isReconcile) {
        this.isReconcile = isReconcile;
    }

    public String getReconcileDate() {
        return reconcileDate;
    }

    public void setReconcileDate(String reconcileDate) {
        this.reconcileDate = reconcileDate;
    }

    public Object getRecurringCampaignId() {
        return recurringCampaignId;
    }

    public void setRecurringCampaignId(Object recurringCampaignId) {
        this.recurringCampaignId = recurringCampaignId;
    }

    public Object getTemplateDTO() {
        return templateDTO;
    }

    public void setTemplateDTO(Object templateDTO) {
        this.templateDTO = templateDTO;
    }

    @Override
    public String toString() {
        return "CampaignsListItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", recipientsNumber='" + recipientsNumber + '\'' +
                ", recipientsCount=" + recipientsCount +
                ", message='" + message + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", isScheduled=" + isScheduled +
                ", scheduledDate='" + scheduledDate + '\'' +
                ", scheduledTime='" + scheduledTime + '\'' +
                ", iPAddress='" + iPAddress + '\'' +
                ", messageCount=" + messageCount +
                ", requiredCredits=" + requiredCredits +
                ", status='" + status + '\'' +
                ", remark='" + remark + '\'' +
                ", clientId=" + clientId +
                ", createdBy=" + createdBy +
                ", groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", isUnicode=" + isUnicode +
                ", languageCode='" + languageCode + '\'' +
                ", groupContactCount=" + groupContactCount +
                ", forAllContact=" + forAllContact +
                ", consumedCredits=" + consumedCredits +
                ", creditsDiffrence=" + creditsDiffrence +
                ", isReconcile=" + isReconcile +
                ", reconcileDate='" + reconcileDate + '\'' +
                ", recurringCampaignId=" + recurringCampaignId +
                ", templateDTO=" + templateDTO +
                '}';
    }
}

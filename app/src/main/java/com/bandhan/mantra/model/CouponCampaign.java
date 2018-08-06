
package com.bandhan.mantra.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CouponCampaign implements Serializable
{

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("ReceipentNumber")
    @Expose
    private String receipentNumber;
    @SerializedName("RecipientsCount")
    @Expose
    private Integer recipientsCount;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("ExpiresOn")
    @Expose
    private String expiresOn;
    @SerializedName("SendOn")
    @Expose
    private String sendOn;
    @SerializedName("ScheduleTime")
    @Expose
    private String scheduleTime;
    @SerializedName("IsScheduled")
    @Expose
    private Boolean isScheduled;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Remark")
    @Expose
    private String remark;
    @SerializedName("IPAddress")
    @Expose
    private String iPAddress;
    @SerializedName("ClientId")
    @Expose
    private Integer clientId;
    @SerializedName("ReedeemedCount")
    @Expose
    private Integer reedeemedCount;
    @SerializedName("TotalCount")
    @Expose
    private Integer totalCount;
    @SerializedName("CreatedBy")
    @Expose
    private Integer createdBy;
    @SerializedName("GroupId")
    @Expose
    private Integer groupId;
    @SerializedName("Group")
    @Expose
    private Object group;
    @SerializedName("GroupContactCount")
    @Expose
    private Object groupContactCount;
    @SerializedName("ForAllContact")
    @Expose
    private Boolean forAllContact;
    @SerializedName("TemplateDTO")
    @Expose
    private Object templateDTO;
    @SerializedName("MinPurchaseAmount")
    @Expose
    private Integer minPurchaseAmount;
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
    @SerializedName("RequiredCredits")
    @Expose
    private Integer requiredCredits;
    @SerializedName("RecurringCampaignId")
    @Expose
    private Object recurringCampaignId;
    @SerializedName("SuccessCount")
    @Expose
    private Object successCount;
    @SerializedName("FailureCount")
    @Expose
    private Object failureCount;
    @SerializedName("PendingCount")
    @Expose
    private Object pendingCount;
    @SerializedName("UpdatedDate")
    @Expose
    private Object updatedDate;
    @SerializedName("UserName")
    @Expose
    private Object userName;
    private final static long serialVersionUID = -1792056693391640954L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getReceipentNumber() {
        return receipentNumber;
    }

    public void setReceipentNumber(String receipentNumber) {
        this.receipentNumber = receipentNumber;
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

    public String getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(String expiresOn) {
        this.expiresOn = expiresOn;
    }

    public String getSendOn() {
        return sendOn;
    }

    public void setSendOn(String sendOn) {
        this.sendOn = sendOn;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public Boolean getIsScheduled() {
        return isScheduled;
    }

    public void setIsScheduled(Boolean isScheduled) {
        this.isScheduled = isScheduled;
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

    public String getIPAddress() {
        return iPAddress;
    }

    public void setIPAddress(String iPAddress) {
        this.iPAddress = iPAddress;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getReedeemedCount() {
        return reedeemedCount;
    }

    public void setReedeemedCount(Integer reedeemedCount) {
        this.reedeemedCount = reedeemedCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
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

    public Object getGroup() {
        return group;
    }

    public void setGroup(Object group) {
        this.group = group;
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

    public Object getTemplateDTO() {
        return templateDTO;
    }

    public void setTemplateDTO(Object templateDTO) {
        this.templateDTO = templateDTO;
    }

    public Integer getMinPurchaseAmount() {
        return minPurchaseAmount;
    }

    public void setMinPurchaseAmount(Integer minPurchaseAmount) {
        this.minPurchaseAmount = minPurchaseAmount;
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

    public Integer getRequiredCredits() {
        return requiredCredits;
    }

    public void setRequiredCredits(Integer requiredCredits) {
        this.requiredCredits = requiredCredits;
    }

    public Object getRecurringCampaignId() {
        return recurringCampaignId;
    }

    public void setRecurringCampaignId(Object recurringCampaignId) {
        this.recurringCampaignId = recurringCampaignId;
    }

    public Object getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Object successCount) {
        this.successCount = successCount;
    }

    public Object getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(Object failureCount) {
        this.failureCount = failureCount;
    }

    public Object getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(Object pendingCount) {
        this.pendingCount = pendingCount;
    }

    public Object getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Object updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Object getUserName() {
        return userName;
    }

    public void setUserName(Object userName) {
        this.userName = userName;
    }

}

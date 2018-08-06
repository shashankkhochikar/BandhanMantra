
package com.bandhan.mantra.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardDetails implements Serializable
{

    @SerializedName("SMSCredits")
    @Expose
    private Integer sMSCredits;
    @SerializedName("PendingCampaigns")
    @Expose
    private Integer pendingCampaigns;
    @SerializedName("PendingCouponCampaigns")
    @Expose
    private Integer pendingCouponCampaigns;
    @SerializedName("PendingMessage")
    @Expose
    private Integer pendingMessage;
    @SerializedName("ActivityLogs")
    @Expose
    private List<ActivityLog> activityLogs = null;
    @SerializedName("MessageSentCounts")
    @Expose
    private List<MessageSentCount> messageSentCounts = null;
    @SerializedName("Campaigns")
    @Expose
    private List<Campaign> campaigns = null;
    @SerializedName("EcouponCampaigns")
    @Expose
    private List<Object> ecouponCampaigns = null;
    @SerializedName("CreditRequests")
    @Expose
    private List<Object> creditRequests = null;
    @SerializedName("Templates")
    @Expose
    private List<Template> templates = null;
    @SerializedName("RedeemCoupons")
    @Expose
    private List<Object> redeemCoupons = null;
    private final static long serialVersionUID = -1718242698800593720L;

    public Integer getSMSCredits() {
        return sMSCredits;
    }

    public void setSMSCredits(Integer sMSCredits) {
        this.sMSCredits = sMSCredits;
    }

    public Integer getPendingCampaigns() {
        return pendingCampaigns;
    }

    public void setPendingCampaigns(Integer pendingCampaigns) {
        this.pendingCampaigns = pendingCampaigns;
    }

    public Integer getPendingCouponCampaigns() {
        return pendingCouponCampaigns;
    }

    public void setPendingCouponCampaigns(Integer pendingCouponCampaigns) {
        this.pendingCouponCampaigns = pendingCouponCampaigns;
    }

    public Integer getPendingMessage() {
        return pendingMessage;
    }

    public void setPendingMessage(Integer pendingMessage) {
        this.pendingMessage = pendingMessage;
    }

    public List<ActivityLog> getActivityLogs() {
        return activityLogs;
    }

    public void setActivityLogs(List<ActivityLog> activityLogs) {
        this.activityLogs = activityLogs;
    }

    public List<MessageSentCount> getMessageSentCounts() {
        return messageSentCounts;
    }

    public void setMessageSentCounts(List<MessageSentCount> messageSentCounts) {
        this.messageSentCounts = messageSentCounts;
    }

    public List<Campaign> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(List<Campaign> campaigns) {
        this.campaigns = campaigns;
    }

    public List<Object> getEcouponCampaigns() {
        return ecouponCampaigns;
    }

    public void setEcouponCampaigns(List<Object> ecouponCampaigns) {
        this.ecouponCampaigns = ecouponCampaigns;
    }

    public List<Object> getCreditRequests() {
        return creditRequests;
    }

    public void setCreditRequests(List<Object> creditRequests) {
        this.creditRequests = creditRequests;
    }

    public List<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

    public List<Object> getRedeemCoupons() {
        return redeemCoupons;
    }

    public void setRedeemCoupons(List<Object> redeemCoupons) {
        this.redeemCoupons = redeemCoupons;
    }

}

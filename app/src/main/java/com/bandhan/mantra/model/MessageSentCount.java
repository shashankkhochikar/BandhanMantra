
package com.bandhan.mantra.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageSentCount implements Serializable
{

    @SerializedName("Days")
    @Expose
    private String days;
    @SerializedName("TotalCount")
    @Expose
    private Integer totalCount;
    @SerializedName("CampaignCount")
    @Expose
    private Integer campaignCount;
    @SerializedName("EcouponCampaignCount")
    @Expose
    private Integer ecouponCampaignCount;
    private final static long serialVersionUID = 3454134349824625666L;

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getCampaignCount() {
        return campaignCount;
    }

    public void setCampaignCount(Integer campaignCount) {
        this.campaignCount = campaignCount;
    }

    public Integer getEcouponCampaignCount() {
        return ecouponCampaignCount;
    }

    public void setEcouponCampaignCount(Integer ecouponCampaignCount) {
        this.ecouponCampaignCount = ecouponCampaignCount;
    }

}

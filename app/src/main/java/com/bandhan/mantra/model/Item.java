
package com.bandhan.mantra.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item implements Serializable
{

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("StartDate")
    @Expose
    private String startDate;
    @SerializedName("StartTime")
    @Expose
    private Object startTime;
    @SerializedName("EndDate")
    @Expose
    private String endDate;
    @SerializedName("EndTime")
    @Expose
    private Object endTime;
    @SerializedName("IsEndDate")
    @Expose
    private Boolean isEndDate;
    @SerializedName("IPAddress")
    @Expose
    private String iPAddress;
    @SerializedName("IsCoupon")
    @Expose
    private Boolean isCoupon;
    @SerializedName("CouponExpire")
    @Expose
    private Object couponExpire;
    @SerializedName("CouponExpireType")
    @Expose
    private String couponExpireType;
    @SerializedName("MinPurchaseAmount")
    @Expose
    private Object minPurchaseAmount;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("SendDate")
    @Expose
    private Object sendDate;
    @SerializedName("IsBirthday")
    @Expose
    private Boolean isBirthday;
    @SerializedName("IsAnniversary")
    @Expose
    private Boolean isAnniversary;
    @SerializedName("GenderType")
    @Expose
    private String genderType;
    @SerializedName("IsRecurring")
    @Expose
    private Boolean isRecurring;
    @SerializedName("IsDay")
    @Expose
    private Boolean isDay;
    @SerializedName("IsWeek")
    @Expose
    private Boolean isWeek;
    @SerializedName("IsMonth")
    @Expose
    private Boolean isMonth;
    @SerializedName("DayValue")
    @Expose
    private Object dayValue;
    @SerializedName("WeekValue")
    @Expose
    private Object weekValue;
    @SerializedName("Sunday")
    @Expose
    private Boolean sunday;
    @SerializedName("Monday")
    @Expose
    private Boolean monday;
    @SerializedName("Tuesday")
    @Expose
    private Boolean tuesday;
    @SerializedName("Wednesday")
    @Expose
    private Boolean wednesday;
    @SerializedName("Thursday")
    @Expose
    private Boolean thursday;
    @SerializedName("Friday")
    @Expose
    private Boolean friday;
    @SerializedName("Saturday")
    @Expose
    private Boolean saturday;
    @SerializedName("MonthValue")
    @Expose
    private Object monthValue;
    @SerializedName("MonthDayValue")
    @Expose
    private Object monthDayValue;
    @SerializedName("ClientId")
    @Expose
    private Integer clientId;
    @SerializedName("CreatedBy")
    @Expose
    private Integer createdBy;
    @SerializedName("GroupId")
    @Expose
    private Object groupId;
    @SerializedName("GroupName")
    @Expose
    private Object groupName;
    @SerializedName("GroupContactCount")
    @Expose
    private Object groupContactCount;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("ForAllContact")
    @Expose
    private Boolean forAllContact;
    @SerializedName("LastCampaignId")
    @Expose
    private Object lastCampaignId;
    @SerializedName("TemplateDTO")
    @Expose
    private Object templateDTO;
    private final static long serialVersionUID = -2313533460054725345L;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Object getStartTime() {
        return startTime;
    }

    public void setStartTime(Object startTime) {
        this.startTime = startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Object getEndTime() {
        return endTime;
    }

    public void setEndTime(Object endTime) {
        this.endTime = endTime;
    }

    public Boolean getIsEndDate() {
        return isEndDate;
    }

    public void setIsEndDate(Boolean isEndDate) {
        this.isEndDate = isEndDate;
    }

    public String getIPAddress() {
        return iPAddress;
    }

    public void setIPAddress(String iPAddress) {
        this.iPAddress = iPAddress;
    }

    public Boolean getIsCoupon() {
        return isCoupon;
    }

    public void setIsCoupon(Boolean isCoupon) {
        this.isCoupon = isCoupon;
    }

    public Object getCouponExpire() {
        return couponExpire;
    }

    public void setCouponExpire(Object couponExpire) {
        this.couponExpire = couponExpire;
    }

    public String getCouponExpireType() {
        return couponExpireType;
    }

    public void setCouponExpireType(String couponExpireType) {
        this.couponExpireType = couponExpireType;
    }

    public Object getMinPurchaseAmount() {
        return minPurchaseAmount;
    }

    public void setMinPurchaseAmount(Object minPurchaseAmount) {
        this.minPurchaseAmount = minPurchaseAmount;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Object getSendDate() {
        return sendDate;
    }

    public void setSendDate(Object sendDate) {
        this.sendDate = sendDate;
    }

    public Boolean getIsBirthday() {
        return isBirthday;
    }

    public void setIsBirthday(Boolean isBirthday) {
        this.isBirthday = isBirthday;
    }

    public Boolean getIsAnniversary() {
        return isAnniversary;
    }

    public void setIsAnniversary(Boolean isAnniversary) {
        this.isAnniversary = isAnniversary;
    }

    public String getGenderType() {
        return genderType;
    }

    public void setGenderType(String genderType) {
        this.genderType = genderType;
    }

    public Boolean getIsRecurring() {
        return isRecurring;
    }

    public void setIsRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    public Boolean getIsDay() {
        return isDay;
    }

    public void setIsDay(Boolean isDay) {
        this.isDay = isDay;
    }

    public Boolean getIsWeek() {
        return isWeek;
    }

    public void setIsWeek(Boolean isWeek) {
        this.isWeek = isWeek;
    }

    public Boolean getIsMonth() {
        return isMonth;
    }

    public void setIsMonth(Boolean isMonth) {
        this.isMonth = isMonth;
    }

    public Object getDayValue() {
        return dayValue;
    }

    public void setDayValue(Object dayValue) {
        this.dayValue = dayValue;
    }

    public Object getWeekValue() {
        return weekValue;
    }

    public void setWeekValue(Object weekValue) {
        this.weekValue = weekValue;
    }

    public Boolean getSunday() {
        return sunday;
    }

    public void setSunday(Boolean sunday) {
        this.sunday = sunday;
    }

    public Boolean getMonday() {
        return monday;
    }

    public void setMonday(Boolean monday) {
        this.monday = monday;
    }

    public Boolean getTuesday() {
        return tuesday;
    }

    public void setTuesday(Boolean tuesday) {
        this.tuesday = tuesday;
    }

    public Boolean getWednesday() {
        return wednesday;
    }

    public void setWednesday(Boolean wednesday) {
        this.wednesday = wednesday;
    }

    public Boolean getThursday() {
        return thursday;
    }

    public void setThursday(Boolean thursday) {
        this.thursday = thursday;
    }

    public Boolean getFriday() {
        return friday;
    }

    public void setFriday(Boolean friday) {
        this.friday = friday;
    }

    public Boolean getSaturday() {
        return saturday;
    }

    public void setSaturday(Boolean saturday) {
        this.saturday = saturday;
    }

    public Object getMonthValue() {
        return monthValue;
    }

    public void setMonthValue(Object monthValue) {
        this.monthValue = monthValue;
    }

    public Object getMonthDayValue() {
        return monthDayValue;
    }

    public void setMonthDayValue(Object monthDayValue) {
        this.monthDayValue = monthDayValue;
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

    public Object getGroupId() {
        return groupId;
    }

    public void setGroupId(Object groupId) {
        this.groupId = groupId;
    }

    public Object getGroupName() {
        return groupName;
    }

    public void setGroupName(Object groupName) {
        this.groupName = groupName;
    }

    public Object getGroupContactCount() {
        return groupContactCount;
    }

    public void setGroupContactCount(Object groupContactCount) {
        this.groupContactCount = groupContactCount;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getForAllContact() {
        return forAllContact;
    }

    public void setForAllContact(Boolean forAllContact) {
        this.forAllContact = forAllContact;
    }

    public Object getLastCampaignId() {
        return lastCampaignId;
    }

    public void setLastCampaignId(Object lastCampaignId) {
        this.lastCampaignId = lastCampaignId;
    }

    public Object getTemplateDTO() {
        return templateDTO;
    }

    public void setTemplateDTO(Object templateDTO) {
        this.templateDTO = templateDTO;
    }

}


package com.bandhan.mantra.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Datum implements Serializable
{

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("BirthDate")
    @Expose
    private String birthDate;
    @SerializedName("AnniversaryDate")
    @Expose
    private String anniversaryDate;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("MobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("ClientId")
    @Expose
    private Integer clientId;
    @SerializedName("Groups")
    @Expose
    private List<Group> groups = null;
    @SerializedName("UserDefinedFields")
    @Expose
    private List<Object> userDefinedFields = null;
    @SerializedName("TotalCount")
    @Expose
    private Integer totalCount;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("LastName")
    @Expose
    private String lastName;
    @SerializedName("IsValid")
    @Expose
    private Boolean isValid;
    @SerializedName("IsMobileValid")
    @Expose
    private Boolean isMobileValid;
    private final static long serialVersionUID = -4415563210143890987L;

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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAnniversaryDate() {
        return anniversaryDate;
    }

    public void setAnniversaryDate(String anniversaryDate) {
        this.anniversaryDate = anniversaryDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Object> getUserDefinedFields() {
        return userDefinedFields;
    }

    public void setUserDefinedFields(List<Object> userDefinedFields) {
        this.userDefinedFields = userDefinedFields;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    public Boolean getIsMobileValid() {
        return isMobileValid;
    }

    public void setIsMobileValid(Boolean isMobileValid) {
        this.isMobileValid = isMobileValid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", name).append("birthDate", birthDate).append("anniversaryDate", anniversaryDate).append("email", email).append("mobileNumber", mobileNumber).append("gender", gender).append("clientId", clientId).append("groups", groups).append("userDefinedFields", userDefinedFields).append("totalCount", totalCount).append("firstName", firstName).append("lastName", lastName).append("isValid", isValid).append("isMobileValid", isMobileValid).toString();
    }

}

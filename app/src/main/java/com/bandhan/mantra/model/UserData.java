
package com.bandhan.mantra.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData implements Serializable
{

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("LastName")
    @Expose
    private String lastName;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Address")
    @Expose
    private Object address;
    @SerializedName("Location")
    @Expose
    private Object location;
    @SerializedName("LocationId")
    @Expose
    private Integer locationId;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("ClientId")
    @Expose
    private Integer clientId;
    @SerializedName("ImageURL")
    @Expose
    private Object imageURL;
    @SerializedName("UserAccessPrivileges")
    @Expose
    private UserAccessPrivileges userAccessPrivileges;
    @SerializedName("UserType")
    @Expose
    private String userType;
    private final static long serialVersionUID = 9068160124021921181L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public Object getLocation() {
        return location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Object getImageURL() {
        return imageURL;
    }

    public void setImageURL(Object imageURL) {
        this.imageURL = imageURL;
    }

    public UserAccessPrivileges getUserAccessPrivileges() {
        return userAccessPrivileges;
    }

    public void setUserAccessPrivileges(UserAccessPrivileges userAccessPrivileges) {
        this.userAccessPrivileges = userAccessPrivileges;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

}

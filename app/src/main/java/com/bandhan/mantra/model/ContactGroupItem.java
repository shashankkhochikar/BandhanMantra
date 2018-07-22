package com.bandhan.mantra.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ContactGroupItem {


    public List<ContactGroupItemData> getContactGroupItemData() {
        return contactGroupItemData;
    }

    public void setContactGroupItemData(List<ContactGroupItemData> contactGroupItemData) {
        this.contactGroupItemData = contactGroupItemData;
    }

    private List<ContactGroupItemData> contactGroupItemData;


}
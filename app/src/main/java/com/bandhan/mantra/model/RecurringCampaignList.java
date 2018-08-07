
package com.bandhan.mantra.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecurringCampaignList implements Serializable
{

    @SerializedName("Items")
    @Expose
    private List<Item> items = null;
    private final static long serialVersionUID = 8151934463034768892L;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

}

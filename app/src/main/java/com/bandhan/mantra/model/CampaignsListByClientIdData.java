
package com.bandhan.mantra.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CampaignsListByClientIdData implements Serializable
{

    @SerializedName("Count")
    @Expose
    private Integer count;
    @SerializedName("Data")
    @Expose
    private List<CampaignsListItem> data = null;
    @SerializedName("SuccessCount")
    @Expose
    private Object successCount;
    @SerializedName("FailureCount")
    @Expose
    private Object failureCount;
    private final static long serialVersionUID = 8279135248459239060L;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<CampaignsListItem> getData() {
        return data;
    }

    public void setData(List<CampaignsListItem> data) {
        this.data = data;
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

}

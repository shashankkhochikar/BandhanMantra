
package com.bandhan.mantra.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class GroupContactsByClientIdData implements Serializable
{

    @SerializedName("Count")
    @Expose
    private Integer count;
    @SerializedName("Data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("SuccessCount")
    @Expose
    private Integer successCount;
    @SerializedName("FailureCount")
    @Expose
    private Integer failureCount;
    private final static long serialVersionUID = -6616529702376136942L;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(Integer failureCount) {
        this.failureCount = failureCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("count", count).append("data", data).append("successCount", successCount).append("failureCount", failureCount).toString();
    }

}

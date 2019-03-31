package com.rohit.pragnesh.myb2b.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JWT {

    @SerializedName("refresh")
    @Expose
    private String refresh;
    @SerializedName("access")
    @Expose
    private String access;

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getAuthorization() {
        return "Bearer " + getAccess();
    }

}

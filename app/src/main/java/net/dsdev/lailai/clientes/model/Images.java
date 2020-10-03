package net.dsdev.lailai.clientes.model;

import net.dsdev.lailai.clientes.util.Constants;

import com.google.gson.annotations.SerializedName;

public class Images {

    @SerializedName(Constants.unavailable)
    private String unavailable;

    @SerializedName(Constants.normal)
    private String normal;

    @SerializedName(Constants.small)
    private String small;

    @SerializedName(Constants.large)
    private String large;

    public String getUnavailable() {
        return unavailable;
    }

    public void setUnavailable(String unavailable) {
        this.unavailable = unavailable;
    }

    public String getNormal() {
        return normal;
    }

    public void setNormal(String normal) {
        this.normal = normal;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }
}

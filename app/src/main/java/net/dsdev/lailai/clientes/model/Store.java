package net.dsdev.lailai.clientes.model;

import com.google.gson.annotations.SerializedName;

import net.dsdev.lailai.clientes.util.Constants;

import java.io.Serializable;

public class Store implements Serializable {
    private Long id;
    @SerializedName(Constants.code)
    private String code;
    @SerializedName(Constants.name)
    private String name;
    @SerializedName(Constants.direction)
    private String address;
    @SerializedName(Constants.telephone)
    private String telephone;
    @SerializedName(Constants.openHour)
    private String openHour;
    @SerializedName(Constants.closeHour)
    private String closeHour;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getOpenHour() {
        return openHour;
    }

    public void setOpenHour(String openHour) {
        this.openHour = openHour;
    }

    public String getCloseHour() {
        return closeHour;
    }

    public void setCloseHour(String closeHour) {
        this.closeHour = closeHour;
    }
}

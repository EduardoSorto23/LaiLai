package net.dsdev.lailai.clientes.model.users;

import com.google.gson.annotations.SerializedName;

import net.dsdev.lailai.clientes.model.Store;
import net.dsdev.lailai.clientes.util.Constants;

import java.util.List;

public class Coverage {

    @SerializedName(Constants.id)
    private Long id;
    @SerializedName(Constants.result)
    private String state;
    @SerializedName(Constants.message)
    private String message;
    @SerializedName(Constants.stores)
    private List<Store> stores;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }
}

package net.dsdev.lailai.clientes.model;

import com.google.gson.annotations.SerializedName;

import net.dsdev.lailai.clientes.util.Constants;

import java.util.List;

public class StoreList {
    @SerializedName(Constants.stores)
    private List<Store> stores;

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }
}

package net.dsdev.lailai.clientes.model;

import com.google.gson.annotations.SerializedName;
import net.dsdev.lailai.clientes.util.Constants;
import java.io.Serializable;
import java.util.List;

public class ActiveOrders implements Serializable {

    public ActiveOrders() {
    }

    @SerializedName(Constants.orders)
    private List<Order> orders;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}

package net.dsdev.lailai.clientes.model.users;

import net.dsdev.lailai.clientes.util.Constants;
import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName(Constants.valid)
    private boolean valid;

    @SerializedName(Constants.msg)
    private String msg;


    @SerializedName(Constants.client)
    private Client client;

    @SerializedName(Constants.orderCode)
    private String orderCode;



    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
}

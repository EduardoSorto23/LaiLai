package net.dsdev.lailai.clientes.model.users;

import net.dsdev.lailai.clientes.util.Constants;
import com.google.gson.annotations.SerializedName;

public class Accounts  {

    public Accounts() {
    }

    public Accounts(String user) {
        this.user = user;
    }

    public Accounts(String user,String actualClave, String nuevaClave) {
        this.user = user;
        this.actualClave = actualClave;
        this.nuevaClave = nuevaClave;
    }

    @SerializedName(Constants.valid)
    private boolean valid;

    @SerializedName(Constants.email)
    private String user;

    private String actualClave;

    private String nuevaClave;

    private String msg;


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getActualClave() {
        return actualClave;
    }

    public void setActualClave(String actualClave) {
        this.actualClave = actualClave;
    }

    public String getNuevaClave() {
        return nuevaClave;
    }

    public void setNuevaClave(String nuevaClave) {
        this.nuevaClave = nuevaClave;
    }
}

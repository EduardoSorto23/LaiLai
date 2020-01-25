package net.dsdev.lailai.clientes.model.users;

import net.dsdev.lailai.clientes.util.Constants;
import com.google.gson.annotations.SerializedName;

public class LoginLocalRequest {

    @SerializedName(Constants.email)
    private String user;

    @SerializedName(Constants.password)
    private String password;

    @SerializedName(Constants.accountType)
    private String accountType;

    public LoginLocalRequest(String user, String password, String accountType) {
        this.user = user;
        this.password = password;
        this.accountType = accountType;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}

package net.dsdev.lailai.clientes.model.users;

import com.google.gson.annotations.SerializedName;

import net.dsdev.lailai.clientes.util.Constants;

public class LoginApiRequest {

    @SerializedName(Constants.names)
    private String names;

    @SerializedName(Constants.lastNames)
    private String lastNames;

    @SerializedName(Constants.email)
    private String email;

    @SerializedName(Constants.token)
    private String token;

    public LoginApiRequest(String names, String lastNames, String email, String token) {
        this.names = names;
        this.lastNames = lastNames;
        this.email = email;
        this.token = token;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getLastNames() {
        return lastNames;
    }

    public void setLastNames(String lastNames) {
        this.lastNames = lastNames;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

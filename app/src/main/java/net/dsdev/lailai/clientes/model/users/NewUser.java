package net.dsdev.lailai.clientes.model.users;

import net.dsdev.lailai.clientes.util.Constants;
import com.google.gson.annotations.SerializedName;

public class NewUser {

    @SerializedName(Constants.names)
    private String names;

    @SerializedName(Constants.lastNames)
    private String lastNames;

    @SerializedName(Constants.user)
    private String user;

    @SerializedName(Constants.email)
    private String email;

    @SerializedName(Constants.password)
    private String password;

    @SerializedName(Constants.accountType)
    private String type;

    @SerializedName(Constants.gender)
    private String gender;

    public NewUser(String names, String lastNames, String user, String email, String password, String type, String gender) {
        this.names = names;
        this.lastNames = lastNames;
        this.user = user;
        this.email = email;
        this.password = password;
        this.type = type;
        this.gender = gender;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

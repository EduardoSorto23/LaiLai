package net.dsdev.lailai.clientes.model.users;

import net.dsdev.lailai.clientes.util.Constants;

import com.google.gson.annotations.SerializedName;

public class Client {

    @SerializedName(Constants.idClient)
    private long idCliente;

    @SerializedName(Constants.names)
    private String names;

    @SerializedName(Constants.lastNames)
    private String lastNames;

    @SerializedName(Constants.email)
    private String email;

    @SerializedName(Constants.accountType)
    private String accountType;

    private long idAddress;

    private String telephone;

    private String finalAddress;

    public Client(String names, String lastNames, String email, String accountType) {
        this.names = names;
        this.lastNames = lastNames;
        this.email = email;
        this.accountType = accountType;
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

    public long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public long getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(long idAddress) {
        this.idAddress = idAddress;
    }

    public String getFinalAddress() {
        return finalAddress;
    }

    public void setFinalAddress(String finalAddress) {
        this.finalAddress = finalAddress;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}

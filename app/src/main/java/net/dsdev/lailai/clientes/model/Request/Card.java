package net.dsdev.lailai.clientes.model.Request;

import net.dsdev.lailai.clientes.util.Constants;
import com.google.gson.annotations.SerializedName;

public class Card {

    @SerializedName(Constants.paymentMethod)
    String paymentMethod = "EFE";

    @SerializedName(Constants.document)
    String document;


    @SerializedName(Constants.ownerName)
    String ownerName;

    @SerializedName(Constants.ccv)
    Integer ccv;

    @SerializedName(Constants.month)
    private String month;

    @SerializedName(Constants.year)
    private String year;

    public Card() {
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }


    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Integer getCcv() {
        return ccv;
    }

    public void setCcv(Integer ccv) {
        this.ccv = ccv;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}

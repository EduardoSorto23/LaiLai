package net.dsdev.lailai.clientes.model.cards;

import com.google.gson.annotations.SerializedName;

import net.dsdev.lailai.clientes.util.Constants;

public class CardAuth {

    @SerializedName(Constants.year)
    private String year;

    @SerializedName(Constants.month)
    private String month;

    @SerializedName(Constants.owner)
    private String ownerName;

    @SerializedName(Constants.documentId)
    private String documentId;

    @SerializedName(Constants.idClient)
    private long idClient;

    @SerializedName(Constants.cardId)
    private long cardId;

    private int ccv;

    public CardAuth(String year, String month, String ownerName, String documentId, long idClient) {
        this.year = year;
        this.month = month;
        this.ownerName = ownerName;
        this.documentId = documentId;
        this.idClient = idClient;
    }

    public CardAuth() {
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public long getIdClient() {
        return idClient;
    }

    public void setIdClient(long idClient) {
        this.idClient = idClient;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public int getCcv() {
        return ccv;
    }

    public void setCcv(int ccv) {
        this.ccv = ccv;
    }
}

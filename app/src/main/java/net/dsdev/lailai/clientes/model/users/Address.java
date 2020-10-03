package net.dsdev.lailai.clientes.model.users;

import com.google.gson.annotations.SerializedName;

import net.dsdev.lailai.clientes.util.Constants;

public class Address extends AddressDTO {

    @SerializedName(Constants.telephone)
    private String telephone;

    @SerializedName(Constants.direction)
    private String direction;

    @SerializedName(Constants.references)
    private String references;

    @SerializedName(Constants.suBurb)
    private String suBurb;

    @SerializedName(Constants.zone)
    private String zone;

    @SerializedName(Constants.accessCode)
    private String accessCode;
    @SerializedName(Constants.houseNumber)
    private String houseNumber;

    @SerializedName(Constants.department)
    private AddressDTO oDepartment;
    @SerializedName(Constants.municipaly)
    private AddressDTO oMunicipaly;

    @SerializedName(Constants.latitude)
    private double latitude;

    @SerializedName(Constants.longitude)
    private double longitude;


    @SerializedName(Constants.idAddress)
    private long idAddress;

    @SerializedName(Constants.idClient)
    private long idClient;

    @SerializedName(Constants.month)
    private String department;

    @SerializedName(Constants.year)
    private String municipaly;


    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public String getSuBurb() {
        return suBurb;
    }

    public void setSuBurb(String suBurb) {
        this.suBurb = suBurb;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public AddressDTO getoDepartment() {
        return oDepartment;
    }

    public void setoDepartment(AddressDTO oDepartment) {
        this.oDepartment = oDepartment;
    }

    public AddressDTO getoMunicipaly() {
        return oMunicipaly;
    }

    public void setoMunicipaly(AddressDTO oMunicipaly) {
        this.oMunicipaly = oMunicipaly;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(long idAddress) {
        this.idAddress = idAddress;
    }

    public long getIdClient() {
        return idClient;
    }

    public void setIdClient(long idClient) {
        this.idClient = idClient;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMunicipaly() {
        return municipaly;
    }

    public void setMunicipaly(String municipaly) {
        this.municipaly = municipaly;
    }
}

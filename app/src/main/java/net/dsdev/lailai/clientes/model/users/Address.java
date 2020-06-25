package net.dsdev.lailai.clientes.model.users;

import com.google.gson.annotations.SerializedName;

import net.dsdev.lailai.clientes.util.Constants;

public class Address {
    @SerializedName(Constants.id)
    private long id;

    @SerializedName(Constants.idAddress)
    private long idAddress;

    @SerializedName(Constants.idClient)
    private long idClient;

    @SerializedName(Constants.name)
    private String nombre;

    @SerializedName(Constants.department)
    private String department;

    @SerializedName(Constants.municipaly)
    private String municipaly;

    @SerializedName(Constants.indications)
    private String indications;

    @SerializedName(Constants.direction)
    private String direction;

    @SerializedName(Constants.latitude)
    private double latitude;

    @SerializedName(Constants.longitude)
    private double longitude;

    @SerializedName(Constants.telephone)
    private String telephone;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getIndications() {
        return indications;
    }

    public void setIndications(String indications) {
        this.indications = indications;
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

    public long getIdClient() {
        return idClient;
    }

    public void setIdClient(long idClient) {
        this.idClient = idClient;
    }

    public long getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(long idAddress) {
        this.idAddress = idAddress;
    }

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
}

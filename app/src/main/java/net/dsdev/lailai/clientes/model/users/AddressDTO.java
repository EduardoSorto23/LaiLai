package net.dsdev.lailai.clientes.model.users;

import com.google.gson.annotations.SerializedName;

import net.dsdev.lailai.clientes.util.Constants;

import java.io.Serializable;
import java.util.List;

public class AddressDTO implements Serializable {
    @SerializedName(Constants.id)
    private Long id;

    @SerializedName(Constants.name)
    private String nombre;

    @SerializedName(Constants.municipalies)
    private List<AddressDTO> municipalies;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<AddressDTO>  getMunicipalies() {
        return municipalies;
    }

    public void setMunicipalies(List<AddressDTO>  municipalies) {
        this.municipalies = municipalies;
    }
}

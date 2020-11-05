package net.dsdev.lailai.clientes.model.users;

import com.google.gson.annotations.SerializedName;

import net.dsdev.lailai.clientes.model.menuDetail.JsonMenuDetail;
import net.dsdev.lailai.clientes.model.menuDetail.MenuDetail;
import net.dsdev.lailai.clientes.util.Constants;

import java.util.List;

public class ventaSugerida {

    @SerializedName(Constants.amount)
    private Double monto;

    @SerializedName(Constants.canal)
    private String canal;

    @SerializedName(Constants.idClient)
    private Integer idCliente;

    @SerializedName(Constants.idDirection)
    private Integer idDireccion;

    @SerializedName(Constants.telephone)
    private String telefono;

    @SerializedName(Constants.chance)
    private String ocasion;

    @SerializedName(Constants.menus)
    private List<MenuDetail> menus;


    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(Integer idDireccion) {
        this.idDireccion = idDireccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getOcasion() {
        return ocasion;
    }

    public void setOcasion(String ocasion) {
        this.ocasion = ocasion;
    }

    public List<MenuDetail> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuDetail> menus) {
        this.menus = menus;
    }
}
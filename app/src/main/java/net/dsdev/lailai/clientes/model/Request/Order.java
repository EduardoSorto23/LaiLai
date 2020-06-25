package net.dsdev.lailai.clientes.model.Request;

import net.dsdev.lailai.clientes.model.menuDetail.MenuDetail;
import net.dsdev.lailai.clientes.util.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {

    @SerializedName(Constants.idClient)
    private long idClient;

    @SerializedName(Constants.telephone)
    private String telephone;

    @SerializedName(Constants.idDirection)
    private long idDirection;

    @SerializedName(Constants.indications)
    private String indications;

    @SerializedName(Constants.canal)
    private String canal;

    @SerializedName(Constants.amount)
    private Double amount;

    @SerializedName(Constants.menus)
    private List<MenuDetail> menus;

    @SerializedName(Constants.paymentDetail)
    private List<Card> paymentDetail;

    private String fechaHora;

    @SerializedName(Constants.orderCode)
    private String orderCode;
    @SerializedName(Constants.state)
    private String orderState;

    @SerializedName(Constants.idStore)
    private Long idStore;

    private String horaEntrega;
    public String ocasion;

    public Order() {
    }

    public long getIdClient() {
        return idClient;
    }

    public void setIdClient(long idClient) {
        this.idClient = idClient;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public long getIdDirection() {
        return idDirection;
    }

    public void setIdDirection(long idDirection) {
        this.idDirection = idDirection;
    }

    public String getIndications() {
        return indications;
    }

    public void setIndications(String indications) {
        this.indications = indications;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<MenuDetail> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuDetail> menus) {
        this.menus = menus;
    }

    public List<Card> getPaymentDetail() {
        return paymentDetail;
    }

    public void setPaymentDetail(List<Card> paymentDetail) {
        this.paymentDetail = paymentDetail;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public Long getIdStore() {
        return idStore;
    }

    public void setIdStore(Long idStore) {
        this.idStore = idStore;
    }

    public String getHoraEntrega() {
        return horaEntrega;
    }

    public void setHoraEntrega(String horaEntrega) {
        this.horaEntrega = horaEntrega;
    }

    public String getOcasion() {
        return ocasion;
    }

    public void setOcasion(String ocasion) {
        this.ocasion = ocasion;
    }
}

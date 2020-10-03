package net.dsdev.lailai.clientes.model;

import com.google.gson.annotations.SerializedName;

import net.dsdev.lailai.clientes.util.Constants;

import java.io.Serializable;

public class Order implements Serializable {

    @SerializedName(Constants.id)
    private Long id;

    @SerializedName(Constants.orderCode)
    private String orderCode;

    @SerializedName(Constants.orderDate)
    private String date;

    @SerializedName(Constants.orderHour)
    private String hour;

    @SerializedName(Constants.orderTotal)
    private Double orderTotal;

    @SerializedName(Constants.state)
    private String state;

    public Order() {
    }

    public Order(long id, String orderCode, String date, String state) {
        this(id,orderCode,date,null,null,state);
    }

    public Order(Long id, String orderCode, String date, Double orderTotal, String state) {
        this(id,orderCode,date,null,orderTotal,state);
    }

    public Order(Long id, String orderCode, String date, String hour, Double orderTotal, String state) {
        this.id = id;
        this.orderCode = orderCode;
        this.date = date;
        this.hour = hour;
        this.orderTotal = orderTotal;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

package net.dsdev.lailai.clientes.model.menuDetail;

import net.dsdev.lailai.clientes.model.Images;
import net.dsdev.lailai.clientes.util.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuDetail {

    @SerializedName(Constants.idMenu)
    private int idMenu;

    @SerializedName(Constants.name)
    private String name;

    @SerializedName(Constants.desc)
    private String desc;

    @SerializedName(Constants.unitPrice)
    private Double price;

    @SerializedName(Constants.images)
    private Images images;

    @SerializedName(Constants.options)
    private List<OptionMenuDetail> options;

    // Parametros para enviar la orden
    @SerializedName(Constants.courtesy)
    private String courtesy = "N";

    @SerializedName(Constants.quantity)
    private int quantity = 1;

    @SerializedName(Constants.extraPrice)
    private Double extraPrice = 0.0;

    @SerializedName(Constants.finalPrice)
    private Double finalPrice = 0.0;


    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<OptionMenuDetail> getOptions() {
        return options;
    }

    public void setOptions(List<OptionMenuDetail> options) {
        this.options = options;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getExtraPrice() {
        return extraPrice;
    }

    public void setExtraPrice(Double extraPrice) {
        this.extraPrice = extraPrice;
    }

    public String getCourtesy() {
        return courtesy;
    }

    public void setCourtesy(String courtesy) {
        this.courtesy = courtesy;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }
}

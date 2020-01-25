package net.dsdev.lailai.clientes.model.Promo;

import net.dsdev.lailai.clientes.model.Images;
import net.dsdev.lailai.clientes.util.Constants;
import com.google.gson.annotations.SerializedName;

public class Promo {

    @SerializedName(Constants.id)
    long id;

    @SerializedName(Constants.name)
    String name;

    @SerializedName(Constants.desc)
    String desc;

    @SerializedName(Constants.price)
    Double price;

    @SerializedName(Constants.state)
    String state;

    @SerializedName(Constants.images)
    private Images images;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }
}

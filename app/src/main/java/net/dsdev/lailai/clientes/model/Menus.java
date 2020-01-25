package net.dsdev.lailai.clientes.model;

import net.dsdev.lailai.clientes.util.Constants;
import com.google.gson.annotations.SerializedName;

public class Menus {


    @SerializedName(Constants.id)
    private long id;

    @SerializedName(Constants.name)
    private String name;

    @SerializedName(Constants.desc)
    private String description;

    @SerializedName(Constants.price)
    private Double price;

    @SerializedName(Constants.state)
    private String state;

    @SerializedName(Constants.images)
    private Images images;

    private int itemType = Constants.ITEM;

    public Menus() {
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }
}

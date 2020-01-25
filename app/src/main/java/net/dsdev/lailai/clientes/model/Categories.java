package net.dsdev.lailai.clientes.model;

import net.dsdev.lailai.clientes.util.Constants;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Categories implements Serializable {

    @SerializedName(Constants.id)
    private long id;

    @SerializedName(Constants.name)
    private String name;

    @SerializedName(Constants.desc)
    private String description;

    @SerializedName(Constants.state)
    private String state;

    @SerializedName("subcategoria")
    private String subCategory;

    @SerializedName("subcategorias")
    private List<SubCategories> subCategories;

    @SerializedName(Constants.images)
    private Images images;

    @SerializedName(Constants.menus)
    private List<Menus> menus;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public List<SubCategories> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategories> subCategories) {
        this.subCategories = subCategories;
    }

    public List<Menus> getMenus() {
        return menus;
    }

    public void setMenus(List<Menus> menus) {
        this.menus = menus;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }
}

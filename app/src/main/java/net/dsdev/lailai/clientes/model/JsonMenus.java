package net.dsdev.lailai.clientes.model;

import net.dsdev.lailai.clientes.util.Constants;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class JsonMenus implements Serializable {

    public JsonMenus() {
    }

    @SerializedName(Constants.categories)
    private List<Categories> categories;

    public List<Categories> getCategories() {
        return categories;
    }

    public void setCategories(List<Categories> categories) {
        this.categories = categories;
    }

}

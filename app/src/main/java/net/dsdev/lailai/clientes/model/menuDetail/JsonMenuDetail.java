package net.dsdev.lailai.clientes.model.menuDetail;

import net.dsdev.lailai.clientes.util.Constants;
import com.google.gson.annotations.SerializedName;

public class JsonMenuDetail {

    @SerializedName(Constants.menu)
    private MenuDetail menu;

    public MenuDetail getMenu() {
        return menu;
    }

    public void setMenu(MenuDetail menu) {
        this.menu = menu;
    }
}

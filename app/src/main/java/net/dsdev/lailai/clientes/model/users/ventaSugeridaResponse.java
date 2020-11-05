package net.dsdev.lailai.clientes.model.users;

import com.google.gson.annotations.SerializedName;

import net.dsdev.lailai.clientes.model.menuDetail.JsonMenuDetail;
import net.dsdev.lailai.clientes.model.menuDetail.MenuDetail;
import net.dsdev.lailai.clientes.util.Constants;

import java.util.List;

public class ventaSugeridaResponse {

    @SerializedName(Constants.valid)
    private String result;

    @SerializedName(Constants.menus)
    private List<MenuDetail> menus;


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<MenuDetail> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuDetail> menus) {
        this.menus = menus;
    }
}

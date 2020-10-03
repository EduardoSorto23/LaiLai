package net.dsdev.lailai.clientes.model.Promo;

import net.dsdev.lailai.clientes.util.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PromoList {

    @SerializedName(Constants.promos)
    List<Promo> promos;

    public PromoList() {
    }

    public List<Promo> getPromos() {
        return promos;
    }

    public void setPromos(List<Promo> promos) {
        this.promos = promos;
    }
}

package net.dsdev.lailai.clientes.model.menuDetail;

import net.dsdev.lailai.clientes.model.Images;
import net.dsdev.lailai.clientes.util.Constants;
import com.google.gson.annotations.SerializedName;

public class VariantMenuDetail {

    @SerializedName(Constants.idVariant)
    private int idVariant;

    @SerializedName(Constants.idProduct)
    private int idProduct;

    @SerializedName(Constants.variant)
    private String variant;

    @SerializedName(Constants.extraPrice)
    private Double extraPrice;

    @SerializedName(Constants.isDefault)
    private boolean isDefault;

    @SerializedName(Constants.images)
    private Images images;

    public int getIdVariant() {
        return idVariant;
    }

    public void setIdVariant(int idVariant) {
        this.idVariant = idVariant;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public Double getExtraPrice() {
        return extraPrice;
    }

    public void setExtraPrice(Double extraPrice) {
        this.extraPrice = extraPrice;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }
}

package net.dsdev.lailai.clientes.model.menuDetail;

import net.dsdev.lailai.clientes.util.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OptionMenuDetail {

    @SerializedName(Constants.idOption)
    private int idOption;

    @SerializedName(Constants.question)
    private String question;

    @SerializedName(Constants.quantity)
    private long quantity = 1;

    @SerializedName(Constants.variants)
    private List<VariantMenuDetail> variants;

    @SerializedName(Constants.selectedVariants)
    private List<VariantMenuDetail> selectedVariants = new ArrayList<>();

    public int getIdOption() {
        return idOption;
    }

    public void setIdOption(int idOption) {
        this.idOption = idOption;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<VariantMenuDetail> getVariants() {
        return variants;
    }

    public void setVariants(List<VariantMenuDetail> variants) {
        this.variants = variants;
    }

    public List<VariantMenuDetail> getSelectedVariants() {
        return selectedVariants;
    }

    public void setSelectedVariants(List<VariantMenuDetail> selectedVariants) {
        this.selectedVariants = selectedVariants;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}

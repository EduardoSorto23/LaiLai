package net.dsdev.lailai.clientes.model.cards;

import com.google.gson.annotations.SerializedName;

import net.dsdev.lailai.clientes.util.Constants;

import java.util.List;

public class CardList {

    @SerializedName(Constants.cards)
    List<CardAuth> cards;

    public List<CardAuth> getCards() {
        return cards;
    }

    public void setCards(List<CardAuth> cards) {
        this.cards = cards;
    }
}

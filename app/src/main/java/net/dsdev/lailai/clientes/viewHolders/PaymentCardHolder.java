package net.dsdev.lailai.clientes.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import net.dsdev.lailai.clientes.R;

public class PaymentCardHolder  extends RecyclerView.ViewHolder  {

    private CardView cardItem;
    private TextView card_number,card_expire,card_cvv,card_name;

    public PaymentCardHolder(@NonNull View itemView) {
        super(itemView);
        cardItem = itemView.findViewById(R.id.cardItem);
        card_number = itemView.findViewById(R.id.card_number);
        card_expire = itemView.findViewById(R.id.card_expire);
        card_cvv = itemView.findViewById(R.id.card_cvv);
        card_name = itemView.findViewById(R.id.card_name);
    }

    public CardView getCardItem() {
        return cardItem;
    }

    public void setCardItem(CardView cardItem) {
        this.cardItem = cardItem;
    }

    public TextView getCard_number() {
        return card_number;
    }

    public void setCard_number(TextView card_number) {
        this.card_number = card_number;
    }

    public TextView getCard_expire() {
        return card_expire;
    }

    public void setCard_expire(TextView card_expire) {
        this.card_expire = card_expire;
    }

    public TextView getCard_cvv() {
        return card_cvv;
    }

    public void setCard_cvv(TextView card_cvv) {
        this.card_cvv = card_cvv;
    }

    public TextView getCard_name() {
        return card_name;
    }

    public void setCard_name(TextView card_name) {
        this.card_name = card_name;
    }
}

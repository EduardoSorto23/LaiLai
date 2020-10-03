package net.dsdev.lailai.clientes.adapters.payment;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.model.cards.CardAuth;
import net.dsdev.lailai.clientes.viewHolders.PaymentCardHolder;

import java.util.List;

public abstract class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentCardHolder>  {
    private Context context;
    private List<CardAuth> cardList;

    public PaymentMethodAdapter(Context context) {
        this.context = context;
    }

    public void setCardList(List<CardAuth>  cardList) {
        this.cardList = cardList;
        notifyDataSetChanged();
    }

    public abstract void setClickListener(PaymentCardHolder holder, CardAuth cardAuth);


    @NonNull
    @Override
    public PaymentCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_card, parent, false);
        return new PaymentCardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentCardHolder holder, int position) {
        CardAuth cardAuth = cardList.get(position);
        holder.getCard_cvv().setText("xxx");
        holder.getCard_expire().setText(cardAuth.getMonth().concat("/").concat(cardAuth.getYear()));
        holder.getCard_name().setText(cardAuth.getOwnerName());
        holder.getCard_number().setText(cardAuth.getDocumentId().replaceAll("....", "$0 "));
        setClickListener(holder,cardAuth);
    }

    @Override
    public int getItemCount() {
        return this.cardList.size();
    }

}

package net.dsdev.lailai.clientes.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import net.dsdev.lailai.clientes.R;

public class HomeViewHolder extends RecyclerView.ViewHolder {
    private CardView cardView;
    private TextView txtHomeTittle;
    private ImageView imgHomeItem;

    public HomeViewHolder(@NonNull View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.materialCardView);
        txtHomeTittle = itemView.findViewById(R.id.txtHomeTittle);
        imgHomeItem = itemView.findViewById(R.id.imgHomeItem);
    }

    public CardView getCardView() {
        return cardView;
    }

    public void setCardView(CardView cardView) {
        this.cardView = cardView;
    }

    public TextView getTxtHomeTittle() {
        return txtHomeTittle;
    }

    public void setTxtHomeTittle(TextView txtHomeTittle) {
        this.txtHomeTittle = txtHomeTittle;
    }

    public ImageView getImgHomeItem() {
        return imgHomeItem;
    }

    public void setImgHomeItem(ImageView imgHomeItem) {
        this.imgHomeItem = imgHomeItem;
    }
}

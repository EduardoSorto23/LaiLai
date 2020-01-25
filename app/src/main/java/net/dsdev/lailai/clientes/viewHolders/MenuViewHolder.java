package net.dsdev.lailai.clientes.viewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.model.Menus;

public class MenuViewHolder extends RecyclerView.ViewHolder{
    private TextView txtTittle;
    private TextView txtDesc;
    private ImageView imgMenu;
    private Button btnPrice;
    private CardView cardView;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        txtTittle = itemView.findViewById(R.id.txtTittle);
        txtDesc = itemView.findViewById(R.id.txtDesc);
        imgMenu = itemView.findViewById(R.id.imgMenu);
        btnPrice = itemView.findViewById(R.id.btnPrice);
        cardView = itemView.findViewById(R.id.materialCardView);
    }

    public void bind(Menus menu){

    }

    public TextView getTxtTittle() {
        return txtTittle;
    }

    public void setTxtTittle(TextView txtTittle) {
        this.txtTittle = txtTittle;
    }

    public TextView getTxtDesc() {
        return txtDesc;
    }

    public void setTxtDesc(TextView txtDesc) {
        this.txtDesc = txtDesc;
    }

    public ImageView getImgMenu() {
        return imgMenu;
    }

    public void setImgMenu(ImageView imgMenu) {
        this.imgMenu = imgMenu;
    }

    public Button getBtnPrice() {
        return btnPrice;
    }

    public void setBtnPrice(Button btnPrice) {
        this.btnPrice = btnPrice;
    }

    public CardView getCardView() {
        return cardView;
    }

    public void setCardView(CardView cardView) {
        this.cardView = cardView;
    }
}
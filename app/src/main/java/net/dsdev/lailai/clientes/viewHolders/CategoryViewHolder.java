package net.dsdev.lailai.clientes.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import net.dsdev.lailai.clientes.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    private TextView txtName, txtShowMenu;
    private ImageView imgCategory;
    private CardView cvCategory;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        txtName = itemView.findViewById(R.id.txtCategoryName);
        txtShowMenu = itemView.findViewById(R.id.txtShowMenuCategory);
        imgCategory = itemView.findViewById(R.id.imgCategory);
        cvCategory = itemView.findViewById(R.id.cvCategory);
    }

    public TextView getTxtName() {
        return txtName;
    }

    public TextView getTxtShowMenu() {
        return txtShowMenu;
    }

    public ImageView getImgCategory() {
        return imgCategory;
    }

    public CardView getCvCategory() {
        return cvCategory;
    }
}

package net.dsdev.lailai.clientes.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.dsdev.lailai.clientes.R;

public class MenuListViewHolder extends RecyclerView.ViewHolder {

    private TextView txtName, txtDesc, txtPrice;
    private ImageView imgMenuList;
    private LinearLayout clMenuList;

    public MenuListViewHolder(@NonNull View itemView) {
        super(itemView);
        txtName = itemView.findViewById(R.id.txtMenuListName);
        txtDesc = itemView.findViewById(R.id.txtMenuListDesc);
        txtPrice = itemView.findViewById(R.id.txtMenuListPrice);
        imgMenuList = itemView.findViewById(R.id.imgMenuList);
        clMenuList = itemView.findViewById(R.id.clMenuList);
    }

    public TextView getTxtName() {
        return txtName;
    }

    public TextView getTxtDesc() {
        return txtDesc;
    }

    public TextView getTxtPrice() {
        return txtPrice;
    }

    public ImageView getImgMenuList() {
        return imgMenuList;
    }

    public LinearLayout getClMenuList() {
        return clMenuList;
    }
}

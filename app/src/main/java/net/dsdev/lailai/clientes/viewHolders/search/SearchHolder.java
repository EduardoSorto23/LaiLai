package net.dsdev.lailai.clientes.viewHolders.search;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import net.dsdev.lailai.clientes.R;

public class SearchHolder extends RecyclerView.ViewHolder {

    private TextView txtName, txtDesc, txtPrice;
    private ConstraintLayout clMenuList;

    public SearchHolder(@NonNull View itemView) {
        super(itemView);
        txtName = itemView.findViewById(R.id.txtMenuListNameSearch);
        txtDesc = itemView.findViewById(R.id.txtMenuListDescSearch);
        txtPrice = itemView.findViewById(R.id.txtMenuListPriceSearch);
        clMenuList = itemView.findViewById(R.id.clMenuListSearch);
    }

    public TextView getTxtName() {
        return txtName;
    }

    public void setTxtName(TextView txtName) {
        this.txtName = txtName;
    }

    public TextView getTxtDesc() {
        return txtDesc;
    }

    public void setTxtDesc(TextView txtDesc) {
        this.txtDesc = txtDesc;
    }

    public TextView getTxtPrice() {
        return txtPrice;
    }

    public void setTxtPrice(TextView txtPrice) {
        this.txtPrice = txtPrice;
    }

    public ConstraintLayout getClMenuList() {
        return clMenuList;
    }

    public void setClMenuList(ConstraintLayout clMenuList) {
        this.clMenuList = clMenuList;
    }
}

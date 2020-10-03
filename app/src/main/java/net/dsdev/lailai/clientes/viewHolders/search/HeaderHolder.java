package net.dsdev.lailai.clientes.viewHolders.search;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.dsdev.lailai.clientes.R;

public class HeaderHolder extends RecyclerView.ViewHolder {

    private TextView txtHeaderMenu;

    public HeaderHolder(@NonNull View itemView) {
        super(itemView);
        txtHeaderMenu = itemView.findViewById(R.id.txtHeaderMenu);
    }

    public TextView getTxtHeaderMenu() {
        return txtHeaderMenu;
    }

    public void setTxtHeaderMenu(TextView txtHeaderMenu) {
        this.txtHeaderMenu = txtHeaderMenu;
    }
}

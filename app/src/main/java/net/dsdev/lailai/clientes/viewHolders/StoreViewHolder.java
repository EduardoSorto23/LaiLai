package net.dsdev.lailai.clientes.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.dsdev.lailai.clientes.R;

public class StoreViewHolder extends RecyclerView.ViewHolder {

    private ImageView imgIcon;
    private TextView txtStoreName, txtStoreAddress,txtOpenHour,txtCloseHour;
    private LinearLayout llStore;

    public StoreViewHolder(@NonNull View itemView) {
        super(itemView);
        imgIcon = itemView.findViewById(R.id.imgDirection);
        txtStoreName = itemView.findViewById(R.id.txtStoreName);
        txtStoreAddress = itemView.findViewById(R.id.txtStoreAddress);
        txtOpenHour = itemView.findViewById(R.id.txtOpenHour);
        txtCloseHour = itemView.findViewById(R.id.txtCloseHour);
        llStore = itemView.findViewById(R.id.llStore);
    }

    public TextView getTxtStoreName() {
        return txtStoreName;
    }

    public void setTxtStoreName(TextView txtStoreName) {
        this.txtStoreName = txtStoreName;
    }

    public TextView getTxtStoreAddress() {
        return txtStoreAddress;
    }

    public void setTxtStoreAddress(TextView txtStoreAddress) {
        this.txtStoreAddress = txtStoreAddress;
    }

    public LinearLayout getLlStore() {
        return llStore;
    }

    public void setLlStore(LinearLayout llStore) {
        this.llStore = llStore;
    }

    public TextView getTxtOpenHour() {
        return txtOpenHour;
    }

    public void setTxtOpenHour(TextView txtOpenHour) {
        this.txtOpenHour = txtOpenHour;
    }

    public TextView getTxtCloseHour() {
        return txtCloseHour;
    }

    public void setTxtCloseHour(TextView txtCloseHour) {
        this.txtCloseHour = txtCloseHour;
    }
}

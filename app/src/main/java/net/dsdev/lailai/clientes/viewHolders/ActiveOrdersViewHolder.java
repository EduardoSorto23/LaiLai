package net.dsdev.lailai.clientes.viewHolders;

import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.dsdev.lailai.clientes.R;

public class ActiveOrdersViewHolder extends RecyclerView.ViewHolder{
    private TextView txtOrderNumber,txtOrderTime,txtOrderState,txtOrderTotal;
    private TableLayout tbLOrderTrack;
    public ActiveOrdersViewHolder(@NonNull View itemView) {
        super(itemView);
        txtOrderNumber = itemView.findViewById(R.id.txtOrderNumber);
        txtOrderTime = itemView.findViewById(R.id.txtOrderTime);
        txtOrderState = itemView.findViewById(R.id.txtOrderState);
        tbLOrderTrack = itemView.findViewById(R.id.tbLOrderTrack);
        txtOrderTotal = itemView.findViewById(R.id.txtOrderTotal);
    }

    public TextView getTxtOrderNumber() {
        return txtOrderNumber;
    }

    public void setTxtOrderNumber(TextView txtOrderNumber) {
        this.txtOrderNumber = txtOrderNumber;
    }

    public TextView getTxtOrderTime() {
        return txtOrderTime;
    }

    public void setTxtOrderTime(TextView txtOrderTime) {
        this.txtOrderTime = txtOrderTime;
    }

    public TextView getTxtOrderState() {
        return txtOrderState;
    }

    public void setTxtOrderState(TextView txtOrderState) {
        this.txtOrderState = txtOrderState;
    }

    public TableLayout getTbLOrderTrack() {
        return tbLOrderTrack;
    }

    public void setTbLOrderTrack(TableLayout tbLOrderTrack) {
        this.tbLOrderTrack = tbLOrderTrack;
    }

    public TextView getTxtOrderTotal() {
        return txtOrderTotal;
    }

    public void setTxtOrderTotal(TextView txtOrderTotal) {
        this.txtOrderTotal = txtOrderTotal;
    }
}

package net.dsdev.lailai.clientes.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.dsdev.lailai.clientes.R;

public class OrderDetailHolder extends RecyclerView.ViewHolder{
    private TextView txtOrderQuantity;
    private TextView txtOrderProduct;
    private TextView txtOrderDesc;
    private TextView txtOrderPrice;
    private TextView txtOrderTotal;
    private ImageView btnDeleteProcess;
    private TableRow tbRowOrderDetail;

    public OrderDetailHolder(@NonNull View itemView) {
        super(itemView);
        txtOrderQuantity = itemView.findViewById(R.id.txtOrderQuantity);
        txtOrderProduct = itemView.findViewById(R.id.txtOrderProduct);
        txtOrderDesc = itemView.findViewById(R.id.txtOrderDesc);
        txtOrderPrice = itemView.findViewById(R.id.txtOrderPrice);
        txtOrderTotal = itemView.findViewById(R.id.txtOrderTotal);
        btnDeleteProcess = itemView.findViewById(R.id.btnDeleteProcess);
        tbRowOrderDetail = itemView.findViewById(R.id.tbRowOrderDetail);
    }

    public TextView getTxtOrderQuantity() {
        return txtOrderQuantity;
    }

    public void setTxtOrderQuantity(TextView txtOrderQuantity) {
        this.txtOrderQuantity = txtOrderQuantity;
    }

    public TextView getTxtOrderProduct() {
        return txtOrderProduct;
    }

    public void setTxtOrderProduct(TextView txtOrderProduct) {
        this.txtOrderProduct = txtOrderProduct;
    }

    public TextView getTxtOrderDesc() {
        return txtOrderDesc;
    }

    public void setTxtOrderDesc(TextView txtOrderDesc) {
        this.txtOrderDesc = txtOrderDesc;
    }

    public TextView getTxtOrderPrice() {
        return txtOrderPrice;
    }

    public void setTxtOrderPrice(TextView txtOrderPrice) {
        this.txtOrderPrice = txtOrderPrice;
    }

    public TextView getTxtOrderTotal() {
        return txtOrderTotal;
    }

    public void setTxtOrderTotal(TextView txtOrderTotal) {
        this.txtOrderTotal = txtOrderTotal;
    }

    public ImageView getBtnDeleteProcess() {
        return btnDeleteProcess;
    }

    public void setBtnDeleteProcess(ImageView btnDeleteProcess) {
        this.btnDeleteProcess = btnDeleteProcess;
    }

    public TableRow getTbRowOrderDetail() {
        return tbRowOrderDetail;
    }

    public void setTbRowOrderDetail(TableRow tbRowOrderDetail) {
        this.tbRowOrderDetail = tbRowOrderDetail;
    }
}

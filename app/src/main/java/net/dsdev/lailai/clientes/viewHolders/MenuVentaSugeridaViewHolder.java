package net.dsdev.lailai.clientes.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.dsdev.lailai.clientes.R;

public class MenuVentaSugeridaViewHolder extends RecyclerView.ViewHolder {

    private TextView txtMenuDetailItem, txtMenuDetailPrice;
    private LinearLayout llContainer;
    private ImageView imgMenuListM;
    private RadioGroup rgMenuDetailItem;


    public MenuVentaSugeridaViewHolder(@NonNull View itemView) {
        super(itemView);
        txtMenuDetailItem = itemView.findViewById(R.id.txtMenuDetailItem);
        txtMenuDetailPrice = itemView.findViewById(R.id.txtMenuDetailPrice);
        imgMenuListM = itemView.findViewById(R.id.imgMenuList);
        rgMenuDetailItem = itemView.findViewById(R.id.rgMenuDetailItem);
        llContainer = itemView.findViewById(R.id.llContainer);
    }

    public TextView getTxtMenuDetailItem() {
        return txtMenuDetailItem;
    }

    public void setTxtMenuDetailItem(TextView txtMenuDetailItem) {
        this.txtMenuDetailItem = txtMenuDetailItem;
    }

    public LinearLayout getLlContainer() {
        return llContainer;
    }

    public void setLlContainer(LinearLayout llContainer) {
        this.llContainer = llContainer;
    }

    public ImageView getImgMenuListM() {
        return imgMenuListM;
    }

    public void setImgMenuListM(ImageView imgMenuListM) {
        this.imgMenuListM = imgMenuListM;
    }

    public RadioGroup getRgMenuDetailItem() {
        return rgMenuDetailItem;
    }

    public void setRgMenuDetailItem(RadioGroup rgMenuDetailItem) {
        this.rgMenuDetailItem = rgMenuDetailItem;
    }

    public TextView getTxtMenuDetailPrice() {
        return txtMenuDetailPrice;
    }

    public void setTxtMenuDetailPrice(TextView txtMenuDetailPrice) {
        this.txtMenuDetailPrice = txtMenuDetailPrice;
    }
}

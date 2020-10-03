package net.dsdev.lailai.clientes.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import net.dsdev.lailai.clientes.R;

public class DirectionViewHolder extends RecyclerView.ViewHolder {

    private ImageView imgIcon;
    private TextView txtDirectionName, txtDirection;
    private LinearLayout constraintLayout;

    public DirectionViewHolder(@NonNull View itemView) {
        super(itemView);
        txtDirection = itemView.findViewById(R.id.txtDirection);
        txtDirectionName = itemView.findViewById(R.id.txtDirectionName);
        imgIcon = itemView.findViewById(R.id.imgDirection);
        constraintLayout = itemView.findViewById(R.id.clDirection);
    }

    public ImageView getImgIcon() {
        return imgIcon;
    }

    public void setImgIcon(ImageView imgIcon) {
        this.imgIcon = imgIcon;
    }

    public TextView getTxtDirectionName() {
        return txtDirectionName;
    }

    public void setTxtDirectionName(TextView txtDirectionName) {
        this.txtDirectionName = txtDirectionName;
    }

    public TextView getTxtDirection() {
        return txtDirection;
    }

    public void setTxtDirection(TextView txtDirection) {
        this.txtDirection = txtDirection;
    }

    public LinearLayout getConstraintLayout() {
        return constraintLayout;
    }

    public void setConstraintLayout(LinearLayout constraintLayout) {
        this.constraintLayout = constraintLayout;
    }
}

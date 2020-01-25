package net.dsdev.lailai.clientes.viewHolders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.dsdev.lailai.clientes.R;

public class MenuDetailViewHolder extends RecyclerView.ViewHolder {

    private TextView txtQuestion;
    private RadioGroup rgMenuDetailOptions;
    private LinearLayout linearLayout;

    public MenuDetailViewHolder(@NonNull View itemView) {
        super(itemView);
        txtQuestion = itemView.findViewById(R.id.txtMenuDetailItem);
        rgMenuDetailOptions = itemView.findViewById(R.id.rgMenuDetailItem);
        linearLayout = itemView.findViewById(R.id.llContainer);
    }

    public TextView getTxtQuestion() {
        return txtQuestion;
    }

    public void setTxtQuestion(TextView txtQuestion) {
        this.txtQuestion = txtQuestion;
    }

    public RadioGroup getRgMenuDetailOptions() {
        return rgMenuDetailOptions;
    }

    public void setRgMenuDetailOptions(RadioGroup rgMenuDetailOptions) {
        this.rgMenuDetailOptions = rgMenuDetailOptions;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }
}

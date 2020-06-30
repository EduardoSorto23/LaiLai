package net.dsdev.lailai.clientes.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;

import net.dsdev.lailai.clientes.R;

public class CustomDialogSelectSweet extends DialogFragment implements View.OnClickListener {
    private Button btnAccept, btnCancel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.layout_custom_select_sweet,container,false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setTitle("Test");
        btnAccept = mView.findViewById(R.id.btnAccept);
        btnAccept.setOnClickListener(this);
        btnCancel = mView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAccept:
                //Toast.makeText(getActivity(),"Aceptar",Toast.LENGTH_LONG).show();
                //Navigation.findNavController(v).navigate(R.id.action_orderDetailFragment_to_directionsFragment);
                NavHostFragment.findNavController(this).navigate(R.id.action_orderDetailFragment_to_directionsFragment);
                dismiss();
                break;
            case R.id.btnCancel:
                //Toast.makeText(getActivity(),"Cancelar",Toast.LENGTH_LONG).show();
                dismiss();
                break;
            default:
                break;
        }
    }
}
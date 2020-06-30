package net.dsdev.lailai.clientes.view;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.NavHostFragment;

import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.fragments.OrderLastRevisionFragmentDirections;
import net.dsdev.lailai.clientes.model.Request.Order;
import net.dsdev.lailai.clientes.model.users.AuthResponse;
import net.dsdev.lailai.clientes.retrofit.RetrofitInstance;
import net.dsdev.lailai.clientes.retrofit.requests.OrderRequest;
import net.dsdev.lailai.clientes.util.AddOrRemoveCallbacks;
import net.dsdev.lailai.clientes.util.Globals;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomDialogConfirmation extends DialogFragment implements View.OnClickListener {

    Order finalOrder;
    private final String TAG = "Hola";
    OrderRequest orderRequest;
    Fragment fragment;
    SharedPreferencesMethods sharedPreferencesMethods;
    TextView txtAddress;
    View mView;
    ProgressDialog progressDialog;
    Boolean isAddress;
    private TextView lblAddress;
    public CustomDialogConfirmation(Order finalOrder, Fragment fragment, SharedPreferencesMethods sharedPreferencesMethods,Boolean isAddress) {
        this.finalOrder = finalOrder;
        this.fragment = fragment;
        this.sharedPreferencesMethods = sharedPreferencesMethods;
        this.isAddress = isAddress;
    }

    private Button btnAccept, btnCancel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.layout_custom_pop_up,container,false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getDialog().setTitle("Test");
        bindUI();
        if (isAddress) {
            lblAddress.setText("¿Estás seguro que deseas enviar a la siguiente dirección?");
            txtAddress.setText(sharedPreferencesMethods.getLoggedUser().getFinalAddress());
        } else {
            lblAddress.setText("¿Estás seguro que deseas recoger en la siguiente Tienda?");
            txtAddress.setText(Globals.tiendaName);
        }
        btnAccept = mView.findViewById(R.id.btnAccept);
        btnAccept.setOnClickListener(this);
        btnCancel = mView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        return mView;
    }

    private void bindUI() {
        txtAddress = mView.findViewById(R.id.txtAddress);
        lblAddress = mView.findViewById(R.id.lblAddress);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAccept:
                progressDialog = ProgressDialog.show(fragment.getActivity(), "Orden",
                        "Enviando orden", true);
                sendFinalOrder();
                //NavHostFragment.findNavController(this).navigate(R.id.action_orderLastRevisionFragment_to_thanksFragment);
                //Navigation.findNavController(v).navigate(R.id.action_orderLastRevisionFragment_to_thanksFragment);
                break;
            case R.id.btnCancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    private void sendFinalOrder() {
        orderRequest = RetrofitInstance.getRetrofitInstance().create(OrderRequest.class);
        Call<AuthResponse> call = orderRequest.sendFinalOrder(finalOrder);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                Log.d(TAG, "onResponse: response code last revision "+response.code());
                if (response.body() != null){
                    if (response.body().isValid()){
                        sharedPreferencesMethods.deleteMenusTree();
                        ((AddOrRemoveCallbacks) Objects.requireNonNull(getActivity())).onRemoveProduct();
                        Globals.OCASION="DOM";
                        Globals.tiendaName="";
                        Globals.tienda=null;
                        Globals.horaEntrega=null;
                        dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putString("orderCode",response.body().getOrderCode());
                        NavHostFragment.findNavController(fragment).navigate(R.id.action_orderLastRevisionFragment_to_thanksFragment,bundle);
                        //Navigation.findNavController(mView).navigate(R.id.action_orderLastRevisionFragment_to_thanksFragment,bundle);
                    }else{
                        try {
                            Toast.makeText(getActivity(),response.body().getMsg(),Toast.LENGTH_LONG).show();
                        }catch (Exception e){
                            Log.d(TAG, "onResponse: error al mostrar toast"+e.getMessage());
                        }
                        dismiss();
                    }
                }else{
                    Toast.makeText(getActivity(),"Error al enviar la orden. Favor intentar nuevamente",Toast.LENGTH_LONG).show();
                    dismiss();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: response code last revision "+t.getMessage());
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                Toast.makeText(getActivity(),"Error al enviar la orden. Favor intentar nuevamente",Toast.LENGTH_LONG).show();
                dismiss();
            }
        });
    }
}
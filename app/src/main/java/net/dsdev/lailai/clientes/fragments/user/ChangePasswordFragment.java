package net.dsdev.lailai.clientes.fragments.user;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import net.dsdev.lailai.clientes.MainActivity;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.model.users.Accounts;
import net.dsdev.lailai.clientes.retrofit.RetrofitInstance;
import net.dsdev.lailai.clientes.retrofit.users.UsersService;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment {


    public ChangePasswordFragment() {
    }
    private CardView cvChangeNewPassword;

    private View rootView;
    UsersService usersService;
    private String TAG = "Hola";
    private EditText txtPasswordActual,txtPasswordNew,txtPasswordConfirm;
    Context context;
    SharedPreferencesMethods sharedPreferencesMethods;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_change_password, container, false);
        bindUI();
        init();
        return rootView;
    }

    private void bindUI() {
        cvChangeNewPassword = rootView.findViewById(R.id.cvChangeNewPassword);
        txtPasswordActual = rootView.findViewById(R.id.txtPasswordActual);
        txtPasswordNew = rootView.findViewById(R.id.txtPasswordNew);
        txtPasswordConfirm = rootView.findViewById(R.id.txtPasswordConfirm);
    }
    Accounts responseUser = new Accounts();
    private void init() {
        sharedPreferencesMethods = new SharedPreferencesMethods(getActivity());
        context = getActivity().getApplicationContext();
        cvChangeNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                usersService = RetrofitInstance.getRetrofitInstance().create(UsersService.class);
                Call<Accounts> call = usersService.changePassword(new Accounts(sharedPreferencesMethods.getLoggedUser().getEmail(),txtPasswordActual.getText().toString(),txtPasswordNew.getText().toString()));
                call.enqueue(new Callback<Accounts>() {
                    @Override
                    public void onResponse(Call<Accounts> call, Response<Accounts> response) {
                        if (response.code() == 404 || response.code() == 500){
                            Log.d(TAG, "onResponse: code "+response.code() +" " + response.message());
                            return;
                        }
                        if(response.body() != null){
                            responseUser = response.body();
                            MaterialAlertDialogBuilder materialAlertDialogBuilder;
                            materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getActivity(),R.style.MyDialogThemeMaterial);
                            materialAlertDialogBuilder.setCancelable(false);
                            String tittle = responseUser.getMsg();
                            Log.d(TAG, "onResponse: responseUser.getMsg() "+responseUser.getMsg());
                            materialAlertDialogBuilder.setTitle(tittle);
                            //materialAlertDialogBuilder.setMessage(tittle);
                            materialAlertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (responseUser.isValid()){
                                        Navigation.findNavController(v).popBackStack();
                                    }
                                    dialog.dismiss();
                                }
                            });
                            materialAlertDialogBuilder.show();
                            Log.d(TAG, "onResponse: completado "+responseUser.isValid());
                        }else{
                            Log.d(TAG, "onResponse: response.body() = null "+ response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Accounts> call, Throwable t){
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
            }
        });
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //this.context = context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View include ;
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            include = activity.findViewById(R.id.include);
            include.setVisibility(View.GONE);
        }else{
            Log.d(TAG, "onActivityCreated: no obtiene el fab");
        }
    }
}

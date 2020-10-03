package net.dsdev.lailai.clientes.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import net.dsdev.lailai.clientes.MainActivity;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.model.cards.CardAuth;
import net.dsdev.lailai.clientes.model.users.AuthResponse;
import net.dsdev.lailai.clientes.retrofit.RetrofitInstance;
import net.dsdev.lailai.clientes.retrofit.cards.CardService;
import net.dsdev.lailai.clientes.util.RandomMethods;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewCreditCardFragment extends Fragment {
    private static final String TAG = "Hola";
    private TextInputEditText etCardName, etCardNumber, etCardCCV, etCardExpire;
    private TextInputLayout tilCardName, tilCardNumber, tilCardCCV, tilCardExpire;
    private MaterialButton btnSaveCard;
    View rootView;
    private String year, month, action;
    CardService cardService;
    SharedPreferencesMethods sharedPreferencesMethods;

    public NewCreditCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_new_credit_card, container, false);
        bind();
        setTextWatchers();
        expireFormat();
        init();
        return rootView;
    }

    private void bind(){
        etCardCCV = rootView.findViewById(R.id.etCardCCv);
        etCardExpire = rootView.findViewById(R.id.etCardExpire);
        etCardName = rootView.findViewById(R.id.etCardName);
        etCardNumber = rootView.findViewById(R.id.etCardNumber);
        tilCardCCV = rootView.findViewById(R.id.tilCardCCV);
        tilCardExpire = rootView.findViewById(R.id.tilCardExpire);
        tilCardName = rootView.findViewById(R.id.tilCardName);
        tilCardNumber = rootView.findViewById(R.id.tilCardNumber);
        btnSaveCard = rootView.findViewById(R.id.btnSaveCard);
    }

    private void init() {
        if (getArguments()!=null){
            action = getArguments().getString("action");
        }
        sharedPreferencesMethods = new SharedPreferencesMethods(getActivity());
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("New Credit Card");
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        }

        btnSaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!areFieldsValid()){
                    return;
                }
                String cardNumber =  etCardNumber.getText().toString().replaceAll("\\s","");
                String[] monthYear = etCardExpire.getText().toString().split("/");
                month = monthYear[0];
                year = monthYear[1];
                CardAuth cardAuth = new CardAuth(year, month, etCardName.getText().toString(), cardNumber, sharedPreferencesMethods.getLoggedUser().getIdCliente());
                cardService = RetrofitInstance.getRetrofitInstance().create(CardService.class);
                Call<AuthResponse> call = cardService.saveCard(cardAuth);
                call.enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.body()!=null){
                            if (response.body().isValid()){
                                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                                Bundle bundle = new Bundle();
                                bundle.putString("action",action);
                                NavHostFragment.findNavController(NewCreditCardFragment.this).navigate(R.id.action_newCreditCardFragment_to_paymentMethodFragment,bundle);
                            } else {
                                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Un error ha ocurrido. Favor intente nuevamente", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), "Un error ha ocurrido. Favor intente nuevamente", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    private void expireFormat(){

        etCardNumber.addTextChangedListener(new TextWatcher() {

            private boolean lock;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (lock || s.length() > 16) {
                    return;
                }
                lock = true;
                for (int i = 4; i < s.length(); i += 5) {
                    if (s.toString().charAt(i) != ' ') {
                        s.insert(i, " ");
                    }
                }
                lock = false;
            }
        });

        etCardExpire.addTextChangedListener(new TextWatcher() {
            private boolean lock;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (lock || s.length() > 5) {
                    return;
                }
                lock = true;
                for (int i = 2; i < s.length(); i += 3) {
                    if (s.toString().charAt(i) != '/') {
                        s.insert(i, "/");
                    }
                }
                lock = false;
            }
        });
    }

    private boolean areFieldsValid(){
        if (etCardNumber.getText().toString().isEmpty()){
            tilCardNumber.setError("Campo obligatorio");
            return false;
        }
        if (etCardExpire.getText().toString().isEmpty()){
            tilCardExpire.setError("Campo obligatorio");
            return false;
        }
        if (etCardCCV.getText().toString().isEmpty()){
            tilCardCCV.setError("Campo obligatorio");
            return false;
        }
        if (etCardNumber.getText().length()!=19){
            tilCardNumber.setError("Formato no válido");
            return false;
        }
        if (etCardName.getText().toString().isEmpty()){
            tilCardName.setError("Campo obligatorio");
            return false;
        }

        return true;
    }

    private void setTextWatchers(){
        RandomMethods.setTextWatcher(etCardCCV, tilCardCCV);
        RandomMethods.setTextWatcher(etCardExpire, tilCardExpire);
        RandomMethods.setTextWatcher(etCardName, tilCardName);
        RandomMethods.setTextWatcher(etCardNumber, tilCardNumber);
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

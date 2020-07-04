package net.dsdev.lailai.clientes.fragments;


import android.content.DialogInterface;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import net.dsdev.lailai.clientes.MainActivity;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.model.cards.CardAuth;
import net.dsdev.lailai.clientes.model.users.AuthResponse;
import net.dsdev.lailai.clientes.retrofit.RetrofitInstance;
import net.dsdev.lailai.clientes.retrofit.cards.CardService;
import net.dsdev.lailai.clientes.retrofit.users.AddressService;
import net.dsdev.lailai.clientes.util.RandomMethods;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCreditCardFragment extends Fragment implements View.OnClickListener {

    View rootView;
    TextInputEditText etCardNumber,etCardValidThru,etCardCvc,etCardName;
    MaterialButton btnCancel,btnEditCard,btnDeleteCard;
    CardAuth cardAuth = new CardAuth();
    String cardName="",cardNumber="",cvc="",cardExpire="";
    private TextInputLayout tilCardName, tilCardNumber, tilCardCCV, tilCardExpire;
    CardService cardService;
    private String year, month;
    private String TAG = "Hola";
    SharedPreferencesMethods sharedPreferencesMethods;
    public EditCreditCardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_edit_credit_card, container, false);
        bindUI();
        setTextWatchers();
        expireFormat();
        init();
        return rootView;
    }

    private void init() {
        sharedPreferencesMethods = new SharedPreferencesMethods(getActivity());
        cardService = RetrofitInstance.getRetrofitInstance().create(CardService.class);
        if (getArguments() != null){
            String cardAuthJson = getArguments().getString("cardAuth");
            cardAuth = new Gson().fromJson(cardAuthJson,CardAuth.class);
        }

        etCardName.setText(cardAuth.getOwnerName());
        etCardNumber.setText(cardAuth.getDocumentId());
        etCardValidThru.setText(cardAuth.getMonth().concat("/").concat(cardAuth.getYear()));

        btnEditCard.setOnClickListener(this);
        btnDeleteCard.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void bindUI() {
        etCardNumber = rootView.findViewById(R.id.etCardNumber);
        etCardValidThru = rootView.findViewById(R.id.etCardValidThru);
        etCardCvc = rootView.findViewById(R.id.etCardCvc);
        etCardName = rootView.findViewById(R.id.etCardName);
        btnCancel = rootView.findViewById(R.id.btnCancel);
        btnEditCard = rootView.findViewById(R.id.btnEditCard);
        btnDeleteCard = rootView.findViewById(R.id.btnDeleteCard);
        tilCardNumber = rootView.findViewById(R.id.tilCardNumber);
        tilCardExpire = rootView.findViewById(R.id.tilCardExpire);
        tilCardCCV = rootView.findViewById(R.id.tilCardCCV);
        tilCardName = rootView.findViewById(R.id.tilCardName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnEditCard:
                    if (!areFieldsValid()){
                        return;
                    }else{
                        editCard();
                    }
                break;
            case R.id.btnCancel:
                    cancel();
                break;
            case R.id.btnDeleteCard:
                    deleteCard();
                break;
            default:
                break;
        }
    }

    private void deleteCard() {
        Call<AuthResponse> call = cardService.deleteCards(cardAuth.getCardId());
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, final Response<AuthResponse> response) {
                if (response.body() != null){
                    new MaterialAlertDialogBuilder(getActivity(), R.style.MyDialogThemeMaterialLight)
                            .setTitle("Tarjeta")
                            .setMessage(response.body().getMsg())
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (response.body().isValid()){
                                        NavHostFragment.findNavController(EditCreditCardFragment.this).popBackStack();
                                    }else{
                                        dialogInterface.dismiss();
                                    }
                                }
                            })
                            .show();
                }else{
                    Toast.makeText(getActivity(), "Un error ha ocurrido. Favor intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Un error ha ocurrido. Favor intente nuevamente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancel() {
        NavHostFragment.findNavController(this).popBackStack();
    }

    private void editCard() {
        CardAuth cardAuthUpdate = new CardAuth();
        cardAuthUpdate.setCardId(cardAuth.getCardId());
        cardAuthUpdate.setDocumentId(etCardNumber.getText().toString().replaceAll("\\s",""));
        cardAuthUpdate.setOwnerName(etCardName.getText().toString());
        String[] monthYear = etCardValidThru.getText().toString().split("/");
        month = monthYear[0];
        year = monthYear[1];
        cardAuthUpdate.setMonth(month);
        cardAuthUpdate.setYear(year);
        cardAuthUpdate.setIdClient(sharedPreferencesMethods.getLoggedUser().getIdCliente());
        Log.d(TAG, "editCard: month "+month);
        Log.d(TAG, "editCard: year "+year);
        Call<AuthResponse> call = cardService.updateCard(cardAuthUpdate);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, final Response<AuthResponse> response) {
                if (response.body() != null){
                    new MaterialAlertDialogBuilder(getActivity(), R.style.MyDialogThemeMaterialLight)
                            .setTitle("Tarjeta")
                            .setMessage(response.body().getMsg())
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (response.body().isValid()){
                                        NavHostFragment.findNavController(EditCreditCardFragment.this).popBackStack();
                                    }else{
                                        dialogInterface.dismiss();
                                    }
                                }
                            })
                            .show();
                }else{
                    Log.d("Hola", "onResponse: "+response.message() +" "+response.code());
                    Toast.makeText(getActivity(), "Un error ha ocurrido. Favor intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.d("Hola", "onfailure for card delete: ");
                Toast.makeText(getActivity(), "Un error ha ocurrido. Favor intente nuevamente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean areFieldsValid(){
        if (etCardNumber.getText().toString().isEmpty()){
            tilCardNumber.setError("Campo obligatorio");
            return false;
        }
        if (etCardValidThru.getText().toString().isEmpty()){
            tilCardExpire.setError("Campo obligatorio");
            return false;
        }
        if (etCardCvc.getText().toString().isEmpty()){
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
        RandomMethods.setTextWatcher(etCardCvc, tilCardCCV);
        RandomMethods.setTextWatcher(etCardValidThru, tilCardExpire);
        RandomMethods.setTextWatcher(etCardName, tilCardName);
        RandomMethods.setTextWatcher(etCardNumber, tilCardNumber);
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

        etCardValidThru.addTextChangedListener(new TextWatcher() {
            private boolean lock;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*if (s.length()==2){
                    etCardValidThru.setText(s.toString().concat("/"));
                    month = s.toString();
                    etCardValidThru.setSelection(etCardValidThru.getText().length());
                }

                if (s.length()==5){
                    year = s.toString().substring(3,5);
                }*/
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

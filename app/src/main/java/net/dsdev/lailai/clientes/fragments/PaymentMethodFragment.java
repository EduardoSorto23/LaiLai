package net.dsdev.lailai.clientes.fragments;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.adapters.payment.PaymentMethodAdapter;
import net.dsdev.lailai.clientes.model.cards.CardAuth;
import net.dsdev.lailai.clientes.model.cards.CardList;
import net.dsdev.lailai.clientes.retrofit.RetrofitInstance;
import net.dsdev.lailai.clientes.retrofit.cards.CardService;
import net.dsdev.lailai.clientes.viewHolders.PaymentCardHolder;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentMethodFragment extends Fragment {


    public PaymentMethodFragment() {
        // Required empty public constructor
    }

    private static final String actionBarTittle = "Forma de pago";
    private static final String TAG = "Hola";
    RecyclerView rvCards;
    View rootView,viewCash_Card;
    PaymentMethodAdapter paymentMethodAdapter;
    FloatingActionButton fabAddCard;
    CardView cvCash;
    Bundle bundle;
    CardService cardService;
    SharedPreferencesMethods sharedPreferencesMethods;
    String action = "";
    LinearLayout llCash;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =inflater.inflate(R.layout.fragment_payment_method, container, false);
        bindUI();
        init();
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void bindUI(){
        rvCards = rootView.findViewById(R.id.rvCards);
        fabAddCard = rootView.findViewById(R.id.fabAddCard);
        cvCash = rootView.findViewById(R.id.cvCash);
        viewCash_Card = rootView.findViewById(R.id.viewCash_Card);
        llCash = rootView.findViewById(R.id.llCash);
    }
    private void init(){
        if (getArguments() != null) {
            String actionArg = getArguments().getString("action");
            if (actionArg != null) {
                action = actionArg;
            }
        }
        if (action.equalsIgnoreCase("profile")){
            cvCash.setVisibility(View.GONE);
            viewCash_Card.setVisibility(View.GONE);
            llCash.setVisibility(View.GONE);
        }

        sharedPreferencesMethods = new SharedPreferencesMethods(getActivity());
        bundle = new Bundle();
        cvCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: cash");
                bundle.putString("paymentMethod","EFE");
                goTo(v,R.id.action_paymentMethodFragment_to_orderLastRevisionFragment, bundle);
            }
        });

        paymentMethodAdapter = new PaymentMethodAdapter(getActivity().getApplicationContext()) {
            @Override
            public void setClickListener(PaymentCardHolder holder, final CardAuth cardAuth) {
                holder.getCardItem().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick: card ");
                        bundle.putString("paymentMethod","CRD");
                        if (action.equalsIgnoreCase("profile")){
                            bundle.putString("action","profile");
                            bundle.putString("cardAuth", new Gson().toJson(cardAuth));
                            goTo(v,R.id.action_paymentMethodFragment_to_editCreditCardFragment,bundle);
                        }else{
                            showCCV(v,cardAuth);
                        }
                    }
                });
            }
        };

        fabAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("action",action);
                goTo(v,R.id.action_paymentMethodFragment_to_newCreditCardFragment, bundle);
            }
        });

        //paymentMethodAdapter.setCount(3);


        cardService = RetrofitInstance.getRetrofitInstance().create(CardService.class);
        Call<CardList> call = cardService.getCards(sharedPreferencesMethods.getLoggedUser().getIdCliente());
        call.enqueue(new Callback<CardList>() {
            @Override
            public void onResponse(Call<CardList> call, Response<CardList> response) {
                if (response.body()!=null){
                    paymentMethodAdapter.setCardList(response.body().getCards());
                    rvCards.setAdapter(paymentMethodAdapter);
                    if (getActivity()!=null){
                        rvCards.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                    }
                }
            }

            @Override
            public void onFailure(Call<CardList> call, Throwable t) {

            }
        });

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(actionBarTittle);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                    NavHostFragment.findNavController(this).navigate(R.id.action_paymentMethodFragment2_to_orderDetailFragment);
                return true;

            default:
                item.setVisible(false);
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.clear();
        super.onPrepareOptionsMenu(menu);
    }

    private void goTo(View v, int id, Bundle bundle){
        Log.d(TAG, "goTo: view : "+v + " id "+id);
        Navigation.findNavController(v).navigate(id, bundle);
    }

    private void showCCV(final View v, final CardAuth cardAuth){
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setMaxLines(1);
        input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(3)});
        input.requestFocus();
        new MaterialAlertDialogBuilder(getActivity(), R.style.MyDialogTheme)
                .setTitle("Tarjeta")
                .setMessage("Por favor ingrese el número CCV. \nSon los últimos 3 dígitos al reverso de su tarjeta")
                .setView(input)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!input.getText().toString().isEmpty() && input.getText().toString().length()==3){
                            CardAuth cardAuth1 = cardAuth;
                            cardAuth1.setCcv(Integer.valueOf(input.getText().toString()));
                            bundle.putString("cardAuth", new Gson().toJson(cardAuth1));
                            goTo(v,R.id.action_paymentMethodFragment_to_orderLastRevisionFragment, bundle);
                        }else{
                            Toast.makeText(getActivity(), "Favor ingrese un numero de CCV válido", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNeutralButton("Cancelar",null)
                .show();
    }

}

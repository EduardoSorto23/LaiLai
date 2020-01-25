package net.dsdev.lailai.clientes.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.dsdev.lailai.clientes.MainActivity;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.adapters.DirectionAdapter;
import net.dsdev.lailai.clientes.model.users.Address;
import net.dsdev.lailai.clientes.model.users.AddressResponse;
import net.dsdev.lailai.clientes.model.users.Client;
import net.dsdev.lailai.clientes.retrofit.RetrofitInstance;
import net.dsdev.lailai.clientes.retrofit.users.AddressService;
import net.dsdev.lailai.clientes.viewHolders.DirectionViewHolder;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class DirectionsFragment extends Fragment {

    View rootView;
    RecyclerView recyclerView;
    DirectionAdapter directionAdapter;
    AddressResponse addresses;
    FloatingActionButton fab;
    private static final String actionBarTittle = "Direcciones";
    private static final String TAG = "Hola";
    AddressService addressService;
    Call<AddressResponse> call;
    String action = "";
    SharedPreferencesMethods sharedPreferencesMethods;
    public DirectionsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_directions, container, false);
        bindUI();
        init();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAddresses();
    }

    private void bindUI(){
        recyclerView = rootView.findViewById(R.id.rvDirections);
        fab = rootView.findViewById(R.id.fabDirections);
    }

    public void init(){
        sharedPreferencesMethods = new SharedPreferencesMethods(getActivity());
        if (getArguments() != null) {
            String actionArg = getArguments().getString("action");
            if (actionArg != null) {
                action = actionArg;
            }
        }
        try {
            getAddresses();
        }
        catch (Exception e){
            Log.d(TAG, "init: error al iniciar getAddresses "+e.getMessage());
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_directionsFragment_to_newDirectionFragment);
            }
        });

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(actionBarTittle);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
            //actionBar.setHomeAsUpIndicator(android.R.drawable.ic_delete);
        }
        setHasOptionsMenu(true);

    }

    private void getAddresses() {
        addresses = new AddressResponse();
        addressService = RetrofitInstance.getRetrofitInstance().create(AddressService.class);
        call = addressService.listDirections(sharedPreferencesMethods.getLoggedUser().getIdCliente());
        call.enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                Log.d(TAG, "onResponse:  al obtener el listado de direcciones");
                if (response != null
                        && response.body() != null
                        && response.body().getAddresses()!= null
                        && response.body().getAddresses().size()>0
                ) {
                    addresses.setAddresses(response.body().getAddresses());
                    initAdapter();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"No hay direcciones disponibles favor añadir una",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: al obtener el listado de direcciones");
                Toast.makeText(getActivity().getApplicationContext(),"No se ha podido obtener direcciones disponibles. \n Favor intente mas tarde",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View include ;
        if ((getActivity()) instanceof MainActivity){
            MainActivity activity = (MainActivity) getActivity();
            include = activity.findViewById(R.id.include);
            include.setVisibility(View.GONE);
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
            actionBar.setTitle(actionBarTittle);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            //actionBar.setLogo(R.drawable.logo_toolbar);
        }else{
            Log.d(TAG, "onActivityCreated: no obtiene el fab");
        }
    }

    private void initAdapter(){
        directionAdapter = new DirectionAdapter(getActivity().getApplicationContext()) {
            @Override
            public void setClickListener(DirectionViewHolder holder, final Address address) {
                holder.getConstraintLayout().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("address",new Gson().toJson(address));
                        Client client = sharedPreferencesMethods.getLoggedUser();
                        client.setIdAddress(address.getId());
                        client.setTelephone(address.getTelephone());
                        client.setFinalAddress(address.getIndications().concat(", ").concat(address.getMunicipaly()).concat(", ").concat(address.getDepartment()));
                        if (action.equalsIgnoreCase("profile")){
                            Navigation.findNavController(v).navigate(R.id.action_directionsFragment_to_editDirectionFragment,bundle);
                        }else if (action.equalsIgnoreCase("order")){
                            sharedPreferencesMethods.saveLoggedUser(client);
                            Navigation.findNavController(v).navigate(R.id.action_directionsFragment_to_orderLastRevisionFragment,bundle);
                        }else {
                            sharedPreferencesMethods.saveLoggedUser(client);
                            Navigation.findNavController(v).navigate(R.id.action_directionsFragment_to_paymentMethodFragment2);
                        }
                    }
                });
            }
        };
        directionAdapter.setDirections(addresses.getAddresses());
        recyclerView.setAdapter(directionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
    }

}

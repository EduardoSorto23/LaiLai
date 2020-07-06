package net.dsdev.lailai.clientes.fragments;


import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import net.dsdev.lailai.clientes.MainActivity;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.adapters.DirectionAdapter;
import net.dsdev.lailai.clientes.adapters.StoreAdapter;
import net.dsdev.lailai.clientes.model.Store;
import net.dsdev.lailai.clientes.model.StoreList;
import net.dsdev.lailai.clientes.model.users.Address;
import net.dsdev.lailai.clientes.model.users.AddressResponse;
import net.dsdev.lailai.clientes.model.users.Client;
import net.dsdev.lailai.clientes.retrofit.RetrofitInstance;
import net.dsdev.lailai.clientes.retrofit.users.AddressService;
import net.dsdev.lailai.clientes.retrofit.users.StoreService;
import net.dsdev.lailai.clientes.util.Globals;
import net.dsdev.lailai.clientes.viewHolders.DirectionViewHolder;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;
import net.dsdev.lailai.clientes.viewHolders.StoreViewHolder;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class DirectionsFragment extends Fragment implements View.OnClickListener {

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
    private Boolean isTimeSelected = false;
    private int hourSelected,minuteSelected;
    TimePickerDialog mTimePicker;
    MaterialButton btnTimePicker;
    private LinearLayout layoutTimePicker,llView;
    MaterialButtonToggleGroup toggleGroupAddress;
    Button btnDom,btnRec;
    private TextView lblOcasion;
    private StoreAdapter storeAdapter;
    private List<Store> stores;
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
        //getAddresses();
    }

    private void bindUI(){
        recyclerView = rootView.findViewById(R.id.rvDirections);
        fab = rootView.findViewById(R.id.fabDirections);
        btnTimePicker = rootView.findViewById(R.id.btnTimePicker);
        layoutTimePicker = rootView.findViewById(R.id.layoutTimePicker);
        toggleGroupAddress = rootView.findViewById(R.id.toggleGroupAddress);
        btnDom = rootView.findViewById(R.id.btnDom);
        btnRec = rootView.findViewById(R.id.btnRec);
        lblOcasion = rootView.findViewById(R.id.lblOcasion);
        llView = rootView.findViewById(R.id.llView);
    }

    public void init(){
        sharedPreferencesMethods = new SharedPreferencesMethods(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setNestedScrollingEnabled(false);
        btnTimePicker.setOnClickListener(this);
        Calendar mcurrentTime = Calendar.getInstance();
        hourSelected = mcurrentTime.get(Calendar.HOUR);
        minuteSelected = mcurrentTime.get(Calendar.MINUTE);
        toggleGroupAddress.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked){
                    if (checkedId == R.id.btnDom){
                        fab.setVisibility(View.VISIBLE);
                        lblOcasion.setText("Direcciones:");
                        recyclerView.setAdapter(null);
                        getAddresses();
                    }else if (checkedId == R.id.btnRec){
                        fab.setVisibility(View.GONE);
                        lblOcasion.setText("Tiendas:");
                        recyclerView.setAdapter(null);
                        initStoresAdapter();
                    }
                }
            }
        });
        if (getArguments() != null) {
            String actionArg = getArguments().getString("action");
            if (actionArg != null) {
                action = actionArg;
            }
        }
        try {
            if (action.equalsIgnoreCase("profile")){
                layoutTimePicker.setVisibility(View.GONE);
                llView.setVisibility(View.GONE);
                toggleGroupAddress.setVisibility(View.GONE);
                getAddresses();
            }else{
                if (Globals.OCASION!=null) {
                    if (Globals.OCASION.equalsIgnoreCase("DOM") || !Globals.OCASION.equalsIgnoreCase("LLV")) {
                        toggleGroupAddress.check(R.id.btnDom);
                    } else if (Globals.OCASION.equalsIgnoreCase("LLV")) {
                        toggleGroupAddress.check(R.id.btnRec);
                    }
                }else{
                    toggleGroupAddress.check(R.id.btnDom);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        /*try {
            getAddresses();
        }
        catch (Exception e){
            Log.d(TAG, "init: error al iniciar getAddresses "+e.getMessage());
        }*/

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

    private void initStoresAdapter() {
        storeAdapter = new StoreAdapter(getContext()) {
            @Override
            public void setClickListener(StoreViewHolder holder, final Store store) {
                holder.getLlStore().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isTimeSelected){
                            Toast.makeText(getActivity(),"Seleccione una hora",Toast.LENGTH_LONG).show();
                        }else{
                            Client client = sharedPreferencesMethods.getLoggedUser();
                            client.setIdAddress(0L);
                            client.setTelephone(null);
                            client.setFinalAddress(null);
                            sharedPreferencesMethods.saveLoggedUser(client);
                            String f = String.format("%d:%d", hourSelected,minuteSelected);
                            Globals.horaEntrega = f;
                            Globals.tienda = store.getId();
                            Globals.tiendaName = store.getName();
                            Globals.OCASION = "LLV";
                            //Navigation.findNavController(v).navigate(R.id.action_directionsFragment_to_paymentMethodFragment2);
                            if (action.equalsIgnoreCase("order")){
                                Bundle bundle = new Bundle();
                                bundle.putString("paymentMethod",getArguments().getString("paymentMethod", "EFE"));
                                bundle.putString("cardAuth",getArguments().getString("cardAuth",""));
                                Navigation.findNavController(v).navigate(R.id.action_directionsFragment_to_orderLastRevisionFragment,bundle);
                            }else {
                                Navigation.findNavController(v).navigate(R.id.action_directionsFragment_to_paymentMethodFragment2);
                            }
                        }
                    }
                });

            }
        };
        stores = new ArrayList<>();
        storeAdapter.setDirections(stores);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(storeAdapter);
        getAllStores();
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
                    initAddressAdapter();
                }else{
                    if (getActivity()!=null){
                        Toast.makeText(getActivity(),"No hay direcciones disponibles favor añadir una",Toast.LENGTH_LONG).show();
                    }
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

    private void initAddressAdapter(){
        directionAdapter = new DirectionAdapter(getActivity()) {
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
                        client.setFinalAddress(address.getReferences().concat(", ").concat(address.getoMunicipaly().getNombre()).concat(", ").concat(address.getoDepartment().getNombre()));
                        if (action.equalsIgnoreCase("profile")){
                            Navigation.findNavController(v).navigate(R.id.action_directionsFragment_to_editDirectionFragment,bundle);
                        }else if (action.equalsIgnoreCase("order")){
                            sharedPreferencesMethods.saveLoggedUser(client);
                            String f = String.format("%d:%d", hourSelected,minuteSelected);
                            Globals.horaEntrega = f;
                            Globals.tienda = null;
                            Globals.OCASION = "DOM";
                            bundle.putString("paymentMethod",getArguments().getString("paymentMethod", "EFE"));
                            bundle.putString("cardAuth",getArguments().getString("cardAuth",""));
                            Navigation.findNavController(v).navigate(R.id.action_directionsFragment_to_orderLastRevisionFragment,bundle);
                        }else {
                            sharedPreferencesMethods.saveLoggedUser(client);
                            String f = String.format("%d:%d", hourSelected,minuteSelected);
                            Globals.horaEntrega = f;
                            Globals.tienda = null;
                            Globals.OCASION = "DOM";
                            Navigation.findNavController(v).navigate(R.id.action_directionsFragment_to_paymentMethodFragment2);
                        }
                    }
                });
            }
        };
        directionAdapter.setDirections(addresses.getAddresses());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(directionAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnTimePicker:
                showTimePicker();
                break;
        }
    }
    private void showTimePicker() {
        mTimePicker = new TimePickerDialog(getActivity(),R.style.MyTimePickerDialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String f = String.format("%d:%d", selectedHour, selectedMinute);
                isTimeSelected = true;
                hourSelected = selectedHour;
                minuteSelected = selectedMinute;
                btnTimePicker.setText(f);
            }
        }, hourSelected,minuteSelected, false);//Yes 24 hour time
        mTimePicker.show();
    }
    private void getAllStores() {
        StoreService storeService = RetrofitInstance.getRetrofitInstance().create(StoreService.class);
        Call<StoreList> call = storeService.getStores();
        call.enqueue(new Callback<StoreList>() {
            @Override
            public void onResponse(Call<StoreList> call, Response<StoreList> response) {
                String msg = "Error al obtener las tiendas";
                if (response.body()!=null){
                    if (response.body().getStores()!=null){
                        stores = response.body().getStores();
                        storeAdapter.setDirections(stores);
                    }else{
                        msg = "No se encontraron tiendas";
                    }
                }
                Log.d(TAG, "onResponse: "+msg);
            }

            @Override
            public void onFailure(Call<StoreList> call, Throwable t) {
                String msg = "Error al obtener las ordenes";
                Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
                Log.d(TAG, "onFailure: "+msg+t.getMessage());
            }
        });
    }

}

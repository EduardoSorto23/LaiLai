package net.dsdev.lailai.clientes.fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
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
import net.dsdev.lailai.clientes.model.users.Coverage;
import net.dsdev.lailai.clientes.retrofit.RetrofitInstance;
import net.dsdev.lailai.clientes.retrofit.users.AddressService;
import net.dsdev.lailai.clientes.retrofit.users.StoreService;
import net.dsdev.lailai.clientes.util.Constants;
import net.dsdev.lailai.clientes.util.Globals;
import net.dsdev.lailai.clientes.viewHolders.DirectionViewHolder;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;
import net.dsdev.lailai.clientes.viewHolders.StoreViewHolder;

import com.google.android.gms.maps.MapView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private Boolean isDateSelected = false;
    private int hourSelected,minuteSelected,yearSelected,monthSelected,daySelected;
    private Date dateSelected;
    TimePickerDialog mTimePicker;
    DatePickerDialog mDatePicker;
    MaterialButton btnTimePicker, btnDatePicker;
    private LinearLayout layoutTimePicker,llView;
    MaterialButtonToggleGroup toggleGroupAddress;
    Button btnDom,btnRec;
    private EditText etSearch;
    private TextView lblOcasion, txtFechaFutura;
    private StoreAdapter storeAdapter;
    private List<Store> stores;
    private List<Store> storesFilter;
    private RadioGroup rgPedido;
    private RadioButton rbPedidoNormal;
    private RadioButton rbPedidoFuturo;
    private Calendar mcurrentTime;
    private String pedidoFuturo;
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
        etSearch = rootView.findViewById(R.id.etSearch);
        rbPedidoFuturo = rootView.findViewById(R.id.rbPedidoFuturo);
        rbPedidoNormal = rootView.findViewById(R.id.rbPedidoNormal);
        rgPedido = rootView.findViewById(R.id.rgPedido);
        txtFechaFutura = rootView.findViewById(R.id.txtFechaFutura);
        btnDatePicker = rootView.findViewById(R.id.btnDatePicker);
    }

    public void init(){
        sharedPreferencesMethods = new SharedPreferencesMethods(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setNestedScrollingEnabled(false);
        btnTimePicker.setOnClickListener(this);
        mcurrentTime = Calendar.getInstance();
        hourSelected = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        minuteSelected = mcurrentTime.get(Calendar.MINUTE);
        toggleGroupAddress.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked){
                    if (checkedId == R.id.btnDom){
                        fab.setVisibility(View.VISIBLE);
                        lblOcasion.setText("Direcciones:");
                        recyclerView.setAdapter(null);
                        etSearch.setVisibility(View.GONE);
                        getAddresses();
                    }else if (checkedId == R.id.btnRec){
                        fab.setVisibility(View.GONE);
                        lblOcasion.setText("Tiendas:");
                        recyclerView.setAdapter(null);
                        etSearch.setVisibility(View.VISIBLE);
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
                etSearch.setVisibility(View.GONE);
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

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtarTiendas(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //Funcionalidad radioGroup Pedido Futuro
        rgPedido.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbFunction(checkedId);
            }
        });

        //Ocultando o mostrando el control segun caso
        if (rbPedidoNormal.isChecked()){
            rbFunction(R.id.rbPedidoNormal);
        }else if(rbPedidoFuturo.isChecked()){
            rbFunction(R.id.rbPedidoFuturo);
        }

        btnDatePicker.setOnClickListener(this);
        dateSelected = mcurrentTime.getTime();
        yearSelected = mcurrentTime.get(Calendar.YEAR);
        monthSelected = mcurrentTime.get(Calendar.MONTH)+1;
        daySelected = mcurrentTime.get(Calendar.DAY_OF_MONTH);
        mcurrentTime.set(Calendar.MINUTE,mcurrentTime.get(Calendar.MINUTE)-1);
    }

    private void rbFunction(int checkedId) {
        if(checkedId == R.id.rbPedidoNormal){
            //Desactivando el control de hora y fecha
            btnTimePicker.setVisibility(View.GONE);
            btnTimePicker.setEnabled(false);
            btnDatePicker.setVisibility(View.GONE);
            btnDatePicker.setEnabled(false);
            txtFechaFutura.setVisibility(View.GONE);
            pedidoFuturo = "N";
        } else if(checkedId == R.id.rbPedidoFuturo){
            //Activando y mostrando los controles de fecha y hora futura
            btnTimePicker.setVisibility(View.VISIBLE);
            btnTimePicker.setEnabled(true);
            btnDatePicker.setVisibility(View.VISIBLE);
            btnDatePicker.setEnabled(true);
            txtFechaFutura.setVisibility(View.VISIBLE);
            pedidoFuturo = "S";
        }
    }

    private void filtarTiendas(CharSequence s){
        try {
            storesFilter = new ArrayList<>();
            for(Store item: stores){
                if(item.getName().toUpperCase().contains(s.toString().toUpperCase()) || item.getAddress().toUpperCase().contains(s.toString().toUpperCase())){
                    storesFilter.add(item);
                }
            }

            storeAdapter.setDirections(storesFilter);
            storeAdapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.d(TAG,"Error al aplicar filtrarTiendas: " + e);
        }
    }

    private void initStoresAdapter() {
        storeAdapter = new StoreAdapter(getContext()) {
            @Override
            public void setClickListener(StoreViewHolder holder, final Store store) {
                holder.getLlStore().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isTimeSelected && pedidoFuturo.equals("S")) {
                            Toast.makeText(getActivity(), "Seleccione una hora", Toast.LENGTH_LONG).show();
                        }else if(!isDateSelected && pedidoFuturo.equals("S")){
                            Toast.makeText(getActivity(), "Seleccione una fecha", Toast.LENGTH_LONG).show();
                        }else{
                            try {
                                if(pedidoFuturo.equals("N")){
                                    yearSelected = mcurrentTime.get(Calendar.YEAR);
                                    monthSelected = mcurrentTime.get(Calendar.MONTH)+1;
                                    daySelected = mcurrentTime.get(Calendar.DAY_OF_MONTH);
                                    hourSelected = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                                    minuteSelected = mcurrentTime.get(Calendar.MINUTE);
                                }
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                String cadenaFec = yearSelected + "-" + monthSelected + "-" + daySelected + " " + store.getOpenHour();
                                Date horarioIni = sdf.parse(cadenaFec);
                                cadenaFec = yearSelected + "-" + monthSelected + "-" + daySelected + " " + store.getCloseHour();
                                Date horarioFin = sdf.parse(cadenaFec);
                                Calendar fecHora = Calendar.getInstance();
                                fecHora.set(Calendar.YEAR, yearSelected);
                                fecHora.set(Calendar.MONTH, monthSelected - 1);
                                fecHora.set(Calendar.DAY_OF_MONTH, daySelected);
                                fecHora.set(Calendar.HOUR_OF_DAY, hourSelected);
                                fecHora.set(Calendar.MINUTE, minuteSelected);
                                if (fecHora.getTime().before(horarioIni) || fecHora.getTime().after(horarioFin)) {
                                    Toast.makeText(getActivity(), "Lo sentimos, esta tienda esta cerrada. Por favor, intenta nuevamente mas tarde", Toast.LENGTH_LONG).show();
                                }else if(fecHora.getTime().before(mcurrentTime.getTime()) && pedidoFuturo.equals("S")){
                                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    String fechaActual = sdf2.format(mcurrentTime.getTime());
                                    Toast.makeText(getActivity(), "Se selecciono una fecha y hora pasada. Por favor, selecciona una fecha y hora mayor al " + fechaActual, Toast.LENGTH_LONG).show();
                                }else{
                                    Client client = sharedPreferencesMethods.getLoggedUser();
                                    client.setIdAddress(0L);
                                    client.setTelephone(null);
                                    client.setFinalAddress(null);
                                    sharedPreferencesMethods.saveLoggedUser(client);
                                    String f = String.format("%d:%d", hourSelected,minuteSelected);
                                    //String f2 = String.format("%d/%d/%d",yearSelected,monthSelected,daySelected);
                                    Calendar fecHora2 = Calendar.getInstance();
                                    fecHora2.set(Calendar.YEAR,yearSelected);
                                    fecHora2.set(Calendar.MONTH, monthSelected-1);
                                    fecHora2.set(Calendar.DAY_OF_MONTH, daySelected);
                                    fecHora2.set(Calendar.HOUR_OF_DAY, hourSelected);
                                    fecHora2.set(Calendar.MINUTE, minuteSelected);
                                    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    String cadenaFecHora2 = sdf3.format(fecHora2.getTime());
                                    Globals.horaEntrega = f;
                                    Globals.fechaEntrega = cadenaFecHora2;
                                    Globals.tienda = store.getId();
                                    Globals.tiendaName = store.getName();
                                    Globals.OCASION = "LLV";
                                    if (action.equalsIgnoreCase("order")){
                                        Bundle bundle = new Bundle();
                                        bundle.putString("paymentMethod",getArguments().getString("paymentMethod", "EFE"));
                                        bundle.putString("cardAuth",getArguments().getString("cardAuth",""));
                                        Navigation.findNavController(v).navigate(R.id.action_directionsFragment_to_orderLastRevisionFragment,bundle);
                                    }else {
                                        Navigation.findNavController(v).navigate(R.id.action_directionsFragment_to_paymentMethodFragment2);
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
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
                    public void onClick(final View v) {
                        //Validando la cobertura y actuando segun el caso
                        if (!isTimeSelected && pedidoFuturo.equals("S")) {
                            Toast.makeText(getActivity(), "Seleccione una hora", Toast.LENGTH_LONG).show();
                        }else if(!isDateSelected && pedidoFuturo.equals("S")){
                            Toast.makeText(getActivity(), "Seleccione una fecha", Toast.LENGTH_LONG).show();
                        }else{
                            final Coverage covertura = new Coverage();
                            Calendar fecHora = Calendar.getInstance();
                            fecHora.set(Calendar.YEAR,yearSelected);
                            fecHora.set(Calendar.MONTH, monthSelected-1);
                            fecHora.set(Calendar.DAY_OF_MONTH, daySelected);
                            fecHora.set(Calendar.HOUR_OF_DAY, hourSelected);
                            fecHora.set(Calendar.MINUTE, minuteSelected);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            String cadenaFecHora = sdf.format(fecHora.getTime());
                            StoreService storeService = RetrofitInstance.getRetrofitInstance().create(StoreService.class);
                            Call<Coverage> call;
                            if(pedidoFuturo.equals("S")){
                                call = storeService.getCoverage(address.getId(),pedidoFuturo,cadenaFecHora);
                            }else{
                                cadenaFecHora = sdf.format(mcurrentTime.getTime());
                                yearSelected = mcurrentTime.get(Calendar.YEAR);
                                monthSelected = mcurrentTime.get(Calendar.MONTH)+1;
                                daySelected = mcurrentTime.get(Calendar.DAY_OF_MONTH);
                                hourSelected = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                                minuteSelected = mcurrentTime.get(Calendar.MINUTE);
                                call = storeService.getCoverage(address.getId(),pedidoFuturo,cadenaFecHora);
                            }
                            if(fecHora.getTime().before(mcurrentTime.getTime()) && pedidoFuturo.equals("S")){
                                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                String fechaActual = sdf2.format(mcurrentTime.getTime());
                                Toast.makeText(getActivity(), "Se selecciono una fecha y hora pasada. Por favor, selecciona una fecha y hora mayor al " + fechaActual, Toast.LENGTH_LONG).show();
                            }else{
                                call.enqueue(new Callback<Coverage>() {
                                    @Override
                                    public void onResponse(Call<Coverage> call, Response<Coverage> response) {
                                        Log.d(TAG, "onResponse:  al obtener la covertura de la tienda");
                                        if (response != null
                                                && response.body() != null
                                                && response.body().getState() != null
                                        ) {
                                            if (response.body().getStores() != null) {
                                                covertura.setStores(response.body().getStores());
                                            }
                                            if(response.body().getMessage() != null){
                                                covertura.setMessage(response.body().getMessage());
                                            }
                                            covertura.setId(response.body().getId());
                                            covertura.setState(response.body().getState());
                                            if (covertura.getState().equals(Constants.stateCoverageClose) || covertura.getState().equals(Constants.stateCoverageOut)) {
                                                Toast.makeText(getActivity().getApplicationContext(), covertura.getMessage(), Toast.LENGTH_LONG).show();
                                            }else if(covertura.getState().equals(Constants.stateCoverageOpen)){
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
                                                    //String f2 = String.format("%d/%d/%d",yearSelected,monthSelected,daySelected);
                                                    Calendar fecHora2 = Calendar.getInstance();
                                                    fecHora2.set(Calendar.YEAR,yearSelected);
                                                    fecHora2.set(Calendar.MONTH, monthSelected-1);
                                                    fecHora2.set(Calendar.DAY_OF_MONTH, daySelected);
                                                    fecHora2.set(Calendar.HOUR_OF_DAY, hourSelected);
                                                    fecHora2.set(Calendar.MINUTE, minuteSelected);
                                                    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                                    String cadenaFecHora2 = sdf3.format(fecHora2.getTime());
                                                    Globals.horaEntrega = f;
                                                    Globals.fechaEntrega = cadenaFecHora2;
                                                    Globals.tienda = null;
                                                    Globals.OCASION = "DOM";
                                                    bundle.putString("paymentMethod",getArguments().getString("paymentMethod", "EFE"));
                                                    bundle.putString("cardAuth",getArguments().getString("cardAuth",""));
                                                    Navigation.findNavController(v).navigate(R.id.action_directionsFragment_to_orderLastRevisionFragment,bundle);
                                                }else {
                                                    sharedPreferencesMethods.saveLoggedUser(client);
                                                    String f = String.format("%d:%d", hourSelected,minuteSelected);
                                                    //String f2 = String.format("%d/%d/%d",yearSelected,monthSelected,daySelected);
                                                    Calendar fecHora2 = Calendar.getInstance();
                                                    fecHora2.set(Calendar.YEAR,yearSelected);
                                                    fecHora2.set(Calendar.MONTH, monthSelected-1);
                                                    fecHora2.set(Calendar.DAY_OF_MONTH, daySelected);
                                                    fecHora2.set(Calendar.HOUR_OF_DAY, hourSelected);
                                                    fecHora2.set(Calendar.MINUTE, minuteSelected);
                                                    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                                    String cadenaFecHora2 = sdf3.format(fecHora2.getTime());
                                                    Globals.horaEntrega = f;
                                                    Globals.fechaEntrega = cadenaFecHora2;
                                                    Globals.tienda = null;
                                                    Globals.OCASION = "DOM";
                                                    Navigation.findNavController(v).navigate(R.id.action_directionsFragment_to_paymentMethodFragment2);
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Coverage> call, Throwable t) {
                                        Log.d(TAG, "onFailure: al obtener la covertura de la direccion");
                                        Toast.makeText(getActivity().getApplicationContext(),"No se ha podido obtener la covertura. \n Favor intente mas tarde",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        };
        directionAdapter.setDirections(addresses.getAddresses());
        if (getActivity()!=null){
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
        }
        recyclerView.setAdapter(directionAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnTimePicker:
                showTimePicker();
                break;
            case R.id.btnDatePicker:
                showDatePicker();
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
    private void showDatePicker(){
        mDatePicker = new DatePickerDialog(getActivity(),R.style.MyDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String f = String.format("%d/%d/%d",year,month,dayOfMonth);
                isDateSelected = true;
                yearSelected = year;
                monthSelected = month;
                daySelected = dayOfMonth;
                btnDatePicker.setText(f);
            }
        }, yearSelected,monthSelected,daySelected);

        //configurando fecha minima y maxima
        mDatePicker.getDatePicker().setMinDate(mcurrentTime.getTimeInMillis());
        mDatePicker.show();
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

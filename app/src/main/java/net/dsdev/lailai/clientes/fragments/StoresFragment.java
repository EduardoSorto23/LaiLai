package net.dsdev.lailai.clientes.fragments;


import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import net.dsdev.lailai.clientes.MainActivity;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.adapters.StoreAdapter;
import net.dsdev.lailai.clientes.model.ActiveOrders;
import net.dsdev.lailai.clientes.model.Store;
import net.dsdev.lailai.clientes.model.StoreList;
import net.dsdev.lailai.clientes.model.users.Client;
import net.dsdev.lailai.clientes.retrofit.RetrofitInstance;
import net.dsdev.lailai.clientes.retrofit.users.OrderService;
import net.dsdev.lailai.clientes.retrofit.users.StoreService;
import net.dsdev.lailai.clientes.util.Globals;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;
import net.dsdev.lailai.clientes.viewHolders.StoreViewHolder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoresFragment extends Fragment implements View.OnClickListener {


    private String TAG="Hola";

    public StoresFragment() {
        // Required empty public constructor
    }

    View rootView;
    MaterialButton btnTimePicker;
    int hourSelected,minuteSelected;
    private Boolean isTimeSelected = false;
    private RecyclerView rvStores;
    private Context context;
    private Fragment THIS;
    private List<Store> stores;
    private StoreAdapter storeAdapter;
    TimePickerDialog mTimePicker;
    SharedPreferencesMethods sharedPreferencesMethods;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_stores, container, false);
        bindUI();
        init();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View include;
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            include = activity.findViewById(R.id.include);
            include.setVisibility(View.GONE);
        }else{
            Log.d(TAG, "onActivityCreated: no obtiene el fab");
        }
    }

    private void bindUI() {
        btnTimePicker = rootView.findViewById(R.id.btnTimePicker);
        rvStores = rootView.findViewById(R.id.rvStores);
        context = getActivity().getApplicationContext();
    }

    private void init() {
        THIS = this;
        sharedPreferencesMethods = new SharedPreferencesMethods(getActivity());
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
                            Globals.OCASION = "LLV";
                            Navigation.findNavController(v).navigate(R.id.action_storesFragment_to_paymentMethodFragment);
                        }
                    }
                });

            }
        };
        btnTimePicker.setOnClickListener(this);
        Calendar mcurrentTime = Calendar.getInstance();

        hourSelected = mcurrentTime.get(Calendar.HOUR);
        minuteSelected = mcurrentTime.get(Calendar.MINUTE);
        try {
            if (Globals.horaEntrega!=null){
                btnTimePicker.setText(Globals.horaEntrega);
                String[] d = Globals.horaEntrega.split(":");
                hourSelected = Integer.valueOf(d[0]);
                minuteSelected = Integer.valueOf(d[1]);
                isTimeSelected = true;
            }
        }catch (Exception e){
            Log.d(TAG, "init: e "+e.getMessage());
        }
        stores = new ArrayList<>();
        storeAdapter.setDirections(stores);
        rvStores.setLayoutManager(new LinearLayoutManager(context));
        rvStores.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        rvStores.setAdapter(storeAdapter);
        rvStores.setNestedScrollingEnabled(false);
        stores();
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
        //mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
    private void stores() {
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

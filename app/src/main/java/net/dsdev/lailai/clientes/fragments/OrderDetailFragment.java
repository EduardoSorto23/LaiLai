package net.dsdev.lailai.clientes.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import net.dsdev.lailai.clientes.MainActivity;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.activity.LoginActivity;
import net.dsdev.lailai.clientes.adapters.OrderDetailAdapter;
import net.dsdev.lailai.clientes.model.menuDetail.MenuDetailList;
import net.dsdev.lailai.clientes.util.AddOrRemoveCallbacks;
import net.dsdev.lailai.clientes.util.Globals;
import net.dsdev.lailai.clientes.viewHolders.OrderDetailHolder;
import net.dsdev.lailai.clientes.util.RandomMethods;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;
import net.dsdev.lailai.clientes.view.CustomDialogSelectSweet;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailFragment extends Fragment {


    RecyclerView recyclerView;
    OrderDetailAdapter orderDetailAdapter;
    View rootView;
    Context context;
    static String TAG = "Hola";
    SharedPreferences mPrefs;
    SharedPreferences.Editor prefsEditor;
    SharedPreferencesMethods sharedMethods;
    Gson gson;
    String json;
    MenuDetailList menuDetailList;
    Button btnProcess,btnAdd,btnCancel;
    private static final String actionBarTittle = "Procesar mi orden";

    public OrderDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_order_detail, container, false);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindUI();
        mPrefs = getActivity().getSharedPreferences("globalPreferences", Context.MODE_PRIVATE);
        init();
        View include ;
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            include = activity.findViewById(R.id.include);
            include.setVisibility(View.GONE);
            sharedMethods = new SharedPreferencesMethods(activity);
            /*ADDED FOR NOT SHOW THE ALERT FOR THE SWEET*/
            sharedMethods.saveSweetSelect("isSweetSelected",true);
        }else{
            Log.d(TAG, "onActivityCreated: no obtiene el fab");
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void bindUI(){
        context = getActivity().getApplicationContext();
        recyclerView = rootView.findViewById(R.id.rvOrderDetail);
        gson = new Gson();
        btnProcess = rootView.findViewById(R.id.btnProcess);
        btnAdd = rootView.findViewById(R.id.btnAdd);
        btnCancel = rootView.findViewById(R.id.btnCancel);
    }
    private void init(){

        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (!sharedMethods.getIsLogged() || sharedMethods.getLoggedUser()==null){
                    //showInfoAlert();
                    RandomMethods.showLoginNeededAlert(getActivity());
                } else {
                    if (menuDetailList != null && menuDetailList.getMenus() != null && menuDetailList.getMenus().size()>0){
                        if (!sharedMethods.isSweetSelected()){
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            CustomDialogSelectSweet custom = new CustomDialogSelectSweet();
                            custom.show(fm,"");
                            sharedMethods.saveSweetSelect("isSweetSelected",true);
                        }else{
                            /*new MaterialAlertDialogBuilder(getActivity(), R.style.MyDialogThemeMaterial)
                                    .setTitle("Importante")
                                    .setMessage("¿Deseas tu pedido a domicilio o para pasar a recogerlo en tienda?")
                                    .setNeutralButton("DOMICILIO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Navigation.findNavController(v).navigate(R.id.action_orderDetailFragment_to_directionsFragment);
                                        }
                                    })
                                    .setNegativeButton("RECOGER", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Navigation.findNavController(v).navigate(R.id.action_orderDetailFragment_to_storesFragment);
                                        }
                                    })
                                    .setPositiveButton("CANCELAR",null)
                                    .show();*/
                            Navigation.findNavController(v).navigate(R.id.action_orderDetailFragment_to_directionsFragment);
                        }

                    }else{
                        Toast.makeText(getActivity().getApplicationContext(),"Por favor, añade algún producto",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_orderDetailFragment_to_homeFragment);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedMethods.deleteMenusTree();
                ((AddOrRemoveCallbacks) Objects.requireNonNull(getActivity())).onRemoveProduct();
                NavHostFragment.findNavController(OrderDetailFragment.this).popBackStack();
            }
        });

        json = mPrefs.getString("shoppingCart", "");
        if (!json.equalsIgnoreCase("")){
            menuDetailList = new MenuDetailList();
            menuDetailList = gson.fromJson(json, MenuDetailList.class);
        }

        orderDetailAdapter = new OrderDetailAdapter(getContext()) {
            @Override
            public void setClickListener(OrderDetailHolder holder, final int position, final int total) {
                holder.getBtnDeleteProcess().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        orderDetailAdapter.removeAt(position);
                        ((AddOrRemoveCallbacks)getActivity()).onRemoveProduct();
                        //String formatButton = " %s ( %s ) ── Q%s";
                        String formatButton = Globals.formatBtn;
                        btnProcess.setText(String.format(formatButton,getResources().getString(R.string.process_my_order), menuDetailList.getMenus() == null ? 0:menuDetailList.getMenus().size(),orderDetailAdapter.getTotal()));
                        //Toast.makeText(getActivity().getApplicationContext(),"Producto eliminado exitosamente",Toast.LENGTH_LONG).show();
                    }
                });


                holder.getTbRowOrderDetail().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick: click en row "+position);
                        Bundle bundle = new Bundle();
                        bundle.putInt("positionMenu",position);
                        bundle.putBoolean("isEditable",true);
                        Navigation.findNavController(v).navigate(R.id.action_orderDetailFragment_to_menuDetailFragment,bundle);
                    }
                });
            }
        };

        orderDetailAdapter.setmPrefs(mPrefs);
        if ( menuDetailList != null && menuDetailList.getMenus() != null && menuDetailList.getMenus().size() > 0 ){
            orderDetailAdapter.setMenuDetailList(menuDetailList);
            recyclerView.setAdapter(orderDetailAdapter);
            //String formatButton = " %s ( %s ) ── Q%s";
            String formatButton = Globals.formatBtn;
            btnProcess.setText(String.format(formatButton,getResources().getString(R.string.process_my_order),menuDetailList.getMenus().size(),orderDetailAdapter.getTotal()));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(actionBarTittle);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayUseLogoEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "click "+item.getItemId() + " home"+android.R.id.home);
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: click ");
                if ( menuDetailList != null&&menuDetailList.getMenus()!= null && menuDetailList.getMenus().size()>0){
                    NavHostFragment.findNavController(this).navigateUp();
                }else{
                    NavHostFragment.findNavController(this).navigate(R.id.action_orderDetailFragment_to_homeFragment);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.clear();
        super.onPrepareOptionsMenu(menu);
    }

    private void showInfoAlert(){
        new MaterialAlertDialogBuilder(getActivity(), R.style.MyDialogThemeMaterial)
                .setTitle("Importante")
                .setMessage("No has iniciado sesión. Para poder ordenar es necesario iniciar sesión.")
                .setPositiveButton("Iniciar sesión", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavHostFragment.findNavController(OrderDetailFragment.this).popBackStack();
                    }
                })
                .show();
    }
}

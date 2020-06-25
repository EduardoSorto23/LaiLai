package net.dsdev.lailai.clientes.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.dsdev.lailai.clientes.MainActivity;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.adapters.ActiveOrderDetailAdapter;
import net.dsdev.lailai.clientes.adapters.OrderDetailAdapter;
import net.dsdev.lailai.clientes.model.Request.Order;
import net.dsdev.lailai.clientes.model.menuDetail.MenuDetail;
import net.dsdev.lailai.clientes.model.menuDetail.MenuDetailList;
import net.dsdev.lailai.clientes.retrofit.RetrofitInstance;
import net.dsdev.lailai.clientes.retrofit.requests.OrderRequest;
import net.dsdev.lailai.clientes.retrofit.users.OrderService;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;
import net.dsdev.lailai.clientes.viewHolders.OrderDetailHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveOrderDetailFragment extends Fragment {


    public ActiveOrderDetailFragment() {
        // Required empty public constructor
    }
    private View rootView;
    RecyclerView recyclerView;
    ActiveOrderDetailAdapter detailAdapter;
    Context context;
    static String TAG = "Hola";
    SharedPreferencesMethods sharedPreferencesMethods;
    private static final String actionBarTittle = "Procesar mi Orden";
    private Order order;
    net.dsdev.lailai.clientes.model.Order activeOrder;
    private List<MenuDetail> menus;
    private Long idOrder;
    private TextView txtOrderNumber,txtOrderState,txtOrderAmmount,
            txtOrderTelephone,txtOrderDate, txtOrderIndications,txtOrderPayment;
    private Button btnBack;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_active_order__detail, container, false);
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
            sharedPreferencesMethods = new SharedPreferencesMethods(activity);
        }else{
            Log.d(TAG, "onActivityCreated: no obtiene el fab");
        }

    }
    private void init() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Objects.requireNonNull(getActivity()).onBackPressed();
                Navigation.findNavController(v).popBackStack();
            }
        });
        sharedPreferencesMethods = new SharedPreferencesMethods(getActivity());
        detailAdapter = new ActiveOrderDetailAdapter(getActivity()) {
            @Override
            public void setClickListener(OrderDetailHolder holder, int position, int total) {

            }
        };
        menus = new ArrayList<>();
        if (getArguments()!=null){
            idOrder = getArguments().getLong("idActiveOrder");
            try {
                activeOrder = (net.dsdev.lailai.clientes.model.Order) getArguments().getSerializable("activeOrder");
            }catch (Exception e){
                Log.d(TAG, "init: error al obtener la orden"+e.getMessage());
            }
        }
        if (activeOrder!=null){
            txtOrderNumber.setText(activeOrder.getOrderCode());
            txtOrderState.setText(activeOrder.getState());
        }
        if (idOrder!=null){
            detailAdapter.setMenuDetailList(menus);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(detailAdapter);
            recyclerView.setNestedScrollingEnabled(false);
            activeOrders(idOrder);
        }
    }

    private void bindUI() {
        recyclerView = rootView.findViewById(R.id.rvOrderTrackList);
        txtOrderNumber = rootView.findViewById(R.id.txtOrderNumber);
        txtOrderState = rootView.findViewById(R.id.txtOrderState);
        txtOrderAmmount = rootView.findViewById(R.id.txtOrderAmmount);
        txtOrderTelephone = rootView.findViewById(R.id.txtOrderTelephone);
        txtOrderDate = rootView.findViewById(R.id.txtOrderDate);
        txtOrderIndications = rootView.findViewById(R.id.txtOrderIndications);
        btnBack = rootView.findViewById(R.id.btnBack);
        txtOrderPayment = rootView.findViewById(R.id.txtOrderPayment);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void activeOrders(Long idOrder) {
        OrderService orderService = RetrofitInstance.getRetrofitInstance().create(OrderService.class);
        Call<Order> call = orderService.getActiveOrderDetail(idOrder);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                String msg = "Error al obtener el detalle de la orden";
                if (response.body()!=null){
                    order = response.body();
                    if (order.getMenus()!=null && order.getMenus().size()>0){
                        menus = order.getMenus();
                        detailAdapter.setMenuDetailList(menus);
                        setTxtValues();
                        msg = "Detalle obtenido exitosamente";
                    }else {
                        msg = "No hay detalle de la orden";
                    }
                }
                Log.d(TAG, "onResponse: "+msg);
                if (getActivity()!=null){
                    Toast.makeText(getActivity().getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                String msg = "Error al obtener el detalle";
                Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
                Log.d(TAG, "onFailure: "+msg+t.getMessage());
            }
        });
    }

    private void setTxtValues() {
        txtOrderNumber.setText(order.getOrderCode());
        txtOrderState.setText(order.getOrderState());
        if (order.getAmount()!=null){
            txtOrderAmmount.setText(String.valueOf(order.getAmount()));
        }
        txtOrderTelephone.setText(order.getTelephone());
        txtOrderDate.setText(order.getFechaHora());
        txtOrderIndications.setText(order.getIndications());
        if (order.getPaymentDetail()!=null && order.getPaymentDetail().size()>0){
            txtOrderPayment.setText(order.getPaymentDetail().get(0).getPaymentMethod());
        }
    }

}

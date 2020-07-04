package net.dsdev.lailai.clientes.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import net.dsdev.lailai.clientes.MainActivity;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.adapters.ActiveOrderDetailAdapter;
import net.dsdev.lailai.clientes.model.Request.Card;
import net.dsdev.lailai.clientes.model.Request.Order;
import net.dsdev.lailai.clientes.model.menuDetail.MenuDetail;
import net.dsdev.lailai.clientes.model.users.CancelOrderResponse;
import net.dsdev.lailai.clientes.retrofit.RetrofitInstance;
import net.dsdev.lailai.clientes.retrofit.users.OrderService;
import net.dsdev.lailai.clientes.util.Globals;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;
import net.dsdev.lailai.clientes.viewHolders.OrderDetailHolder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static net.dsdev.lailai.clientes.util.Globals.formatCard;
import static net.dsdev.lailai.clientes.util.Globals.formatDouble;

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
            txtOrderTelephone,txtOrderDate, txtOrderIndications,txtOrderPayment,
            txtSubTotal,txtTotal,lblTipoOcasion,txtAddress,txtPayment,txtNit,txtMoneyBack,txtIndications,txtCardNumber;
    private Button btnBack,btnCancel;
    private ImageView imgPayment;
    private View paymentLine;
    private RadioGroup rgBill;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_active_order_detail, container, false);
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
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrder(idOrder);
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
            //txtOrderState.setText(activeOrder.getState());
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

    private void cancelOrder(Long idOrder) {
        OrderService orderService = RetrofitInstance.getRetrofitInstance().create(OrderService.class);
        Call<CancelOrderResponse> call = orderService.orderCancel(idOrder);
        call.enqueue(new Callback<CancelOrderResponse>() {
            @Override
            public void onResponse(Call<CancelOrderResponse> call, Response<CancelOrderResponse> response) {
                CancelOrderResponse res = response.body();
                if (res!=null){
                    Log.d(TAG, "onResponse: result "+res.isValid());
                    if (res.isValid()) {
                        Navigation.findNavController(rootView).popBackStack();
                    }else if (!res.isValid()){
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(), res.getMsg(), Toast.LENGTH_LONG).show();
                        }
                    }
                }else{
                    Log.d(TAG, "onResponse: body is null");
                    Toast.makeText(getActivity(),"No se ha podido cancelar su orden", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CancelOrderResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: error al cancelar "+t.getMessage());
                Toast.makeText(getActivity(),"No se ha podido cancelar su orden", Toast.LENGTH_LONG).show();
            }
        });
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

        //
        txtSubTotal = rootView.findViewById(R.id.txtSubTotal);
        txtTotal = rootView.findViewById(R.id.txtTotal);
        lblTipoOcasion = rootView.findViewById(R.id.lblTipoOcasion);
        txtAddress = rootView.findViewById(R.id.txtAddress);
        txtPayment = rootView.findViewById(R.id.txtPayment);
        imgPayment = rootView.findViewById(R.id.imgPayment);
        paymentLine = rootView.findViewById(R.id.paymentLine);
        rgBill = rootView.findViewById(R.id.rgBill);
        txtNit = rootView.findViewById(R.id.txtNit);
        txtMoneyBack = rootView.findViewById(R.id.txtMoneyBack);
        txtIndications = rootView.findViewById(R.id.txtIndications);
        btnCancel = rootView.findViewById(R.id.btnCancel);
        txtCardNumber = rootView.findViewById(R.id.txtCardNumber);
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
                boolean show = true;
                if (response.body()!=null){
                    order = response.body();
                    if (order.getMenus()!=null && order.getMenus().size()>0){
                        menus = order.getMenus();
                        detailAdapter.setMenuDetailList(menus);
                        try {
                            setTxtValues();
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                        show = false;
                        msg = "Detalle obtenido exitosamente";
                    }else {
                        msg = "No hay detalle de la orden";
                    }

                    setData();

                }
                Log.d(TAG, "onResponse: "+msg);
                if (getActivity()!=null && show){
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

    private void setData() {
        if (order.getOrderState()!=null && order.getOrderState().equalsIgnoreCase("PEN")){
            btnCancel.setClickable(true);
            btnCancel.setEnabled(true);
        }
        txtSubTotal.setText(String.format(formatDouble,order.getAmount()));
        txtTotal.setText(String.format(formatDouble,order.getAmount()));
        if (order.getOcasion()!=null && order.getOcasion().equalsIgnoreCase("DOM")){
            lblTipoOcasion.setText("Dirección de envío");
            if (order.getDireccion()!=null){
                txtAddress.setText(order.getDireccion().getDirection());
            }
        }else {
            lblTipoOcasion.setText("Tienda");
            if (order.getTienda() != null) {
                txtAddress.setText(order.getTienda().getName());
            }
        }
        if (order.getPaymentDetail()!=null && order.getPaymentDetail()!= null && order.getPaymentDetail().size()>0){
            Card card = order.getPaymentDetail().get(0);
            String method = card.getPaymentMethod();
            if (method.equals("EFE")){
                txtPayment.setText("EFECTIVO");
                imgPayment.setVisibility(View.INVISIBLE);
                paymentLine.setVisibility(View.INVISIBLE);
            } else if (method.equals("CRD")){
                txtPayment.setText("CRÉDITO");
                imgPayment.setVisibility(View.VISIBLE);
                paymentLine.setVisibility(View.VISIBLE);
                if (card.getDocument().length()>=4){
                    String nC = card.getDocument().substring(card.getDocument().length()-4);
                    txtCardNumber.setVisibility(View.VISIBLE);
                    txtCardNumber.setText(String.format(formatCard,nC));
                }
            }
        }
        txtOrderTelephone.setText(order.getTelephone());
        if (order.getFactura() !=null && order.getFactura()){
            rgBill.check(R.id.rbYes);
            txtNit.setText(order.getFacturaNit());
        }else{
            rgBill.check(R.id.rbNo);
        }
        if (order.getVuelto()!=null){
            txtMoneyBack.setText(String.format(formatDouble,order.getVuelto()));
        }
        txtIndications.setText(order.getIndications());

        if (order.getPaymentDetail()!= null && order.getPaymentDetail().size()>0){

        }

    }

    private void setTxtValues() {
        txtOrderNumber.setText(order.getOrderCode());
        txtOrderState.setText(order.getEstadoDesc());
        if (order.getAmount()!=null){
            txtOrderAmmount.setText(String.format(formatDouble,order.getAmount()));
        }
        txtOrderTelephone.setText(order.getTelephone());
        txtOrderDate.setText(order.getFechaHora());
        txtOrderIndications.setText(order.getIndications());
        if (order.getPaymentDetail()!=null && order.getPaymentDetail().size()>0){
            txtOrderPayment.setText(order.getPaymentDetail().get(0).getPaymentMethod());
        }
    }

}

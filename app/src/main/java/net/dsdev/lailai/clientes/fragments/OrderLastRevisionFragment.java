package net.dsdev.lailai.clientes.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.dsdev.lailai.clientes.model.cards.CardAuth;
import net.dsdev.lailai.clientes.retrofit.requests.OrderRequest;
import net.dsdev.lailai.clientes.MainActivity;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.adapters.OrderDetailAdapter;
import net.dsdev.lailai.clientes.model.Request.Card;
import net.dsdev.lailai.clientes.model.Request.Order;
import net.dsdev.lailai.clientes.model.menuDetail.JsonMenuDetail;
import net.dsdev.lailai.clientes.model.menuDetail.MenuDetail;
import net.dsdev.lailai.clientes.model.menuDetail.MenuDetailList;
import net.dsdev.lailai.clientes.util.Globals;
import net.dsdev.lailai.clientes.viewHolders.OrderDetailHolder;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;
import net.dsdev.lailai.clientes.view.CustomDialogConfirmation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.zxing.common.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static net.dsdev.lailai.clientes.util.Globals.formatCard;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderLastRevisionFragment extends Fragment {


    public OrderLastRevisionFragment() {
        // Required empty public constructor
    }

    private Button btnProcess;
    private TextInputEditText etIndications;
    private View rootView;
    OrderRequest orderRequest;
    RecyclerView recyclerView;
    OrderDetailAdapter orderDetailAdapter;
    Context context;
    static String TAG = "Hola";
    SharedPreferences mPrefs;
    SharedPreferences.Editor prefsEditor;
    Gson gson;
    String json;
    MenuDetailList menuDetailList;
    SharedPreferencesMethods sharedPreferencesMethods;
    Order finalOrder;
    TextView txtPayment, txtLastOrderNumber;
    View paymentLine;
    ImageView imgPayment,imgArrowAddress, imgArrowPayment;
    TextView txtAddress,txtTotal,txtSubTotal,lblTipoOcasion,txtCardNumber;
    private static final String actionBarTittle = "Procesar mi Orden";
    private FragmentActivity myContext;
    private EditText txtMoneyBack,txtNit;
    RadioGroup rgBill;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_order_last_revision, container, false);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindUI();
        mPrefs = getActivity().getSharedPreferences("globalPreferences", Context.MODE_PRIVATE);
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            sharedPreferencesMethods = new SharedPreferencesMethods(activity);
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(actionBarTittle);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeAsUpIndicator(android.R.drawable.ic_delete);
            }
        }else{
            Log.d(TAG, "onActivityCreated: no obtiene el fab");
        }
        init();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void bindUI(){
        btnProcess = rootView.findViewById(R.id.btnProcessLast);
        //context = getActivity().getApplicationContext();
        recyclerView = rootView.findViewById(R.id.rvOrderDetailLast);
        etIndications = rootView.findViewById(R.id.etIndications);
        txtPayment = rootView.findViewById(R.id.txtPayment);
        paymentLine = rootView.findViewById(R.id.paymentLine);
        imgPayment = rootView.findViewById(R.id.imgPayment);
        imgArrowAddress = rootView.findViewById(R.id.imgArrowAddress);
        txtAddress = rootView.findViewById(R.id.txtAddress);
        lblTipoOcasion = rootView.findViewById(R.id.lblTipoOcasion);
        imgArrowPayment = rootView.findViewById(R.id.imgArrowPayment);
        txtTotal = rootView.findViewById(R.id.txtTotal);
        txtSubTotal = rootView.findViewById(R.id.txtSubTotal);
        txtLastOrderNumber = rootView.findViewById(R.id.txtLastOrderNumber);
        txtMoneyBack = rootView.findViewById(R.id.txtMoneyBack);
        txtNit = rootView.findViewById(R.id.txtNit);
        rgBill = rootView.findViewById(R.id.rgBill);
        String method = getArguments().getString("paymentMethod", "EFE");
        if (method.equals("EFE")){
            txtPayment.setText("EFECTIVO");
            imgPayment.setVisibility(View.INVISIBLE);
            paymentLine.setVisibility(View.INVISIBLE);
        } else if (method.equals("CRD")){
            txtPayment.setText("CRÉDITO");
        }
        txtCardNumber = rootView.findViewById(R.id.txtCardNumber);

    }

    private void init(){
        txtNit.setFocusable(false);
        txtNit.setEnabled(false);
        //rgBill.check(R.id.rbNo);
        rgBill.check(R.id.rbYes);
        //Para habilitar el txtNit desde un inicio
        txtNit.setFocusable(true);
        txtNit.setEnabled(true);
        txtNit.setFocusableInTouchMode(true);
        txtNit.setText("C.F.");
        rgBill.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbYes){
                    finalOrder.setFactura(true);
                    finalOrder.setFacturaNit(txtNit.getText().toString());
                    txtNit.setFocusable(true);
                    txtNit.setEnabled(true);
                    txtNit.setFocusableInTouchMode(true);
                }else{
                    txtNit.setFocusable(false);
                    txtNit.setEnabled(false);
                }
            }
        });
        if(Globals.OCASION==null || Globals.OCASION.equalsIgnoreCase("DOM")){
            lblTipoOcasion.setText("Dirección de envío");
            txtAddress.setText(sharedPreferencesMethods.getLoggedUser().getFinalAddress());
        }else if (Globals.OCASION.equalsIgnoreCase("LLV")){
            txtAddress.setText(Globals.tiendaName);
            lblTipoOcasion.setText("Tienda");
        }
        finalOrder = new Order();
        finalOrder.setAmount(sharedPreferencesMethods.getFinalPrice());
        finalOrder.setCanal("APP");
        finalOrder.setIdClient(sharedPreferencesMethods.getLoggedUser().getIdCliente());
        finalOrder.setIdDirection(sharedPreferencesMethods.getLoggedUser().getIdAddress());
        finalOrder.setIndications(etIndications.getText().toString());

        finalOrder.setHoraEntrega(Globals.horaEntrega);
        finalOrder.setIdStore(Globals.tienda);
        finalOrder.setOcasion(Globals.OCASION);
        finalOrder.setFechaHora(Globals.fechaEntrega);
        Card card = new Card();
        card.setPaymentMethod(getArguments().getString("paymentMethod", "EFE"));
        if (getArguments() != null && getArguments().getString("cardAuth") != null) {
            CardAuth cardAuth = new Gson().fromJson(getArguments().getString("cardAuth"), CardAuth.class);
            card.setOwnerName(cardAuth.getOwnerName());
            card.setDocument(cardAuth.getDocumentId());
            card.setMonth(cardAuth.getMonth());
            card.setYear(cardAuth.getYear());
            card.setCcv(cardAuth.getCcv());
            if (cardAuth.getDocumentId().length()>=4){
                String nC = cardAuth.getDocumentId().substring(cardAuth.getDocumentId().length()-4);
                txtCardNumber.setVisibility(View.VISIBLE);
                txtCardNumber.setText(String.format(formatCard,nC));
            }
        }
        List<Card> cards = new ArrayList<>();
        cards.add(card);
        finalOrder.setPaymentDetail(cards);
        gson = new Gson();
        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtLastOrderNumber.getText().length()<=0){
                    Toast.makeText(getContext(), "El número de teléfono es un campo obligatorio", Toast.LENGTH_LONG).show();
                    return;
                }else if (rgBill.getCheckedRadioButtonId() == R.id.rbYes &&
                        (txtNit.getText().toString().isEmpty() || txtNit.getText().toString().length()<=0)
                ){
                    Toast.makeText(getContext(), "Debe ingresar un número de NIT", Toast.LENGTH_LONG).show();
                    return;
                }
                Boolean valNit = validarNit(txtNit.getText().toString());
                if(!valNit){
                    Toast.makeText(getContext(), "Debe ingresar \"CF\" o un número de NIT valido", Toast.LENGTH_LONG).show();
                    return;
                }
                String nit = txtNit.getText().toString();
                if((nit.equals("CF") || nit.equals("cf") || nit.equals("C/F") || nit.equals("c/f") || nit.equals("Cf") || nit.equals("C/f") || nit.equals("c.f.")) && valNit){
                    txtNit.setText("C.F.");
                }
                try{
                    finalOrder.setVuelto(Double.parseDouble(txtMoneyBack.getText().toString()));
                }catch (Exception e){
                    e.printStackTrace();
                }
                finalOrder.setTelephone(txtLastOrderNumber.getText().toString());
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Boolean isAddress = true;
                if (Globals.OCASION.equalsIgnoreCase("LLV")){
                    isAddress=false;
                }
                CustomDialogConfirmation custom = new CustomDialogConfirmation(finalOrder,OrderLastRevisionFragment.this,sharedPreferencesMethods,isAddress);
                custom.show(fm,"");
                Log.d(TAG, "onClick: go to send response");
                Log.d(TAG, "onClick: to json "+gson.toJson(finalOrder));
                /*orderRequest = RetrofitInstance.getRetrofitInstance().create(OrderRequest.class);
                Call<Order> call = orderRequest.sendFinalOrder(finalOrder);
                call.enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        Log.d(TAG, "onResponse: response code last revision "+response.code());
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        Log.d(TAG, "onFailure: response code last revision "+t.getMessage());
                    }
                });*/
            }
        });

        json = mPrefs.getString("shoppingCart", "");
        if (!json.equalsIgnoreCase("")){
            menuDetailList = new MenuDetailList();
            menuDetailList = gson.fromJson(json, MenuDetailList.class);
            List<MenuDetail> menuDetails = new ArrayList<>();
            for (JsonMenuDetail me : menuDetailList.getMenus()) {
                //me.getMenu().setQuantity(1);
                menuDetails.add(me.getMenu());
            }
            finalOrder.setMenus(menuDetails);
        }

        orderDetailAdapter = new OrderDetailAdapter(getContext()) {
            @Override
            public void setClickListener(OrderDetailHolder holder, final int position, final int total) {

            }
        };
        orderDetailAdapter.setLastRevision(true);
        orderDetailAdapter.setmPrefs(mPrefs);
        if ( menuDetailList != null && menuDetailList.getMenus() != null && menuDetailList.getMenus().size() > 0 ){
            orderDetailAdapter.setMenuDetailList(menuDetailList);
            recyclerView.setAdapter(orderDetailAdapter);
            //String formatButton = " %s ( %s ) ── Q%s";
            String formatButton = Globals.formatBtn;
            btnProcess.setText(String.format(formatButton,getResources().getString(R.string.process_my_order),menuDetailList.getMenus().size(),orderDetailAdapter.getTotal()));
            txtTotal.setText(String.format("Q%01.02f",orderDetailAdapter.getTotal()));
            txtSubTotal.setText(String.format("Q%01.02f",orderDetailAdapter.getTotal()));
            txtLastOrderNumber.setText(sharedPreferencesMethods.getLoggedUser().getTelephone());
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(actionBarTittle);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
        }

        imgArrowPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_orderLastRevisionFragment_to_paymentMethodFragment);
            }
        });
        imgArrowAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("action","order");
                bundle.putString("paymentMethod",getArguments().getString("paymentMethod", "EFE"));
                bundle.putString("cardAuth",getArguments().getString("cardAuth",""));
                Navigation.findNavController(v).navigate(R.id.action_orderLastRevisionFragment_to_directionsFragment,bundle);
            }
        });
    }

    public boolean validarNit(String nit) {
        if (nit.equals("CF") || nit.equals("cf") || nit.equals("C/F") || nit.equals("c/f") || nit.equals("Cf") || nit.equals("C/f") || nit.equals("C.F.") || nit.equals("c.f.")){
            return true;
        }
        if (nit.isEmpty()){
            return false;
        }
        if (!Pattern.matches("^[0-9]+(-?[0-9kK])?$", nit)){
            return false;
        }
        nit = nit.replace("-", "");
        int laschart = nit.length() - 1;
        String number = nit.substring(0, laschart);
        String expectedChecker = nit.substring(laschart, laschart + 1).toLowerCase();
        int factor = number.length() + 1;
        int total = 0;
        for (int i = 0; i < number.length(); i++){
            String character = number.substring(i, i + 1);
            int digit = Integer.parseInt(character, 10);
            total += (digit * factor);
            factor = factor - 1;
        }
        int module = (11 - (total % 11)) % 11;
        String computedChecker = (module == 10 ? "k" : String.valueOf(module));
        if (expectedChecker.equals(computedChecker)){
            return true;
        }else{
            return false;
        }
    }

}

package net.dsdev.lailai.clientes.fragments.user;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.dsdev.lailai.clientes.MainActivity;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.adapters.ActiveOrdersAdapter;
import net.dsdev.lailai.clientes.model.ActiveOrders;
import net.dsdev.lailai.clientes.model.Order;
import net.dsdev.lailai.clientes.retrofit.RetrofitInstance;
import net.dsdev.lailai.clientes.retrofit.users.OrderService;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;
import net.dsdev.lailai.clientes.viewHolders.ActiveOrdersViewHolder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveOrdersFragment extends Fragment {


    public ActiveOrdersFragment() {
        // Required empty public constructor
    }

    View rootView;
    private ActiveOrdersAdapter activeOrdersAdapter;
    RecyclerView rvTrackOrder;
    Context context;
    Fragment THIS;
    private SharedPreferencesMethods sharedPreferencesMethods;
    private List<Order> activeOrders;
    private String TAG = "Hola";
    private static String actionBarTittle = "Consulta tu orden";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_active_orders, container, false);
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
        rvTrackOrder = rootView.findViewById(R.id.rvTrackOrder);
        context = getActivity().getApplicationContext();
    }

    private void init() {
        THIS = this;
        /*ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(actionBarTittle);
            actionBar.setDisplayUseLogoEnabled(true);
        }*/
        sharedPreferencesMethods = new SharedPreferencesMethods(getActivity());
        activeOrdersAdapter = new ActiveOrdersAdapter(getContext()) {
            @Override
            public void setClickListener(ActiveOrdersViewHolder holder, final int position, final Order order) {
                holder.getTbLOrderTrack().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(context,"Select "+position,Toast.LENGTH_LONG).show();
                        Bundle bundle = new Bundle();
                        bundle.putLong("idActiveOrder",order.getId());
                        bundle.putSerializable("activeOrder",order);
                        NavHostFragment.findNavController(THIS).navigate(R.id.action_activeOrdersFragment_to_activeOrderDetailFragment,bundle);
                    }
                });
            }
        };
        //activeOrdersAdapter.setCount(5);
        //List<Order> orderActives = new ArrayList<>();
        //orderActives.add(new Order(1L,"2","12-12-12","En Camino"));
        activeOrders = new ArrayList<>();
        activeOrdersAdapter.setOrderActives(activeOrders);
        rvTrackOrder.setLayoutManager(
                new LinearLayoutManager(context));
        rvTrackOrder.addItemDecoration(
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        rvTrackOrder.setAdapter(activeOrdersAdapter);
        activeOrders();
    }

    private void activeOrders() {
        OrderService orderService = RetrofitInstance.getRetrofitInstance().create(OrderService.class);
        Call<ActiveOrders> call = orderService.getActiveOrders(sharedPreferencesMethods.getLoggedUser().getIdCliente());
        call.enqueue(new Callback<ActiveOrders>() {
            @Override
            public void onResponse(Call<ActiveOrders> call, Response<ActiveOrders> response) {
                String msg = "Error al obtener las ordenes activas";
                boolean show = true;
                if (response.body()!=null){
                    ActiveOrders res = response.body();
                    if (res.getOrders()!=null && res.getOrders().size()>0){
                        activeOrders = res.getOrders();
                        activeOrdersAdapter.setOrderActives(activeOrders);
                        show = false;
                        //msg = "Ordenes obtenidas exitosamente";
                    }else{
                        msg = "No hay ordenes activas";
                    }
                }
                Log.d(TAG, "onResponse: "+msg);
                if (getActivity()!=null && show){
                    Toast.makeText(getActivity().getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ActiveOrders> call, Throwable t) {
                String msg = "Error al obtener las ordenes";
                Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
                Log.d(TAG, "onFailure: "+msg+t.getMessage());
            }
        });
    }


}

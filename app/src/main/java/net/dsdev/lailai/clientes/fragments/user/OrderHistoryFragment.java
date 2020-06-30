package net.dsdev.lailai.clientes.fragments.user;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.adapters.ActiveOrdersAdapter;
import net.dsdev.lailai.clientes.model.Order;
import net.dsdev.lailai.clientes.viewHolders.ActiveOrdersViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderHistoryFragment extends Fragment {


    public OrderHistoryFragment() {
        // Required empty public constructor
    }

    View rootView;
    ActiveOrdersAdapter activeOrdersAdapter;
    RecyclerView rvOrderHistory;
    Context context;
    Fragment THIS;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_order_history, container, false);
        bindUI();
        init();
        return rootView;
    }

    private void bindUI() {
        rvOrderHistory = rootView.findViewById(R.id.rvOrderHistory);
        context = getActivity().getApplicationContext();
    }

    private void init() {
        THIS = this;
        activeOrdersAdapter = new ActiveOrdersAdapter(getContext()) {
            @Override
            public void setClickListener(ActiveOrdersViewHolder holder, final int position,Order order) {
                holder.getTbLOrderTrack().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(context,"Select "+position,Toast.LENGTH_LONG).show();
                        NavHostFragment.findNavController(THIS).navigate(R.id.action_orderHistoryFragment_to_activeOrderDetailFragment);
                    }
                });
            }
        };
        //activeOrdersAdapter.setCount(5);
        List<Order> orderActives = new ArrayList<>();
        orderActives.add(new Order(1L,"2","12-12-12","Active"));
        activeOrdersAdapter.setOrderActives(orderActives);
        rvOrderHistory.setLayoutManager(
                new LinearLayoutManager(context));
        rvOrderHistory.addItemDecoration(
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        rvOrderHistory.setAdapter(activeOrdersAdapter);

    }
}

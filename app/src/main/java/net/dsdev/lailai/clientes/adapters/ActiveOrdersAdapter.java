package net.dsdev.lailai.clientes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.model.Order;
import net.dsdev.lailai.clientes.viewHolders.ActiveOrdersViewHolder;

import java.util.List;

public abstract class ActiveOrdersAdapter extends RecyclerView.Adapter<ActiveOrdersViewHolder> {

    private Context context;
    private int count;
    private List<Order> activeOrders;

    public void setCount(int count) {
        this.count = count;
        notifyDataSetChanged();
    }

    public void setOrderActives(List<Order> activeOrders) {
        this.activeOrders = activeOrders;
        notifyDataSetChanged();
    }

    public ActiveOrdersAdapter(Context context) {
        this.context = context;
    }

    public abstract void setClickListener(ActiveOrdersViewHolder holder, int position,Order order);


    @NonNull
    @Override
    public ActiveOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_active, parent, false);
        return new ActiveOrdersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveOrdersViewHolder holder, int position) {
        holder.getTxtOrderNumber().setText(activeOrders.get(position).getOrderCode());
        holder.getTxtOrderTime().setText(activeOrders.get(position).getHour());
        holder.getTxtOrderTotal().setText(String.valueOf(activeOrders.get(position).getOrderTotal()));
        holder.getTxtOrderState().setText(activeOrders.get(position).getState());
        setClickListener(holder,position,activeOrders.get(position));
    }

    @Override
    public int getItemCount() {
        return activeOrders.size();
    }

}

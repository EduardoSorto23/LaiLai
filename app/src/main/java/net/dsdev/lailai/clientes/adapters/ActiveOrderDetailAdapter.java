package net.dsdev.lailai.clientes.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.model.Request.Order;
import net.dsdev.lailai.clientes.model.menuDetail.MenuDetail;
import net.dsdev.lailai.clientes.model.menuDetail.MenuDetailList;
import net.dsdev.lailai.clientes.viewHolders.OrderDetailHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class ActiveOrderDetailAdapter extends RecyclerView.Adapter<OrderDetailHolder> {

    private Context context;
    List<MenuDetail> menus;

    public ActiveOrderDetailAdapter(Context context) {
        this.context = context;
    }

    public void setMenuDetailList(List<MenuDetail> menus){
        this.menus = menus;
        notifyDataSetChanged();
    }

    public abstract void setClickListener(OrderDetailHolder holder,int position, int total);


    @NonNull
    @Override
    public OrderDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new OrderDetailHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailHolder holder, int position) {
        MenuDetail menu = menus.get(position);
        holder.getTxtOrderProduct().setText(menu.getName());
        holder.getTxtOrderDesc().setText(menu.getDesc());
        if (menu.getPrice()!=null){
            holder.getTxtOrderPrice().setText(String.valueOf(menu.getPrice()));
        }
        if (menu.getFinalPrice()!=null){
            holder.getTxtOrderTotal().setText(String.valueOf(menu.getFinalPrice()));
        }
        holder.getBtnDeleteProcess().setVisibility(View.GONE);
        setClickListener(holder,position,getItemCount());
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }
}

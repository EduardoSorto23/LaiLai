package net.dsdev.lailai.clientes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.model.Store;
import net.dsdev.lailai.clientes.model.users.Address;
import net.dsdev.lailai.clientes.viewHolders.DirectionViewHolder;
import net.dsdev.lailai.clientes.viewHolders.StoreViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class StoreAdapter extends RecyclerView.Adapter<StoreViewHolder> {

    private static final String TAG = "Hola";
    private Context context;
    private List<Store> stores;

    public StoreAdapter(Context context) {
        this.context = context;
    }

    public void setDirections(List<Store> stores) {
        this.stores = stores;
        notifyDataSetChanged();
    }

    public abstract void setClickListener(StoreViewHolder holder, Store store);

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store, parent, false);
        return new StoreViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        Store s = stores.get(position);
        holder.getTxtStoreName().setText(s.getName());
        holder.getTxtStoreAddress().setText(s.getAddress());
        holder.getTxtOpenHour().setText(s.getOpenHour());
        holder.getTxtCloseHour().setText(s.getCloseHour());
        setClickListener(holder,s);
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }
}

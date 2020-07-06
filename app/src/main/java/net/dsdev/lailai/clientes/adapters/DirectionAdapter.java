package net.dsdev.lailai.clientes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.model.users.Address;
import net.dsdev.lailai.clientes.viewHolders.DirectionViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class DirectionAdapter extends RecyclerView.Adapter<DirectionViewHolder> {

    private static final String TAG = "Hola";
    private Context context;
    private List<Address> addresses;

    public DirectionAdapter(Context context) {
        this.context = context;
        addresses = new ArrayList<>();
    }

    public void setDirections(List<Address> addresses) {
        this.addresses = addresses;
        notifyDataSetChanged();
    }

    public abstract void setClickListener(DirectionViewHolder holder, Address address);

    @NonNull
    @Override
    public DirectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_direction, parent, false);
        return new DirectionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DirectionViewHolder holder, int position) {
        Address currentAddresses= addresses.get(position);
        holder.getTxtDirection().setText(currentAddresses.getReferences());
        holder.getTxtDirectionName().setText(currentAddresses.getNombre());
        if (holder.getTxtDirectionName().getText().toString().equalsIgnoreCase("Casa")){
            holder.getImgIcon().setImageResource(R.drawable.ic_home);
        }else if (holder.getTxtDirectionName().getText().toString().equalsIgnoreCase("Trabajo")){
            holder.getImgIcon().setImageResource(R.drawable.ic_work);
        }else{
            holder.getImgIcon().setImageResource(R.drawable.ic_address_other);
        }
        setClickListener(holder,currentAddresses);
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }
}

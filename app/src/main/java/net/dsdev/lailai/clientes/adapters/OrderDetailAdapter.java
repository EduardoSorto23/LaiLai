package net.dsdev.lailai.clientes.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.model.menuDetail.MenuDetailList;
import net.dsdev.lailai.clientes.viewHolders.OrderDetailHolder;
import com.google.gson.Gson;

public abstract class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailHolder> {

    private Context context;
    MenuDetailList menuDetailList;
    SharedPreferences mPrefs;
    SharedPreferences.Editor editor;
    boolean isLastRevision = false;

    public OrderDetailAdapter(Context context) {
        this.context = context;
        menuDetailList = new MenuDetailList();
    }

    public void setLastRevision(boolean lastRevision) {
        isLastRevision = lastRevision;
    }

    public void setmPrefs(SharedPreferences mPrefs) {
        this.mPrefs = mPrefs;
    }

    public void setMenuDetailList(MenuDetailList menuDetailList){
        this.menuDetailList = menuDetailList;
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
        if (menuDetailList.getMenus() != null && menuDetailList.getMenus().size() > 0 ){
            holder.getTxtOrderProduct().setText(menuDetailList.getMenus().get(position).getMenu().getName());
            holder.getTxtOrderDesc().setText(menuDetailList.getMenus().get(position).getMenu().getDesc());
            String formatText = "%01.02f";
            holder.getTxtOrderPrice().setText(String.format(formatText,menuDetailList.getMenus().get(position).getMenu().getPrice()));
            holder.getTxtOrderTotal().setText(String.format(formatText,menuDetailList.getMenus().get(position).getMenu().getFinalPrice()));
            holder.getTxtOrderQuantity().setText(String.valueOf(menuDetailList.getMenus().get(position).getMenu().getQuantity()));
        }else {
            //all null
        }
        if (isLastRevision){
            holder.getBtnDeleteProcess().setVisibility(View.GONE);
        }
        //Log.d("Hola", "onBindViewHolder: hola.get(position) "+hola.get(position));
        setClickListener(holder,position,getItemCount());
    }

    @Override
    public int getItemCount() {
        return menuDetailList.getMenus().size();
    }

    public void removeAt(int position) {
        menuDetailList.getMenus().remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, menuDetailList.getMenus().size());
        editor = mPrefs.edit();
        editor.remove("shoppingCart");
        editor.putString("shoppingCart",new Gson().toJson(menuDetailList));
        editor.apply();
    }

    public Double getTotal(){
        Double total = 0.0;
        try{
            for (int i = 0; i < getItemCount(); i++) {
                total += menuDetailList.getMenus().get(i).getMenu().getFinalPrice();
            }
        } catch (Exception e){
            Log.d("Hola", "getTotal: error "+e.toString());
            total = 0.0;
        }
        return total;
    }
}

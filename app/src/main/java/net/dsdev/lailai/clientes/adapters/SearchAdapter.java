package net.dsdev.lailai.clientes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.model.Menus;
import net.dsdev.lailai.clientes.util.Constants;
import net.dsdev.lailai.clientes.viewHolders.search.HeaderHolder;
import net.dsdev.lailai.clientes.viewHolders.search.SearchHolder;
import net.dsdev.lailai.clientes.util.filter.CustomMenuFilter;

import java.util.ArrayList;
import java.util.List;

public abstract class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private Context context;
    private List<Menus> menus;
    private CustomMenuFilter filter;

    public SearchAdapter(Context context) {
        this.context = context;
        menus = new ArrayList<>();

    }

    public void setMenus(List<Menus> menus) {
        this.menus = menus;
        notifyDataSetChanged();
    }

    public abstract void setClickListener(SearchHolder holder, Menus menu);

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case Constants.HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_search, parent, false);
                return new HeaderHolder(view);
            case Constants.ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
                return new SearchHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Menus currentMenu = menus.get(position);
        switch (currentMenu.getItemType()){
            case Constants.HEADER:
                ((HeaderHolder) holder).getTxtHeaderMenu().setText(currentMenu.getName());
                break;
            case Constants.ITEM:
                SearchHolder searchHolder = (SearchHolder) holder;
                searchHolder.getTxtName().setText(currentMenu.getName());
                searchHolder.getTxtDesc().setText(currentMenu.getDescription());
                searchHolder.getTxtPrice().setText("Q"+currentMenu.getPrice());
                setClickListener(searchHolder, currentMenu);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (menus.get(position).getItemType()){
            case 0:
                return Constants.HEADER;
            case 1:
                return Constants.ITEM;
            default:
                return -1;
        }
    }

    @Override
    public CustomMenuFilter getFilter() {
        if (filter==null){
            filter = new CustomMenuFilter(this, menus);
        }
        return filter;
    }

}

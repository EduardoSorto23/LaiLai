package net.dsdev.lailai.clientes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import net.dsdev.lailai.clientes.model.Categories;
import net.dsdev.lailai.clientes.model.JsonMenus;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.viewHolders.HomeViewHolder;

public abstract class CategoryAdapter extends RecyclerView.Adapter<HomeViewHolder> {

    private Context context;
    //private List<Categories> categories;
    private JsonMenus jsonMenus;

    public CategoryAdapter(Context context) {
        this.context = context;
        //categories = new ArrayList<>();

    }

    /*public List<Categories> find(){
        return categories;
    }*/

    public JsonMenus find(){
        return jsonMenus;
    }

    public void setMenu(JsonMenus jsonMenus){
        this.jsonMenus = jsonMenus;
        notifyDataSetChanged();
    }

    public abstract void setClickListener(HomeViewHolder holder,Categories category, int position);

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        return new HomeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        //Categories currentCategories = categories.get(position);
        Categories currentCategories = jsonMenus.getCategories().get(position);
        Glide.with(context)
                .load(currentCategories.getImages().getNormal() == null ? currentCategories.getImages().getUnavailable():currentCategories.getImages().getNormal())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.getImgHomeItem());
        holder.getTxtHomeTittle().setText(currentCategories.getName());
        setClickListener(holder,currentCategories, position);
    }
    @Override
    public int getItemCount() {
        return jsonMenus.getCategories().size();
    }

}


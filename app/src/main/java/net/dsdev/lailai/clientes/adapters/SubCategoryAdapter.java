package net.dsdev.lailai.clientes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.model.Categories;
import net.dsdev.lailai.clientes.model.SubCategories;
import net.dsdev.lailai.clientes.viewHolders.CategoryViewHolder;

abstract public class SubCategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private Context context;
    private Categories category;

    public SubCategoryAdapter(Context context) {
        this.context = context;
    }

    public void setCategory(Categories category) {
        this.category = category;
        notifyDataSetChanged();
    }

    public abstract void setClickListener(CategoryViewHolder holder,SubCategories subCategory,int position);

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        SubCategories subCategory = category.getSubCategories().get(position);
        holder.getTxtName().setText(subCategory.getName());
        holder.getTxtShowMenu().setText("Ver Menú");
        Glide.with(context)
                .load(subCategory.getImages().getSmall() == null ? subCategory.getImages().getUnavailable():subCategory.getImages().getNormal())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //.apply(RequestOptions.circleCropTransform())
                .into(holder.getImgCategory());
        setClickListener(holder,subCategory,position);
    }

    @Override
    public int getItemCount() {
        return category.getSubCategories().size();
    }

}

package net.dsdev.lailai.clientes.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.model.Menus;
import net.dsdev.lailai.clientes.model.SubCategories;

import java.util.List;

public abstract class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private SubCategories subCategory;
    private Menus menus;
    /** CAMBIADO POR REQUERIMIENTO DE SOLO MOSTRAR LISTA **/
    private boolean isList = true;


    public MenuAdapter(Context context) {
        this.context = context;
    }

    public void setList(boolean list) {
        isList = list;
        notifyDataSetChanged();
    }

    public SubCategories find(){
        return subCategory;
    }

    public void setMenu(SubCategories subCategory, List<Menus> menus){
        if (subCategory == null && menus != null){
            this.subCategory = new SubCategories();
            this.subCategory.setMenus(menus);
        } else if (subCategory!=null){
            this.subCategory = subCategory;
        }
        notifyDataSetChanged();
    }

    public abstract void setClickListener(MenuViewHolder holder, int position,Menus menu);
    public abstract void setClickListener(MenuListViewHolder holder, int position, Menus menu);
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_GROUP = 2;

    @Override
    public int getItemViewType(int position) {
        // here your custom logic to choose the view type
        return isList ? TYPE_IMAGE : TYPE_GROUP;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == TYPE_IMAGE) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_list, parent, false);
            return new MenuListViewHolder(itemView);
        }else{
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
            return new MenuViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Menus currentMenu = subCategory.getMenus().get(position);
        switch (holder.getItemViewType()) {

            case TYPE_IMAGE:
                Log.d("Hola", "onBindViewHolder: en lista ");
                /*            ((MenuListViewHolder) holder).bind(currentMenu, position);*/
                MenuListViewHolder menuListViewHolder = (MenuListViewHolder) holder;
                menuListViewHolder.bind(currentMenu, position);
                break;

            case TYPE_GROUP:
                MenuViewHolder menuViewHolder = (MenuViewHolder) holder;
                menuViewHolder.bind(currentMenu, position);
                /*((MenuViewHolder) holder).bind(currentMenu, position);*/
                Log.d("Hola", "onBindViewHolder: en cuadro ");
                break;
        }

        //if (isList){


        //} else {

        //}
    }

    @Override
    public int getItemCount() {
        return subCategory.getMenus().size();
    }

    public class MenuListViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName, txtDesc, txtPrice;
        private ImageView imgMenuList;
        private LinearLayout clMenuList;

        public MenuListViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtMenuListName);
            txtDesc = itemView.findViewById(R.id.txtMenuListDesc);
            txtPrice = itemView.findViewById(R.id.txtMenuListPrice);
            imgMenuList = itemView.findViewById(R.id.imgMenuList);
            clMenuList = itemView.findViewById(R.id.clMenuList);
        }

        public void bind(Menus menu, int position){
            this.txtName.setText(menu.getName());
            this.txtDesc.setText(menu.getDescription());
            /** CAMBIADO POR REQUERIMIENTO DE MOSTRAR IMAGEN CUADRADA **/
            Glide.with(context)
                    .load(menu.getImages().getNormal() == null ? menu.getImages().getUnavailable():menu.getImages().getNormal())
                    .centerCrop()
                    //.apply(RequestOptions.circleCropTransform())
                    .transform(new RoundedCorners(16))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(this.imgMenuList);

            setClickListener(this, position, menu);
        }

        public TextView getTxtName() {
            return txtName;
        }

        public TextView getTxtDesc() {
            return txtDesc;
        }

        public TextView getTxtPrice() {
            return txtPrice;
        }

        public ImageView getImgMenuList() {
            return imgMenuList;
        }

        public LinearLayout getClMenuList() {
            return clMenuList;
        }
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder{
        public TextView txtTittle;
        public TextView txtDesc;
        public ImageView imgMenu;
        public Button btnPrecio;
        public CardView cardView;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTittle = itemView.findViewById(R.id.txtTittle);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            imgMenu = itemView.findViewById(R.id.imgMenu);
            btnPrecio = itemView.findViewById(R.id.btnPrice);
            cardView = itemView.findViewById(R.id.materialCardView);
        }

        public Button getBtnPrecio() {
            return btnPrecio;
        }

        public void bind(Menus menu, int position){
            this.txtTittle.setText(menu.getName());
            this.txtDesc.setText(menu.getDescription());
            this.btnPrecio.setText("Q"+menu.getPrice());
            Glide.with(context)
                    .load(menu.getImages().getNormal() == null ? menu.getImages().getUnavailable():menu.getImages().getNormal())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(this.imgMenu);

            setClickListener(this, position,menu);
        }
    }


}


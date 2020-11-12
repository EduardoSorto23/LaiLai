package net.dsdev.lailai.clientes.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.model.menuDetail.JsonMenuDetail;
import net.dsdev.lailai.clientes.model.menuDetail.MenuDetail;
import net.dsdev.lailai.clientes.model.menuDetail.MenuDetailList;
import net.dsdev.lailai.clientes.model.menuDetail.OptionMenuDetail;
import net.dsdev.lailai.clientes.model.menuDetail.VariantMenuDetail;
import net.dsdev.lailai.clientes.util.Constants;
import net.dsdev.lailai.clientes.viewHolders.MenuVentaSugeridaViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class MenuVentaSugeridaAdapter extends RecyclerView.Adapter<MenuVentaSugeridaViewHolder> {

    private static final String TAG = "Hola";
    private Context context;
    private List<OptionMenuDetail> options;
    private MenuDetailList menus;

    @NonNull
    @Override
    public MenuVentaSugeridaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option_venta_sugerida, parent, false);
        return new MenuVentaSugeridaViewHolder(itemView);
    }

    public abstract void setRadioGroupClickListener(MenuVentaSugeridaViewHolder holder, RadioGroup radioGroup, int idOption);

    public void onBindViewHolder(@NonNull MenuVentaSugeridaViewHolder holder, int position) {
        Resources res = context.getResources();
        JsonMenuDetail currentMenu = menus.getMenus().get(position);
        Log.d(TAG,"onBindHolder: menu "+ currentMenu.getMenu().getName());
        holder.getTxtMenuDetailItem().setText(currentMenu.getMenu().getName());
        Double vMenuFormat = formatearDecimales(currentMenu.getMenu().getPrice(),2);
        holder.getTxtMenuDetailPrice().setText(res.getString(R.string.moneySymbol) + " " + String.format("%.2f", vMenuFormat));

        RadioGroup rg = new RadioGroup(context);
        rg.setId(currentMenu.getMenu().getIdMenu());
        rg.setTag(currentMenu.getMenu().getOptions().get(0).getIdOption());
        LinearLayout ll  = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        Glide.with(context)
                .load(currentMenu.getMenu().getImages().getNormal() == null ? currentMenu.getMenu().getImages().getUnavailable():currentMenu.getMenu().getImages().getNormal())
                .centerCrop()
                //.apply(RequestOptions.circleCropTransform())
                .transform(new RoundedCorners(16))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.getImgMenuListM());

        //Radio Button por defecto
        RadioButton radioButtonDefault = new RadioButton(context);
        radioButtonDefault.setId(-1);
        radioButtonDefault.setText(res.getString(R.string.noAddMenu));
        radioButtonDefault.setTag(0.0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            radioButtonDefault.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
        }

        radioButtonDefault.setTextColor(Color.parseColor("#000000"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            radioButtonDefault.setTextAppearance(R.style.CustomFontFamily);
        }else{
            radioButtonDefault.setTextAppearance(context,R.style.CustomFontFamily);
        }
        rg.addView(radioButtonDefault);
        rg.check(radioButtonDefault.getId());

        for (VariantMenuDetail v: currentMenu.getMenu().getOptions().get(0).getVariants()) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setId(v.getIdVariant());
            radioButton.setTag(currentMenu.getMenu().getPrice() + v.getExtraPrice());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                radioButton.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
            }

            radioButton.setTextColor(Color.parseColor("#000000"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                radioButton.setTextAppearance(R.style.CustomFontFamily);
            }else{
                radioButton.setTextAppearance(context,R.style.CustomFontFamily);
            }

            if (v.getExtraPrice() > 0){
                Double valorFormateado = formatearDecimales(v.getExtraPrice(),2);
                radioButton.setText(v.getVariant() + "     +" + res.getString(R.string.moneySymbol) + String.format("%.2f", valorFormateado));
            }else{
                radioButton.setText(v.getVariant());
            }


            TextView textView = new TextView(context);
            TextViewCompat.setTextAppearance(textView, R.style.CustomFontFamily);
            String formatText = "+%01.02f";
            textView.setText(String.format(formatText,v.getExtraPrice()));
            if (!(v.getExtraPrice()>0)){
                textView.setVisibility(View.INVISIBLE);
            }
            radioButton.setHeight(90);
            textView.setHeight(90);
            textView.setGravity(Gravity.CENTER);

            //ll.addView(textView);
            rg.addView(radioButton);
        }
        setRadioGroupClickListener(holder, rg, currentMenu.getMenu().getOptions().get(0).getIdOption());
        holder.getLlContainer().addView(ll);
        holder.getRgMenuDetailItem().addView(rg);
    }

    public MenuVentaSugeridaAdapter(Context context) {
        this.context = context;
        options = new ArrayList<>();
    }

    /*public void setOptions(List<OptionMenuDetail> options) {
        this.options = options;
        //notifyDataSetChanged();
    }*/

    public void setMenus(MenuDetailList menus) {
        this.menus = menus;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return menus.getMenus().size();
    }

    public static Double formatearDecimales(Double numero, Integer numeroDecimales) {
        return Math.round(numero * Math.pow(10, numeroDecimales)) / Math.pow(10, numeroDecimales);
    }
}

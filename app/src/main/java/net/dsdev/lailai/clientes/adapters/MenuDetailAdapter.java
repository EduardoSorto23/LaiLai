package net.dsdev.lailai.clientes.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
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

import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.model.menuDetail.OptionMenuDetail;
import net.dsdev.lailai.clientes.model.menuDetail.VariantMenuDetail;
import net.dsdev.lailai.clientes.viewHolders.MenuDetailViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class MenuDetailAdapter extends RecyclerView.Adapter<MenuDetailViewHolder> {

    private static final String TAG = "Hola";
    private Context context;
    private List<OptionMenuDetail> options;

    public MenuDetailAdapter(Context context) {
        this.context = context;
        options = new ArrayList<>();
    }

    public void setOptions(List<OptionMenuDetail> options) {
        this.options = options;
        notifyDataSetChanged();
    }

    public abstract void setRadioGroupClickListener(MenuDetailViewHolder holder, RadioGroup radioGroup, int idOption);

    @NonNull
    @Override
    public MenuDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option, parent, false);
        return new MenuDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuDetailViewHolder holder, int position) {
        OptionMenuDetail currentOption = options.get(position);
        Log.d(TAG, "onBindViewHolder: text "+currentOption.getQuestion());
        holder.getTxtQuestion().setText(currentOption.getQuestion());
        List<VariantMenuDetail> variants = currentOption.getVariants();

        RadioGroup rg = new RadioGroup(context);
        LinearLayout ll  = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        rg.setOrientation(LinearLayout.VERTICAL);

        /*-----------------------------------------------------
            Para ocultar el rg cuando solo posee una variante
        -------------------------------------------------------*/
        if(variants.size() < 2){
            rg.setVisibility(View.GONE);
            Log.d(TAG,"Se volvio invisible el radio group");
        }else{
            rg.setVisibility(View.VISIBLE);
            Log.d(TAG,"Se volvio visible el radio group");
        }

        for (VariantMenuDetail v: variants) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setId(v.getIdVariant());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                radioButton.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
            }

            radioButton.setTextColor(Color.parseColor("#000000"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                radioButton.setTextAppearance(R.style.CustomFontFamily);
            }else{
                radioButton.setTextAppearance(context,R.style.CustomFontFamily);
            }

            radioButton.setText(v.getVariant());
            if (v.isDefault()) {
                radioButton.setChecked(true);
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //radioButton.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#fbc02d")));
                }*/
            } else {
                radioButton.setChecked(false);
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

            ll.addView(textView);
            rg.addView(radioButton);
        }

        setRadioGroupClickListener(holder, rg, currentOption.getIdOption());

        holder.getLinearLayout().addView(ll);
        holder.getRgMenuDetailOptions().addView(rg);
    }

    @Override
    public int getItemCount() {
        return options.size();
    }
}

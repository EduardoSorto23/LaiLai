package net.dsdev.lailai.clientes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.model.Promo.PromoList;
import net.dsdev.lailai.clientes.viewHolders.SliderAdapterVH;
import com.smarteist.autoimageslider.SliderViewAdapter;

import android.graphics.Color;

public abstract class SliderHomeAdapter extends
        SliderViewAdapter<SliderAdapterVH> {

    private Context context;
    private PromoList promoList;

    public SliderHomeAdapter(Context context) {
        this.context = context;
    }

    public void setPromoList(PromoList promoList) {
        this.promoList = promoList;
        notifyDataSetChanged();
    }


    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_slider_layout, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {


        setClickListener(viewHolder,promoList.getPromos().get(position).getId());

        viewHolder.getTextViewDescription().setText(promoList.getPromos().get(position).getDesc());
        viewHolder.getTextViewDescription().setTextSize(16);
        viewHolder.getTextViewDescription().setTextColor(Color.BLACK);
        viewHolder.getImageGifContainer().setVisibility(View.GONE);
        Glide.with(viewHolder.getItemView())
            .load(
                    (
                            promoList.getPromos().get(position).getImages().getNormal() == null || promoList.getPromos().get(position).getImages().getNormal().equals("")
                    )?
                            promoList.getPromos().get(position).getImages().getUnavailable():
                            promoList.getPromos().get(position).getImages().getNormal()
            )
            .fitCenter()
            .into(viewHolder.getImageViewBackground());


    }

    public void setClickListener(SliderAdapterVH holder,long id){

    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return promoList.getPromos().size();
    }


}
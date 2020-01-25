package net.dsdev.lailai.clientes.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.dsdev.lailai.clientes.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

    private View itemView;
    private ImageView imageViewBackground;
    private ImageView imageGifContainer;
    private TextView textViewDescription;

    public SliderAdapterVH(View itemView) {
        super(itemView);
        imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
        imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
        textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
        this.itemView = itemView;
    }

    public View getItemView() {
        return itemView;
    }

    public ImageView getImageViewBackground() {
        return imageViewBackground;
    }

    public ImageView getImageGifContainer() {
        return imageGifContainer;
    }

    public TextView getTextViewDescription() {
        return textViewDescription;
    }
}

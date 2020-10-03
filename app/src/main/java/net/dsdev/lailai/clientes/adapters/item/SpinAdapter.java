package net.dsdev.lailai.clientes.adapters.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.model.users.AddressDTO;


import java.util.List;

public class SpinAdapter extends ArrayAdapter<AddressDTO> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private List<AddressDTO> values;

    public SpinAdapter(Context context, int textViewResourceId,
                       List<AddressDTO> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public AddressDTO getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent,false);
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return initView(position, convertView, parent,true);
    }

    private View initView(int position, View convertView, ViewGroup parent,boolean drop) {
        if (convertView == null) {
            if (drop){
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.spinner_dropdown_item, parent, false
                );
            }else{
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.spinner_item_selected, parent, false
                );
            }
        }
        TextView textViewName = convertView.findViewById(R.id.txtName);
        AddressDTO addressDTO = getItem(position);
        if (addressDTO != null) {
            textViewName.setText(addressDTO.getNombre());
        }

        return convertView;
    }

    public int getItem(AddressDTO addressDTO){
        int i = 0;
        for (AddressDTO a: values){
            if (a.getId() == addressDTO.getId()){
                i = values.indexOf(a);
            }
        }
        return i;
    }
}
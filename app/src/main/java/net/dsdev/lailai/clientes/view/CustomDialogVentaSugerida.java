package net.dsdev.lailai.clientes.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.adapters.MenuVentaSugeridaAdapter;
import net.dsdev.lailai.clientes.model.menuDetail.JsonMenuDetail;
import net.dsdev.lailai.clientes.model.menuDetail.MenuDetailList;
import net.dsdev.lailai.clientes.model.menuDetail.OptionMenuDetail;
import net.dsdev.lailai.clientes.model.menuDetail.VariantMenuDetail;
import net.dsdev.lailai.clientes.model.users.mnuSelecDTO;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;
import net.dsdev.lailai.clientes.viewHolders.MenuVentaSugeridaViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomDialogVentaSugerida extends DialogFragment implements View.OnClickListener {

    private final String TAG = "Hola";
    Fragment fragment;
    SharedPreferencesMethods sharedPreferencesMethods;
    SharedPreferences mPrefs;
    SharedPreferences.Editor prefsEditor;
    View mView;
    MenuDetailList mDetail, mShopping;
    private Button btnAccept, btnCancel;
    private RecyclerView rvVentaSugerida;
    private HashMap<Integer,Double> valores;
    private ArrayList<mnuSelecDTO> mnsSelec;
    private Gson gson;
    private String json;
    private View v;
    MenuVentaSugeridaAdapter adapter;
    Resources res;

    public CustomDialogVentaSugerida(Fragment fragment, SharedPreferencesMethods sharedPreferencesMethods, MenuDetailList menuActual, MenuDetailList menuDetailList, View v){
        this.fragment = fragment;
        this.sharedPreferencesMethods = sharedPreferencesMethods;
        this.mShopping = menuActual;
        this.mDetail = menuDetailList;
        this.v = v;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.layout_custom_venta_sugerida, container,false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bindUI();
        init();
        return mView;
    }

    public void bindUI(){
        btnAccept = mView.findViewById(R.id.btnAddToOrder);
        btnCancel = mView.findViewById(R.id.btnCancel);
        rvVentaSugerida = mView.findViewById(R.id.rvMenuVS);
        btnAccept.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        gson = new Gson();
        res = getResources();
    }

    public void init(){
        Log.d(TAG,"tamaño de las opciones: " + mDetail.getMenus().get(0).getMenu().getOptions().size());
        mPrefs = getActivity().getSharedPreferences("globalPreferences", Context.MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        valores = new HashMap<>();
        mnsSelec = new ArrayList<>();
        adapter = new MenuVentaSugeridaAdapter(getActivity().getApplicationContext()) {
            @Override
            public void setRadioGroupClickListener(final MenuVentaSugeridaViewHolder holder, final RadioGroup radioGroup, int idOption) {

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        final RadioButton radioButton = group.findViewById(checkedId);
                        Integer rbId = radioButton.getId();
                        Resources res = getResources();
                        if(radioButton.getText().equals(res.getString(R.string.noAddMenu))){
                            rbId = -1;
                        }
                        valores.remove(group.getId());
                        if(rbId == -1){
                            valores.put(group.getId(),0.0);
                        }else{
                            valores.put(group.getId(),(Double)radioButton.getTag());
                        }
                        updateText();
                        selectVariant(group.getId(), (Integer)group.getTag(), rbId);
                    }
                });
            }
        };
        adapter.setMenus(mDetail);
        rvVentaSugerida.setAdapter(adapter);
        rvVentaSugerida.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
    }

    public void selectVariant(Integer mnuId, Integer optId, Integer varId){
        ArrayList<mnuSelecDTO> tempList = mnsSelec;
        if(mnsSelec.size()>0){
            for (Integer i=0; i<mnsSelec.size(); i++){
                if(mnsSelec.get(i).getMnuId().equals(mnuId)  && mnsSelec.get(i).getOptionId().equals(optId)){
                    tempList.remove(i);
                }
            }
            mnsSelec = tempList;
            if(varId != -1){
                mnuSelecDTO mnu = new mnuSelecDTO();
                mnu.setMnuId(mnuId);
                mnu.setOptionId(optId);
                mnu.setVariantId(varId);
                mnsSelec.add(mnu);
            }
        }else{
            if(varId != -1){
                mnuSelecDTO row = new mnuSelecDTO();
                row.setMnuId(mnuId);
                row.setOptionId(optId);
                row.setVariantId(varId);
                mnsSelec.add(row);
            }
        }
    }

    public void updateText(){
        Double total = 0.0;
        for(HashMap.Entry<Integer,Double> row: valores.entrySet()){
            total += row.getValue();
        }
        if(total > 0){
            btnAccept.setText(res.getString(R.string.agregar) + " " + res.getString(R.string.moneySymbol) + total);
        }else{
            btnAccept.setText(res.getString(R.string.continuar));
        }

    }

    public void agregarMenus(){
        for (mnuSelecDTO row: mnsSelec){
            JsonMenuDetail cmenu = new JsonMenuDetail();
            //Actualizando el menu y guardando temporalmente
            for (JsonMenuDetail mnu: mDetail.getMenus()){
                if (mnu.getMenu().getIdMenu() == row.getMnuId()){

                    for (OptionMenuDetail opt: mnu.getMenu().getOptions()){
                        if(opt.getIdOption() == row.getOptionId()){

                            for (VariantMenuDetail var: opt.getVariants()){
                                if (var.getIdVariant() == row.getVariantId()){
                                    //Log.d(TAG,"Selected varians de opt " + opt.getIdOption() + " antes: " + opt.getSelectedVariants().size());
                                    opt.getSelectedVariants().clear();
                                    opt.getSelectedVariants().add(var);
                                    //Log.d(TAG,"Selected varians de opt " + opt.getIdOption() + " despues: " + opt.getSelectedVariants().size());
                                    cmenu = mnu;
                                }
                            }
                        }
                    }

                }
            }
            //reemplazando el menu por el actualizado
            JsonMenuDetail tempMenu = new JsonMenuDetail();
            for (JsonMenuDetail mnu: mDetail.getMenus()){
                if (mnu.getMenu().getIdMenu() == row.getMnuId()){
                    tempMenu = mnu;
                }
            }

            mDetail.getMenus().remove(tempMenu);
            mDetail.getMenus().add(cmenu);
        }
        //agregando el extraPrice a los menus
        for (JsonMenuDetail jmd2: mDetail.getMenus()){
            Double extraPrice = 0.0;
            for (OptionMenuDetail op: jmd2.getMenu().getOptions()){
                for (VariantMenuDetail var: op.getSelectedVariants()){
                    extraPrice += var.getExtraPrice();
                }
            }
            if(extraPrice > 0){
                jmd2.getMenu().setExtraPrice(extraPrice);
            }
            jmd2.getMenu().setFinalPrice(jmd2.getMenu().getPrice() + extraPrice);
        }

        //agregando los menus seleccionados
        List<JsonMenuDetail> lista = new ArrayList<>();
        for (JsonMenuDetail jmd: mShopping.getMenus()){
            lista.add(jmd);
        }
        for (JsonMenuDetail jmd2: mDetail.getMenus()){
            for (OptionMenuDetail op: jmd2.getMenu().getOptions()){
                if (op.getSelectedVariants().size()>0){
                    lista.add(jmd2);
                    break;
                }
            }
        }
        mShopping.setMenus(lista);
        json = gson.toJson(mShopping);
        prefsEditor.putString("shoppingCart", json);
        prefsEditor.apply();
        Navigation.findNavController(v).navigate(R.id.action_orderDetailFragment_to_directionsFragment);
        dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddToOrder:
                agregarMenus();
                break;
            case R.id.btnCancel:
                dismiss();
                break;
            default:
                break;
        }
    }
}

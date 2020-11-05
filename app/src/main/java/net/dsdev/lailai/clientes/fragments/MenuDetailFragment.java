package net.dsdev.lailai.clientes.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.dsdev.lailai.clientes.retrofit.menu.MenuDetailService;
import net.dsdev.lailai.clientes.MainActivity;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.adapters.EditMenuDetailAdapter;
import net.dsdev.lailai.clientes.adapters.MenuDetailAdapter;
import net.dsdev.lailai.clientes.model.menuDetail.JsonMenuDetail;
import net.dsdev.lailai.clientes.model.menuDetail.MenuDetail;
import net.dsdev.lailai.clientes.model.menuDetail.MenuDetailList;
import net.dsdev.lailai.clientes.model.menuDetail.OptionMenuDetail;
import net.dsdev.lailai.clientes.model.menuDetail.VariantMenuDetail;
import net.dsdev.lailai.clientes.retrofit.RetrofitInstance;
import net.dsdev.lailai.clientes.util.AddOrRemoveCallbacks;
import net.dsdev.lailai.clientes.util.Globals;
import net.dsdev.lailai.clientes.viewHolders.MenuDetailViewHolder;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuDetailFragment extends Fragment {

    private static final String TAG = "Hola";

    View rootView;
    RecyclerView recyclerView;
    ImageView imgMenu;
    MenuDetailAdapter menuDetailAdapter;
    EditMenuDetailAdapter editMenuDetailAdapter;
    MenuDetailService menuDetailService;
    JsonMenuDetail jsonMenuDetail;
    long idMenu;
    MaterialButton btnBottomCart;
    TextView txtPriceCart,txtMenuDetailName,txtMenuDetailPrice,txtRecommended;
    SharedPreferences mPrefs;
    SharedPreferences.Editor prefsEditor;
    private Gson gson;
    private String json;
    private MenuDetailList menuDetailList;
    Context context;
    private SharedPreferencesMethods sharedPreferencesMethods;
    private boolean isEditable = false;
    private int positionSelectedMenu;
    Call<JsonMenuDetail> call;
    private static final String actionBarTittle = "";
    NumberPicker numberPicker;
    private static String formatText = "Q%01.02f";
    public MenuDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_menu_detail, container, false);

        mPrefs = getActivity().getSharedPreferences("globalPreferences", Context.MODE_PRIVATE);
        bindUI();

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View include ;

        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            context = getActivity().getBaseContext();
            sharedPreferencesMethods = new SharedPreferencesMethods(activity);
            include = activity.findViewById(R.id.include);
            include.setVisibility(View.VISIBLE);
            btnBottomCart = activity.findViewById(R.id.btnBottomCart);
            btnBottomCart.setEnabled(false);
            txtPriceCart = activity.findViewById(R.id.txtPriceCart);
            btnBottomCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prefsEditor = mPrefs.edit();
                    try {
                        jsonMenuDetail.getMenu().setQuantity(numberPicker.getValue());
                    }catch (Exception e){
                        Log.d(TAG, "onClick: error al actualizar cantidad"+e.getMessage());
                    }
                    json = mPrefs.getString("shoppingCart", "");
                    if (json.equals("")){
                        menuDetailList = new MenuDetailList();
                        List<JsonMenuDetail> jsMd = new ArrayList<>();
                        jsMd.add(jsonMenuDetail);
                        menuDetailList.setMenus(jsMd);
                        //Toast.makeText(getActivity().getApplicationContext(),"Producto añadido exitosamente",Toast.LENGTH_LONG).show();
                    } else {
                        menuDetailList = gson.fromJson(json, MenuDetailList.class);
                        if (isEditable){
                            menuDetailList.getMenus().get(positionSelectedMenu).setMenu(jsonMenuDetail.getMenu());
                            //Toast.makeText(getActivity().getApplicationContext(),"Producto editado exitosamente",Toast.LENGTH_LONG).show();
                        } else {
                            menuDetailList.getMenus().add(jsonMenuDetail);
                            //Toast.makeText(getActivity().getApplicationContext(),"Producto añadido exitosamente",Toast.LENGTH_LONG).show();
                        }
                        menuDetailList.getMenus().get(positionSelectedMenu).getMenu().setExtraPrice(sharedPreferencesMethods.getMenuExtraPrice(positionSelectedMenu));
                    }

                    json = gson.toJson(menuDetailList);
                    int maxLogSize = 1000;
                    for(int i = 0; i <= json.length() / maxLogSize; i++) {
                        int start = i * maxLogSize;
                        int end = (i+1) * maxLogSize;
                        end = end > json.length() ? json.length() : end;
                        Log.v(TAG,"onClick: JSON: \n"+ json.substring(start, end));
                    }
                    /*Log.d(TAG, "onClick: JSON: \n"+json);*/
                    prefsEditor.putString("shoppingCart", json);
                    prefsEditor.apply();

                    ((AddOrRemoveCallbacks)getActivity()).onAddProduct();
                    //Navigation.findNavController(v).popBackStack();
                    NavHostFragment.findNavController(MenuDetailFragment.this).popBackStack();

                }
            });

            Log.d(TAG, "onActivityCreated: Obtiene el fab"+ btnBottomCart.getText().toString());

            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
            actionBar.setTitle(actionBarTittle);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setLogo(R.drawable.logo_toolbar);
            try{
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Toolbar toolbar;
                    toolbar = (Toolbar) getActivity().findViewById(R.id.my_toolbar);
                    toolbar.getMenu().getItem(0).setVisible(true);
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.d(TAG, "onActivityCreated: error "+e.getMessage());
            }
        }else{
            Log.d(TAG, "onActivityCreated: no obtiene el fab");
        }

        idMenu = getArguments().getLong("idMenu");
        isEditable = getArguments().getBoolean("isEditable", false);
        if (isEditable){
            btnBottomCart.setText("Editar ");
        }else {
            btnBottomCart.setText("Agregar ");
        }
        positionSelectedMenu = getArguments().getInt("positionMenu");
        if (isEditable && positionSelectedMenu >= 0){
            initEditMenuDetailAdapter(positionSelectedMenu);
        } else{
            initMenuDetailAdapter(idMenu);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (call!=null){
            call.cancel();
        }
    }

    private void bindUI(){
        recyclerView = rootView.findViewById(R.id.rvMenuDetail);
        recyclerView.setNestedScrollingEnabled(false);
        imgMenu = rootView.findViewById(R.id.imgMenu);
        txtMenuDetailName = rootView.findViewById(R.id.txtMenuDetailName);
        txtMenuDetailPrice = rootView.findViewById(R.id.txtMenuDetailPrice);
        txtRecommended = rootView.findViewById(R.id.txtRecommended);
        gson = new Gson();
        txtPriceCart = getActivity().findViewById(R.id.txtPriceCart);
        numberPicker = rootView.findViewById(R.id.numberPicker);
        numberPicker.setDisplayFocusable(false);
        numberPicker.setFocusable(false);
        numberPicker.setEnabled(false);
        numberPicker.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                String actionText = action == ActionEnum.MANUAL ? "manually set" : (action == ActionEnum.INCREMENT ? "incremented" : "decremented");
                String message = String.format("NumberPicker is %s to %d", actionText, value);
                if (jsonMenuDetail!=null && jsonMenuDetail.getMenu()!=null){
                    Double price = (jsonMenuDetail.getMenu().getPrice() * value) + (jsonMenuDetail.getMenu().getExtraPrice()*value);
                    jsonMenuDetail.getMenu().setQuantity(value);
                    jsonMenuDetail.getMenu().setFinalPrice(price);
                    txtPriceCart.setText(String.format(formatText,price));
                }
                Log.d(TAG, message);
            }
        });
        txtPriceCart.setText("Q0.00");
    }

    public void initMenuDetailAdapter(long id){
        Log.d(TAG, "init: antes de inicializar adapter "+id);
        menuDetailAdapter = new MenuDetailAdapter(getActivity().getApplicationContext()) {
            @Override
            public void setRadioGroupClickListener(MenuDetailViewHolder holder, RadioGroup radioGroup, final int idOption) {

                setSelectedVariant(idOption, radioGroup.getCheckedRadioButtonId());

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        final RadioButton radioButton = group.findViewById(checkedId);

                        //setRadioButtonColor(radioButton);

                        setSelectedVariant(idOption, checkedId);
                        /*Double extraPrice = Double.parseDouble(txtPriceCart.getText().toString());
                        txtPriceCart.setText(extraPrice.toString());*/

                        //Toast.makeText(getActivity().getApplicationContext(), radioButton.getText(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        menuDetailService = RetrofitInstance.getRetrofitInstance().create(MenuDetailService.class);
        call = menuDetailService.getMenu(id);
        call.enqueue(new Callback<JsonMenuDetail>() {
            @Override
            public void onResponse(Call<JsonMenuDetail> call, Response<JsonMenuDetail> response) {
                if (response.code() == 404){
                    Log.d(TAG, "onResponse: code "+response.code());
                    return;
                }
                if (response.body() != null){
                    jsonMenuDetail = response.body();
                    btnBottomCart.setEnabled(true);
                    numberPicker.setFocusable(true);
                    numberPicker.setEnabled(true);
                    Log.d(TAG, "onResponse: JSONMENU: "+gson.toJson(jsonMenuDetail));
                    Log.d(TAG, "onResponse: Response JSONMENU: "+gson.toJson(response.body()));
                    Log.d(TAG, "onResponse: Response JSONMENU: "+response.body().getMenu().getPrice());
                    jsonMenuDetail.getMenu().setFinalPrice(jsonMenuDetail.getMenu().getPrice());
                    //menuDetailAdapter.setOptions(response.body().getMenu().getOptions());
                    menuDetailAdapter.setOptions(jsonMenuDetail.getMenu().getOptions());
                    txtMenuDetailName.setText(jsonMenuDetail.getMenu().getName());
                    txtMenuDetailPrice.setText(String.format(formatText,jsonMenuDetail.getMenu().getPrice()));
                    txtRecommended.setText(jsonMenuDetail.getMenu().getDesc());
                    menuDetailAdapter.setOptions(jsonMenuDetail.getMenu().getOptions());
                    recyclerView.setAdapter(menuDetailAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                    try{
                        txtPriceCart.setText(String.format(formatText,jsonMenuDetail.getMenu().getPrice()));
                    }catch (Exception e){
                        Log.d(TAG, "onResponse: exception btnBottomCart "+e.toString());
                    }

                    /*if ((jsonMenuDetail.getMenu().getImages().getNormal().equals(""))) {
                        Log.d(TAG, "onResponse: NORMAL: " + jsonMenuDetail.getMenu().getImages().getUnavailable());
                    } else {
                        Log.d(TAG, "onResponse: UNAVAILABLE: " + jsonMenuDetail.getMenu().getImages().getNormal());
                    }*/
                    Glide.with(context)
                            .load((jsonMenuDetail.getMenu().getImages().getNormal()==null)?jsonMenuDetail.getMenu().getImages().getUnavailable():jsonMenuDetail.getMenu().getImages().getNormal())
                            .fitCenter()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imgMenu);


                } else {
                    Log.d(TAG, "onResponse: null");
                }
            }

            @Override
            public void onFailure(Call<JsonMenuDetail> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    public void initEditMenuDetailAdapter(int positionMenu){
        sharedPreferencesMethods = new SharedPreferencesMethods(getActivity());
        JsonMenuDetail currentMenu = sharedPreferencesMethods.getMenuByPosition(positionMenu);
        jsonMenuDetail = currentMenu;
        editMenuDetailAdapter = new EditMenuDetailAdapter(getActivity().getApplicationContext()) {
            @Override
            public void setRadioGroupClickListener(MenuDetailViewHolder holder, RadioGroup radioGroup, final int idOption) {
                setSelectedVariant(idOption, radioGroup.getCheckedRadioButtonId());

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = group.findViewById(checkedId);

                        //setRadioButtonColor(radioButton);
                        setSelectedVariant(idOption, checkedId);

                        //Toast.makeText(getActivity().getApplicationContext(), radioButton.getText(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        MenuDetail menuDetail = currentMenu.getMenu();
        editMenuDetailAdapter.setOptions(menuDetail.getOptions());
        txtMenuDetailName.setText(menuDetail.getName());
        txtMenuDetailPrice.setText(String.format(formatText,menuDetail.getPrice()));
        /* Cambiar por catch de objeto vacio */
        if (menuDetail.getImages() != null){
            Glide.with(context)
                    .load((menuDetail.getImages().getNormal() != null && !menuDetail.getImages().getNormal().equals(""))
                                    ? menuDetail.getImages().getNormal()
                                    : menuDetail.getImages().getUnavailable())
                    .fitCenter()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgMenu);
        }
        txtPriceCart.setText(String.format(formatText,menuDetail.getFinalPrice()));
        //cantidad
        numberPicker.setValue(menuDetail.getQuantity());
        recyclerView.setAdapter(editMenuDetailAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        btnBottomCart.setEnabled(true);
        numberPicker.setFocusable(true);
        numberPicker.setEnabled(true);
    }

    public void setSelectedVariant(int idOption, int checkedIdVariant){
        List<OptionMenuDetail> optionMenuDetail = jsonMenuDetail.getMenu().getOptions();
        for (int i = 0; i<optionMenuDetail.size(); i++){
            if (optionMenuDetail.get(i).getIdOption()==idOption){
                for (int j=0; j<optionMenuDetail.get(i).getVariants().size(); j++){
                    VariantMenuDetail variant = optionMenuDetail.get(i).getVariants().get(j);
                    if (variant.getIdVariant() == checkedIdVariant){
                        Log.d(TAG, "onCheckedChanged: selectedVariant: "+variant.getVariant());
                        List<VariantMenuDetail> selectedVariants = jsonMenuDetail.getMenu().getOptions().get(i).getSelectedVariants();
                        if (selectedVariants.size()>0){
                            jsonMenuDetail.getMenu().getOptions().get(i).getSelectedVariants().set(0,variant);
                        } else {
                            jsonMenuDetail.getMenu().getOptions().get(i).getSelectedVariants().add(variant);
                        }
                        //Double price = jsonMenuDetail.getMenu().getPrice() + jsonMenuDetail.getMenu().getOptions().get(i).getSelectedVariants().get(0).getExtraPrice();
                        //Double price = jsonMenuDetail.getMenu().getFinalPrice() + (jsonMenuDetail.getMenu().getOptions().get(i).getSelectedVariants().get(0).getExtraPrice()*numberPicker.getValue());
                        jsonMenuDetail.getMenu().setExtraPrice(jsonMenuDetail.getMenu().getOptions().get(i).getSelectedVariants().get(0).getExtraPrice());
                        Double price = (jsonMenuDetail.getMenu().getPrice() * numberPicker.getValue()) + (jsonMenuDetail.getMenu().getExtraPrice()*numberPicker.getValue());
                        jsonMenuDetail.getMenu().setFinalPrice(price);
                        txtPriceCart.setText(String.format(formatText,price));
                        //txtPriceCart.setText("Q"+price.toString());
                        //jsonMenuDetail.getMenu().setFinalPrice();
                        //jsonMenuDetail.getMenu().getOptions().get(i).setSelectedVariant(variant);
                        //jsonMenuDetail.getMenu().getOptions().get(i).getSelectedVariants().add(0,variant);

                        break;
                    }
                }
                break;
            }
        }
    }

    private void setRadioButtonColor(final RadioButton radioButton){
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int textColor= Color.parseColor("#fbc02d");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    radioButton.setButtonTintList(ColorStateList.valueOf(textColor));
                }
                if (isChecked) {
                    textColor = Color.parseColor("#fbc02d");
                } else {
                    textColor = Color.parseColor("#000000");
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    radioButton.setButtonTintList(ColorStateList.valueOf(textColor));
                }
            }
        });
    }

}

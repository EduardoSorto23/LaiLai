package net.dsdev.lailai.clientes.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import net.dsdev.lailai.clientes.MainActivity;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.adapters.MenuAdapter;
import net.dsdev.lailai.clientes.model.Categories;
import net.dsdev.lailai.clientes.model.Menus;
import net.dsdev.lailai.clientes.model.SubCategories;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    View rootView;
    RecyclerView rvMenus;
    MenuAdapter menuAdapter;
    RadioGroup radioGroup;
    TextView txtSubCategories,txtPriceCart;
    SharedPreferencesMethods sharedPreferencesMethods;
    TextInputEditText etSearch;
    private static final String actionBarTittle = "";
    public static final String TAG = "Hola";
    ImageButton imgSearch;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        bindUI();
        init();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View include ;
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            sharedPreferencesMethods = new SharedPreferencesMethods(activity);
            include = activity.findViewById(R.id.include);
            include.setVisibility(View.VISIBLE);
            MaterialButton btnBottomCart = activity.findViewById(R.id.btnBottomCart);
            btnBottomCart.setText(R.string.view_order);
            btnBottomCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(rootView).navigate(R.id.action_menuFragment_to_orderDetailFragment);
                }
            });
            txtPriceCart = activity.findViewById(R.id.txtPriceCart);
            txtPriceCart.setText("Q"+sharedPreferencesMethods.getFinalPrice().toString());
            Log.d(TAG, "onActivityCreated: Obtiene el fab"+btnBottomCart.getText().toString());

            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
            actionBar.setTitle(actionBarTittle);
            actionBar.setDisplayHomeAsUpEnabled(true);
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
    }

    public void init() {
        ///
        menuAdapter = new MenuAdapter(getActivity().getApplicationContext()) {
            @Override
            public void setClickListener(final MenuViewHolder holder, final int position, final Menus menu) {

                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navigateToMenuDetailFragment(menu, v);
                    }
                    /*@Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("menus",subCategory);
                        Navigation.findNavController(v).navigate(R.id.action_subCategoriesFragment_to_menuFragment,bundle);
                    }*/
                });
                holder.btnPrecio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navigateToMenuDetailFragment(menu, v);
                    }
                });
            }

            @Override
            public void setClickListener(MenuAdapter.MenuListViewHolder holder, int position, final Menus menu) {
                holder.getClMenuList().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navigateToMenuDetailFragment(menu, v);
                    }
                });
            }
        };

        SubCategories subCategory = (SubCategories) getArguments().getSerializable("subCategory");
        final Categories category = (Categories) getArguments().getSerializable("category");

        try {
            Log.d(TAG, "init: category.getMenus().size() " + category.getMenus().size());
            Log.d(TAG, "init: category.getMenus() " + category.getMenus());
        }catch (Exception e){
            Log.d(TAG, "init: exception "+e.toString());
        }
        if (subCategory == null && (category.getMenus() != null && category.getMenus().size() > 0)){
            Log.d(TAG, "init: setear Menu");
            menuAdapter.setMenu(null,category.getMenus());
        }else if(subCategory!=null){
            Log.d(TAG, "init: setear subCategory");
            menuAdapter.setMenu(subCategory,null);
        }
        //menuAdapter.setList(false);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbList:
                        Log.d(TAG, "onCheckedChanged: check list");
                        txtSubCategories.setVisibility(View.VISIBLE);
                        menuAdapter.setList(true);
                        break;
                    case R.id.rbCrop:
                        Log.d(TAG, "onCheckedChanged: check crop");
                        txtSubCategories.setVisibility(View.GONE);
                        menuAdapter.setList(false);
                        break;
                }
            }
        });

        rvMenus.setAdapter(menuAdapter);
        rvMenus.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        /*etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                menuAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_menuFragment_to_searchFragment);
            }
        });

    }

    public void bindUI(){
        rvMenus = rootView.findViewById(R.id.rvMenus);
        //rvMenus.setNestedScrollingEnabled(false);
        radioGroup = rootView.findViewById(R.id.rgToList);
        txtSubCategories = rootView.findViewById(R.id.txtSubCategories);
        etSearch = rootView.findViewById(R.id.metSearch);
        imgSearch = rootView.findViewById(R.id.imgSearch);
    }

    public void navigateToMenuDetailFragment(Menus menu, View v){
        Bundle bundle = new Bundle();
        bundle.putLong("idMenu", menu.getId());
        bundle.putBoolean("isEditable", false);
        Navigation.findNavController(v).navigate(R.id.action_menuFragment_to_menuDetailFragment, bundle);
    }
}

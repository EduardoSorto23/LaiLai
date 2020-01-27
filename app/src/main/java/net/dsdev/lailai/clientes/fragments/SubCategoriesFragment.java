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

import net.dsdev.lailai.clientes.MainActivity;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.adapters.SubCategoryAdapter;
import net.dsdev.lailai.clientes.model.Categories;
import net.dsdev.lailai.clientes.model.SubCategories;
import net.dsdev.lailai.clientes.viewHolders.CategoryViewHolder;
import com.google.android.material.button.MaterialButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubCategoriesFragment extends Fragment {

    View rootView;
    RecyclerView rvCategories;
    SubCategoryAdapter subCategoryAdapter;
    ImageButton imgSearch;
    public static final String TAG = "Hola";
    private static final String actionBarTittle = "";
    public SubCategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_sub_categories, container, false);
        Log.d(TAG, "onCreateView: ");
        bindUI();
        init();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
        View include ;
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            include = activity.findViewById(R.id.include);
            include.setVisibility(View.GONE);
            MaterialButton btnButtomCart = activity.findViewById(R.id.btnBottomCart);
            Log.d(TAG, "onActivityCreated: Obtiene el fab"+btnButtomCart.getText().toString());
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
            actionBar.setTitle(actionBarTittle);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setLogo(R.drawable.logo_toolbar);
            try{
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Toolbar toolbar;
                    toolbar = getActivity().findViewById(R.id.my_toolbar);
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
        subCategoryAdapter = new SubCategoryAdapter(getActivity().getApplicationContext()) {
            @Override
            public void setClickListener(CategoryViewHolder holder, final SubCategories subCategory, final int position) {

                holder.getCvCategory().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "setClickListener: hola");
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("subCategory",subCategory);
                        Navigation.findNavController(v).navigate(R.id.action_subCategoriesFragment_to_menuFragment,bundle);
                    }
                });
            }
        };

        Categories category = (Categories) getArguments().getSerializable("category");
        Log.d(TAG, "init: category "+category.getSubCategories().size());
        subCategoryAdapter.setCategory(category);
        rvCategories.setAdapter(subCategoryAdapter);
        rvCategories.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Qlero aqui manda al fragment de busqueda
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    public void bindUI(){
        rvCategories = rootView.findViewById(R.id.rvCategories);
        rvCategories.setNestedScrollingEnabled(false);
        imgSearch = rootView.findViewById(R.id.imgSearch);
    }
}

package net.dsdev.lailai.clientes.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import net.dsdev.lailai.clientes.MainActivity;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.adapters.CategoryAdapter;
import net.dsdev.lailai.clientes.adapters.MenuAdapter;
import net.dsdev.lailai.clientes.adapters.SliderHomeAdapter;
import net.dsdev.lailai.clientes.model.Categories;
import net.dsdev.lailai.clientes.model.JsonMenus;
import net.dsdev.lailai.clientes.model.Promo.PromoList;
import net.dsdev.lailai.clientes.retrofit.Promo.PromoService;
import net.dsdev.lailai.clientes.retrofit.RetrofitInstance;
import net.dsdev.lailai.clientes.retrofit.menu.MenuService;
import net.dsdev.lailai.clientes.util.Constants;
import net.dsdev.lailai.clientes.util.Globals;
import net.dsdev.lailai.clientes.viewHolders.HomeViewHolder;
import net.dsdev.lailai.clientes.viewHolders.SliderAdapterVH;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.navigation.Navigation.findNavController;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    //public static String MENU_SAVED = "";
    static int secondsForSlider = 7;
    View rootView;
    SliderView sliderView;
    private Toolbar mTopToolbar;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    RecyclerView rvIniHome;
    CategoryAdapter categoryAdapter;
    SliderHomeAdapter sliderAdapter;

    MenuService menuService;
    MenuAdapter menuAdapter;
    private String TAG = "Hola";
    PromoList promoList;
    SwipeRefreshLayout swipe_container;
    ImageButton imgLeftArrow,imgRightArrow,imgSearch;
    SharedPreferencesMethods sharedPreferencesMethods;
    Call<JsonMenus> call;

    private ShimmerFrameLayout shimmerFrameLayout;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        bindUI();
        return rootView;
    }

    public void init(){
        sharedPreferencesMethods = new SharedPreferencesMethods(getActivity());
        swipe_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setPromoList();
                retrofit();
                swipe_container.setRefreshing(false);
            }
        });

        imgRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG, "onClick: imgRightArrow"+sliderView.getCurrentPagePosition());
                try {
                    sliderView.setCurrentPagePosition(sliderView.getCurrentPagePosition()+1);
                }catch (Exception e){
                    Log.d(TAG, "onClick: bla bla : "+e.getLocalizedMessage());
                }
            }
        });

        imgLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG, "onClick: imgRightArrow"+sliderView.getCurrentPagePosition());
                try {
                    sliderView.setCurrentPagePosition(sliderView.getCurrentPagePosition()-1);
                }catch (Exception e){
                    Log.d(TAG, "onClick: bla bla : "+e.getLocalizedMessage());
                }
            }
        });

        prefs = getActivity().getSharedPreferences("globalPreferences", Context.MODE_PRIVATE);
        sliderAdapter = new SliderHomeAdapter(getActivity().getApplicationContext()) {
            @Override
            public void setClickListener(SliderAdapterVH holder, final long id) {
                holder.getItemView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "setClickListener: id promo :"+id);
                        Bundle bundle = new Bundle();
                        bundle.putLong("idMenu", id);
                        bundle.putBoolean("isEditable", false);
                        Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_menuDetailFragment, bundle);
                    }
                });
            }
        };
        setPromoList();
        sliderView.setScrollTimeInSec(secondsForSlider);
        sliderView.setIndicatorAnimation(IndicatorAnimations.SLIDE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.startAutoCycle();
        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderView.setCurrentPagePosition(position);
            }
        });


        categoryAdapter = new CategoryAdapter(getActivity().getApplicationContext()) {
            @Override
            public void setClickListener(HomeViewHolder holder, final Categories category, int position) {
                holder.getCardView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getActivity().getApplicationContext(), "Hola " + jsonMenus.getCategories().get((int)id).getId(), Toast.LENGTH_LONG).show();
                        // ConfirmationAction action = SpecifyAmountFragmentDirections.confirmationAction();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("category", category);
                        if (category.getSubCategory().equals("Si")){
                            Navigation.findNavController(v).navigate(R.id.subCategoriesFragment, bundle);

                        }else{
                            Navigation.findNavController(v).navigate(R.id.menuFragment, bundle);
                            Log.d(TAG, "onClick: no tiene subcategoria");
                            //Navigation.findNavController(v).navigate(R.id.menuFragment, bundle);
                        }
                        
                    }
                });
            }
        };

        retrofit();


        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_searchFragment);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmerAnimation();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (call!=null){
            call.cancel();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
       super.onActivityCreated(savedInstanceState);
        View include ;
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            include = activity.findViewById(R.id.include);
            include.setVisibility(View.GONE);
            MaterialButton btnButtomCart = activity.findViewById(R.id.btnBottomCart);
            Log.d(TAG, "onActivityCreated: Obtiene el fab"+btnButtomCart.getText().toString());
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
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
        init();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmerAnimation();
    }

    private void bindUI(){
        rvIniHome = rootView.findViewById(R.id.rvIniHome);
        sliderView = rootView.findViewById(R.id.imageSlider);
        rvIniHome.setNestedScrollingEnabled(false);
        swipe_container = rootView.findViewById(R.id.swipe_container);
        imgLeftArrow = rootView.findViewById(R.id.imgLeftArrow);
        imgRightArrow = rootView.findViewById(R.id.imgRightArrow);
        imgSearch = rootView.findViewById(R.id.imgSearch);
        shimmerFrameLayout = rootView.findViewById(R.id.shimmer_view_container);
        //shimmerFrameLayout.startShimmerAnimation();
    }

    public void retrofit(){
        if (Globals.MENUS_SAVED != null && !Globals.MENUS_SAVED.equals("")){
            shimmerFrameLayout.stopShimmerAnimation();
            shimmerFrameLayout.setVisibility(View.GONE);
            categoryAdapter.setMenu(new Gson().fromJson(Globals.MENUS_SAVED,JsonMenus.class));
            rvIniHome.setAdapter(categoryAdapter);
            rvIniHome.setLayoutManager(
                    new GridLayoutManager(getActivity().getApplicationContext(),2));
        }else {
            menuService = RetrofitInstance.getRetrofitInstance().create(MenuService.class);
            call = menuService.getMenus();
            call.enqueue(new Callback<JsonMenus>() {
                @Override
                public void onResponse(Call<JsonMenus> call, Response<JsonMenus> response) {
                    if (response.code() == 404) {
                        Log.d(TAG, "onResponse: code " + response.code());
                        return;
                    }
                    if (response.body() != null) {
                        sharedPreferencesMethods.saveMenuTree(Constants.menuTree, new Gson().toJson(response.body()));
                        Globals.MENUS_SAVED = new Gson().toJson(response.body());
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);

                        categoryAdapter.setMenu(response.body());
                        rvIniHome.setAdapter(categoryAdapter);
                        rvIniHome.setLayoutManager(
                                new GridLayoutManager(getActivity().getApplicationContext(), 2));
                    } else {
                        Log.d(TAG, "onResponse: null");
                    }
                }

                @Override
                public void onFailure(Call<JsonMenus> call, Throwable t) {
                    Log.d(TAG, "onFailure : " + t.getMessage());
                }
            });
        }

    }

    private void setPromoList(){
        if (Globals.PROMOS_SAVED != null && !Globals.PROMOS_SAVED.equals("")){
            promoList = new Gson().fromJson(Globals.PROMOS_SAVED,PromoList.class);
            sliderAdapter.setPromoList(promoList);
            sliderView.setSliderAdapter(sliderAdapter);
        }else{
            promoList = new PromoList();
            PromoService promoService = RetrofitInstance.getRetrofitInstance().create(PromoService.class);
            Call<PromoList> callPromos = promoService.getPromos();
            callPromos.enqueue(new Callback<PromoList>() {
                @Override
                public void onResponse(Call<PromoList> call, Response<PromoList> response) {
                    if (response.code() == 404){
                        Log.d(TAG, "onResponse: code "+response.code());
                        return;
                    }
                    if(response.body() != null){
                        promoList = response.body();
                        Globals.PROMOS_SAVED = new Gson().toJson(response.body());
                        sliderAdapter.setPromoList(promoList);
                        sliderView.setSliderAdapter(sliderAdapter);
                    }
                    Log.d(TAG, "onResponse: getPromos ");
                }

                @Override
                public void onFailure(Call<PromoList> call, Throwable t) {
                    Log.d(TAG, "onFailure : " +t.getMessage());
                }
            });
        }
    }
}

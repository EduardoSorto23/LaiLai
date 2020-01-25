package net.dsdev.lailai.clientes.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import net.dsdev.lailai.clientes.MainActivity;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.adapters.SearchAdapter;
import net.dsdev.lailai.clientes.model.Menus;
import net.dsdev.lailai.clientes.util.Constants;
import net.dsdev.lailai.clientes.viewHolders.search.SearchHolder;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    View rootView;
    RecyclerView recyclerView;
    EditText etSearch;
    SearchAdapter searchAdapter;
    SharedPreferencesMethods sharedPreferencesMethods;
    List<Menus> menus;
    private static final String TAG = "Hola";

    public SearchFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        bindUI();
        init();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View include;
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            include = activity.findViewById(R.id.include);
            include.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void bindUI(){
        recyclerView = rootView.findViewById(R.id.rvMenusSearch);
        etSearch = rootView.findViewById(R.id.metSearch);
        //recyclerView.setNestedScrollingEnabled(false);
    }

    private void init(){
        sharedPreferencesMethods = new SharedPreferencesMethods(getActivity());
        menus = getMenus();
        searchAdapter = new SearchAdapter(getActivity().getApplicationContext()) {
            @Override
            public void setClickListener(SearchHolder holder, final Menus menu) {
                holder.getClMenuList().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putLong("idMenu",menu.getId());
                        bundle.putBoolean("isEditable", false);
                        Navigation.findNavController(v).navigate(R.id.action_searchFragment_to_menuDetailFragment, bundle);
                    }
                });
            }
        };

        searchAdapter.setMenus(menus);
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public List<Menus> getMenus (){
        return sharedPreferencesMethods.getMenusTree(Constants.menuTree);
    }

}

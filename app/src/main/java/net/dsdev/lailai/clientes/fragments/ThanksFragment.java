package net.dsdev.lailai.clientes.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.dsdev.lailai.clientes.MainActivity;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.util.Globals;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThanksFragment extends Fragment {

    public static final String TAG = "Hola";
    View rootView;
    private TextView lblOrderCode,lblEmail;
    private Button btnGoToHome;
    private String orderCode;
    private SharedPreferencesMethods sharedPreferencesMethods;
    private static final String actionBarTittle = "Gracias!";

    public ThanksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_thanks, container, false);
        bindUI();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void bindUI() {
        lblOrderCode = rootView.findViewById(R.id.lblOrderCode);
        lblEmail = rootView.findViewById(R.id.lblEmail);
        btnGoToHome = rootView.findViewById(R.id.btnGoToHome);
    }

    private void init(){
        sharedPreferencesMethods = new SharedPreferencesMethods(getActivity());
        btnGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHome();
            }
        });

        if (getArguments()!=null){
            orderCode = getArguments().getString("orderCode","");
        }

        String format = "Pedido No. %s";
        lblOrderCode.setText(String.format(format,orderCode));
        lblEmail.setText(sharedPreferencesMethods.getLoggedUser().getEmail());

    }

    private void goToHome() {
        /*final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Navigation.findNavController(rootView).navigate(R.id.action_thanksFragment_to_homeFragment);
            }
        }, 2000);*/
        Navigation.findNavController(rootView).navigate(R.id.action_thanksFragment_to_homeFragment);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.clear();
        super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View include ;
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            include = activity.findViewById(R.id.include);
            include.setVisibility(View.GONE);
        }else{
            Log.d(TAG, "onActivityCreated: no obtiene el fab");
        }
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(actionBarTittle);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayUseLogoEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        }
        setHasOptionsMenu(true);
    }
}

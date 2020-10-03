package net.dsdev.lailai.clientes.fragments.user;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.dsdev.lailai.clientes.MainActivity;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    SharedPreferencesMethods sharedPreferencesMethods;
    MaterialCardView cvChangePassword,cvDirections,cvCards;
    TextView user_profile_name, user_profile_short_bio;

    public ProfileFragment() {
        // Required empty public constructor
    }
    private String TAG = "Hola";
    View rootView;
    Toolbar toolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.layout_profile, container, false);
        bindUI();
        try{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Toolbar toolbar;
                toolbar = getActivity().findViewById(R.id.my_toolbar);
                toolbar.getMenu().getItem(0).setVisible(false);
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "onActivityCreated: error "+e.getMessage());
        }

        init();
        return rootView;
    }

    private void init() {
        sharedPreferencesMethods = new SharedPreferencesMethods(getActivity());
        user_profile_name.setText(sharedPreferencesMethods.getLoggedUser().getNames());
        user_profile_short_bio.setText(sharedPreferencesMethods.getLoggedUser().getEmail());
        if (!sharedPreferencesMethods.getLoggedUser().getAccountType().equals("LOCAL")){
            cvChangePassword.setVisibility(View.GONE);
        }
        cvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_changePasswordFragment);
            }
        });
        cvDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToFragment(v);
            }
        });
        cvCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToCardsFragment(v);
            }
        });
    }

    private void bindUI() {
        cvChangePassword = rootView.findViewById(R.id.cvChangePassword);
        cvDirections = rootView.findViewById(R.id.cvDirections);
        cvCards = rootView.findViewById(R.id.cvCards);
        user_profile_short_bio = rootView.findViewById(R.id.user_profile_short_bio);
        user_profile_name = rootView.findViewById(R.id.user_profile_name);
    }

    private void navigateToFragment(View v){
        Bundle bundle = new Bundle();
        bundle.putString("action", "profile");
        Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_directionsFragment, bundle);
    }
    private void navigateToCardsFragment(View v){
        Bundle bundle = new Bundle();
        bundle.putString("action", "profile");
        Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_paymentMethodFragment, bundle);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View include ;
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            include = activity.findViewById(R.id.include);
            include.setVisibility(View.GONE);
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setLogo(R.drawable.logo_toolbar);
        }else{
            Log.d(TAG, "onActivityCreated: no obtiene el fab");
        }
    }
}

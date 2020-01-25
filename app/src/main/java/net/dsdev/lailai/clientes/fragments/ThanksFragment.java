package net.dsdev.lailai.clientes.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.dsdev.lailai.clientes.MainActivity;
import net.dsdev.lailai.clientes.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThanksFragment extends Fragment {

    public static final String TAG = "Hola";
    View rootView;

    public ThanksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_thanks, container, false);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Navigation.findNavController(rootView).navigate(R.id.action_thanksFragment_to_homeFragment);
            }
        }, 2000);

        return rootView;
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
    }
}

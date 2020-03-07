package net.dsdev.lailai.clientes.fragments.user;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.dsdev.lailai.clientes.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrackOrderFragment extends Fragment {


    public TrackOrderFragment() {
        // Required empty public constructor
    }

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_track_order, container, false);
        init();
        return rootView;
    }

    private void init() {

    }


}

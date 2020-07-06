package net.dsdev.lailai.clientes.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import net.dsdev.lailai.clientes.MainActivity;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.adapters.item.SpinAdapter;
import net.dsdev.lailai.clientes.model.users.Address;
import net.dsdev.lailai.clientes.model.users.AddressDTO;
import net.dsdev.lailai.clientes.model.users.AuthResponse;
import net.dsdev.lailai.clientes.retrofit.RetrofitInstance;
import net.dsdev.lailai.clientes.retrofit.users.AddressService;
import net.dsdev.lailai.clientes.util.Constants;
import net.dsdev.lailai.clientes.util.RandomMethods;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDirectionFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    View rootView;
    private static final String actionBarTittle = "Direcciones";
    private static final String TAG = "Hola";
    private Context context;
    private String api_key = "";
    private Marker markerUser;
    private static final int REQUEST_LOCATION_PERMISSION = 99;
    private GoogleMap gMap;
    AutocompleteSupportFragment autocompleteFragment;
    //FusedLocationProviderClient mFusedLocationClient;

    /*UI*/
    RadioGroup rgDirections;
    TextInputEditText etDirection, etOther, etTelephone, etIndication, etCol, etZone, etAccessCode, etHouseNumber;
    TextInputLayout tlDirection, tlOther, tlTelephone, tlIndication, tlCol, tlZone, tlAccessCode, tlHouseNumber;
    MaterialButton btnEditDirection, btnCancel, btnDeleteDirection;
    RadioButton rbHouse, rbWork, rbOther;
    /**/
    AddressService addressService;
    Call<AuthResponse> call;
    Address currentAddress;
    Double latitudeSelected, longitudeSelected;
    SharedPreferencesMethods sharedPreferencesMethods;
    List<AddressDTO> departmentList;
    List<AddressDTO> municipalyList;
    private Spinner spDepartments, spMunicipalies;
    Boolean isFirst = true;
    SpinAdapter muniAdapter,deptoAdapter;
    MaterialCardView cvMap;
    public EditDirectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_edit_direction, container, false);
        context = rootView.getContext();
        bindUI();
        init();
        api_key = getResources().getString(R.string.google_maps_key);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void bindUI() {
        rgDirections = rootView.findViewById(R.id.rgDirections);
        etDirection = rootView.findViewById(R.id.etDirection);
        etOther = rootView.findViewById(R.id.etOther);
        btnEditDirection = rootView.findViewById(R.id.btnEditDirection);
        btnCancel = rootView.findViewById(R.id.btnCancel);
        btnDeleteDirection = rootView.findViewById(R.id.btnDeleteDirection);
        tlOther = rootView.findViewById(R.id.tlOther);
        rbHouse = rootView.findViewById(R.id.rbHouse);
        rbWork = rootView.findViewById(R.id.rbWork);
        rbOther = rootView.findViewById(R.id.rbOther);
        tlDirection = rootView.findViewById(R.id.tlDirection);
        tlTelephone = rootView.findViewById(R.id.tlTelephone);
        etTelephone = rootView.findViewById(R.id.etTelephone);

        etIndication = rootView.findViewById(R.id.etIndication);
        tlIndication = rootView.findViewById(R.id.tlIndication);

        spDepartments = rootView.findViewById(R.id.spDepartments);
        spMunicipalies = rootView.findViewById(R.id.spMunicipalies);

        etCol = rootView.findViewById(R.id.etCol);
        etZone = rootView.findViewById(R.id.etZone);
        etAccessCode = rootView.findViewById(R.id.etAccessCode);
        tlCol = rootView.findViewById(R.id.tlCol);
        tlZone = rootView.findViewById(R.id.tlZone);
        tlAccessCode = rootView.findViewById(R.id.tlAccessCode);

        tlHouseNumber = rootView.findViewById(R.id.tlHouseNumber);
        etHouseNumber = rootView.findViewById(R.id.etHouseNumber);
        cvMap = rootView.findViewById(R.id.cvMap);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        View include;
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            include = activity.findViewById(R.id.include);
            include.setVisibility(View.GONE);
        } else {
            Log.d(TAG, "onActivityCreated: no obtiene el fab");
        }
        if (actionBar != null) {
            actionBar.setTitle(actionBarTittle);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        }
        setHasOptionsMenu(true);
        MapView mapView = rootView.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    private void init() {
        sharedPreferencesMethods = new SharedPreferencesMethods(getActivity());
        bindData();
        //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        RandomMethods.setTextWatcher(etOther, tlOther);
        rgDirections.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbHouse:
                        etOther.setEnabled(false);
                        tlOther.setError("");
                        break;
                    case R.id.rbWork:
                        etOther.setEnabled(false);
                        tlOther.setError("");
                        break;
                    case R.id.rbOther:
                        etOther.setEnabled(true);
                        etOther.requestFocus();
                        if ((etOther.getText() == null || etOther.getText().toString().isEmpty())) {
                            tlOther.setError("Por favor escribe un nombre de la dirección");
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        btnEditDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: rgDirection " + rgDirections.getCheckedRadioButtonId());
                if (validateFields()) {
                    editAddress();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });
        btnDeleteDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressService service = RetrofitInstance.getRetrofitInstance().create(AddressService.class);
                Call<AuthResponse> deleteCall = service.deleteAddress(currentAddress.getId());
                deleteCall.enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, final Response<AuthResponse> response) {
                        if (response.body() != null) {
                            new MaterialAlertDialogBuilder(context, R.style.MyDialogThemeMaterialLight)
                                    .setTitle("Dirección")
                                    .setMessage(response.body().getMsg())
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (response.body().isValid()) {
                                                NavHostFragment.findNavController(EditDirectionFragment.this).popBackStack();
                                            } else {
                                                dialogInterface.dismiss();
                                            }
                                        }
                                    })
                                    .show();
                        } else {
                            Toast.makeText(context, "Un error ha ocurrido por favor intente de nuevo", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: al enviar direccion");
                    }
                });
            }
        });

        listDepartments();
    }

    private void listDepartments() {
        AddressService service = RetrofitInstance.getRetrofitInstance().create(AddressService.class);
        Call<LinkedHashMap<String, List<AddressDTO>>> callDeptos = service.getDepartments();
        callDeptos.enqueue(new Callback<LinkedHashMap<String, List<AddressDTO>>>() {
            @Override
            public void onResponse(Call<LinkedHashMap<String, List<AddressDTO>>> call, Response<LinkedHashMap<String, List<AddressDTO>>> response) {
                if (response.body() != null) {
                    LinkedHashMap<String, List<AddressDTO>> res = response.body();
                    departmentList = res.get(Constants.departments);
                    initDepartmentsAdapter();
                }
            }

            @Override
            public void onFailure(Call<LinkedHashMap<String, List<AddressDTO>>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void initDepartmentsAdapter() {
        deptoAdapter = new SpinAdapter(getContext().getApplicationContext(), R.layout.spinner_dropdown_item, departmentList);
        spDepartments.setAdapter(deptoAdapter); // Set the custom adapter to the spinner
        spDepartments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                AddressDTO depto = deptoAdapter.getItem(position);
                if (depto != null) {
                    municipalyList = new ArrayList<>();
                    municipalyList.addAll(depto.getMunicipalies());
                    initMunicipaliesAdapter();
                    //listMunicipalies(depto.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
        if (currentAddress.getoDepartment() != null) {
            spDepartments.setSelection(deptoAdapter.getItem(currentAddress.getoDepartment()), true);
        }
    }

    private void listMunicipalies(Long idDepto) {
        AddressService service = RetrofitInstance.getRetrofitInstance().create(AddressService.class);
        Call<LinkedHashMap<String, List<AddressDTO>>> callDeptos = service.getMunicipalies(idDepto);
        callDeptos.enqueue(new Callback<LinkedHashMap<String, List<AddressDTO>>>() {
            @Override
            public void onResponse(Call<LinkedHashMap<String, List<AddressDTO>>> call, Response<LinkedHashMap<String, List<AddressDTO>>> response) {
                if (response.body() != null) {
                    LinkedHashMap<String, List<AddressDTO>> res = response.body();
                    municipalyList = res.get(Constants.municipalies);
                    initMunicipaliesAdapter();
                }
            }

            @Override
            public void onFailure(Call<LinkedHashMap<String, List<AddressDTO>>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void initMunicipaliesAdapter() {
        muniAdapter = new SpinAdapter(getContext().getApplicationContext(), R.layout.spinner_dropdown_item, municipalyList);
        spMunicipalies.setAdapter(muniAdapter); // Set the custom adapter to the spinner
        spMunicipalies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

        if (isFirst) {
            if (currentAddress.getoMunicipaly() != null) {
                spMunicipalies.setSelection(muniAdapter.getItem(currentAddress.getoMunicipaly()), true);
            }
            isFirst = false;
        }
    }

    private void bindData() {
        if (getArguments() != null) {
            String current = getArguments().getString("address");
            currentAddress = new Gson().fromJson(current, Address.class);
        }

        if (currentAddress != null) {
            switch (currentAddress.getNombre()) {
                case "Casa":
                    rbHouse.setChecked(true);
                    rbWork.setChecked(false);
                    rbOther.setChecked(false);
                    break;
                case "Trabajo":
                    rbHouse.setChecked(false);
                    rbWork.setChecked(true);
                    rbOther.setChecked(false);
                    break;
                default:
                    rbHouse.setChecked(false);
                    rbWork.setChecked(false);
                    rbOther.setChecked(true);
                    etOther.setText(currentAddress.getNombre());
                    break;
            }
            etDirection.setText(currentAddress.getDirection());
            etIndication.setText(currentAddress.getReferences());
            etTelephone.setText(currentAddress.getTelephone());

            etCol.setText(currentAddress.getSuBurb());
            etZone.setText(currentAddress.getZone());
            etAccessCode.setText(currentAddress.getAccessCode());
            etHouseNumber.setText(currentAddress.getHouseNumber());
        }
    }

    private void editAddress() {
        Address address = new Address();
        //address.setIdAddress(currentAddress.getId());
        address.setId(currentAddress.getId());
        address.setIdClient(sharedPreferencesMethods.getLoggedUser().getIdCliente());
        address.setNombre(getAddressName());

        //address.setDepartment(etDepartment.getText().toString());
        //address.setMunicipaly(etMunicipality.getText().toString());
        //AddressDTO depto = (AddressDTO) spDepartments.getSelectedItem();
        AddressDTO muni = (AddressDTO) spMunicipalies.getSelectedItem();
        //address.setoDepartment(depto);
        address.setoMunicipaly(muni);
        address.setReferences(etIndication.getText().toString());
        address.setDirection(etDirection.getText().toString());
        address.setLatitude(latitudeSelected);
        address.setLongitude(longitudeSelected);
        address.setTelephone(etTelephone.getText().toString());

        address.setSuBurb(etCol.getText().toString());
        address.setZone(etZone.getText().toString());
        address.setAccessCode(etAccessCode.getText().toString());
        address.setHouseNumber(etHouseNumber.getText().toString());

        Log.d(TAG, "editAddress: direccion a editar"+new Gson().toJson(address));
        addressService = RetrofitInstance.getRetrofitInstance().create(AddressService.class);
        call = addressService.updateAddress(address);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, final Response<AuthResponse> response) {
                Log.d(TAG, "onResponse: codigo actualizar "+response.code());
                Log.d(TAG, "onResponse: mensaje actualizar "+response.message());
                if (response.body() != null) {
                    new MaterialAlertDialogBuilder(context, R.style.MyDialogThemeMaterialLight)
                            .setTitle("Dirección")
                            .setMessage(response.body().getMsg())
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (response.body().isValid()) {
                                        NavHostFragment.findNavController(EditDirectionFragment.this).popBackStack();
                                    } else {
                                        dialogInterface.dismiss();
                                    }
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: al enviar direccion"+t.getMessage());
            }
        });
    }

    private String getAddressName() {
        String addressSelected = "Casa";
        switch (rgDirections.getCheckedRadioButtonId()) {
            case R.id.rbHouse:
                addressSelected = getResources().getString(R.string.house);
                break;
            case R.id.rbWork:
                addressSelected = getResources().getString(R.string.work);
                break;
            case R.id.rbOther:
                addressSelected = etOther.getText().toString();
                break;
        }
        return addressSelected;
    }

    private boolean validateFields() {
        boolean isNotError = true;
        if ((etDirection.getText() == null
                || etDirection.getText().toString().isEmpty()
                || etDirection.getText().toString().length() == 0)) {
            tlDirection.setError("Favor ingresa una dirección válida");
            isNotError = false;
        } else {
            tlDirection.setError("");
        }

        if ((etTelephone.getText() == null
                || etTelephone.getText().toString().isEmpty()
                || etTelephone.getText().toString().length() == 0)) {
            tlTelephone.setError("Favor ingresa una dirección válida");
            isNotError = false;
        } else {
            tlTelephone.setError("");
        }

        if ((rgDirections.getCheckedRadioButtonId() == R.id.rbOther)
                && (etOther.getText() == null
                || etOther.getText().toString().isEmpty()
        )) {
            tlOther.setError("Por favor escribe un nombre de la dirección");
            isNotError = false;
        } else {
            tlOther.setError("");
        }

        if ((etCol.getText() == null
                || etCol.getText().toString().isEmpty()
                || etCol.getText().toString().length() == 0)) {
            tlCol.setError("Favor ingresa una colonia");
            isNotError = false;
        } else {
            tlCol.setError("");
        }
        if ((etZone.getText() == null
                || etZone.getText().toString().isEmpty()
                || etZone.getText().toString().length() == 0)) {
            tlZone.setError("Favor ingresa una zona");
            isNotError = false;
        } else {
            tlZone.setError("");
        }

        if ((etHouseNumber.getText() == null
                || etHouseNumber.getText().toString().isEmpty()
                || etHouseNumber.getText().toString().length() == 0)) {
            tlHouseNumber.setError("Favor ingresa un No de Casa/Appto");
            isNotError = false;
        } else {
            tlHouseNumber.setError("");
        }

        if ((etIndication.getText() == null
                || etIndication.getText().toString().isEmpty()
                || etIndication.getText().toString().length() == 0)) {
            tlIndication.setError("Favor ingresa un No de Casa/Appto");
            isNotError = false;
        } else {
            tlIndication.setError("");
        }

        if (!ubicationSelected()) {
            new MaterialAlertDialogBuilder(context, R.style.MyDialogThemeMaterialLight)
                    .setTitle("Selecciona dirección")
                    .setMessage("Debes seleccionar una ubicación")
                    .setNegativeButton("Aceptar", null)
                    .show();
            isNotError = false;
        }
        return isNotError;
    }

    private void showInfoAlert() {
        new MaterialAlertDialogBuilder(context, R.style.MyDialogThemeMaterialLight)
                .setTitle("Señal GPS")
                .setMessage("No tienes encendido el GPS. Quieres encenderlo ahora?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        double lat = 13.68313;
        double lgt = -89.23659;
        if (currentAddress != null) {
            lat = currentAddress.getLatitude();
            lgt = currentAddress.getLongitude();
        }
        float zoom = 15;
        LatLng firtLocation = new LatLng(lat, lgt);
        gMap = googleMap;
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firtLocation, zoom));
        setLocationMarker(firtLocation);
        initSearch();
        setMapClick(gMap);
        setPoiClick(gMap);
        if (!this.isGPSEnabled()) {
            showInfoAlert();
        }
    }

    private void setMapClick(final GoogleMap map) {

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (autocompleteFragment != null) {
                    autocompleteFragment.setText("");
                }
                setLocationMarker(latLng);
            }
        });
    }

    private void setPoiClick(final GoogleMap map) {
        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(PointOfInterest poi) {
                if (autocompleteFragment != null) {
                    autocompleteFragment.setText("");
                }
                setLocationMarker(poi.latLng);
            }
        });
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        //Toast.makeText(getActivity(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
        try {
            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            setLocationMarker(myLocation);
        } catch (Exception e) {
            Log.d(TAG, "onMyLocationClick: e " + e.getMessage());
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        if (!this.isGPSEnabled()) {
            showInfoAlert();
        }
        Location loc = gMap.getMyLocation();
        try {
            LatLng myLocation = new LatLng(loc.getLatitude(), loc.getLongitude());
            setLocationMarker(myLocation);
        } catch (Exception e) {
            Log.d(TAG, "onMyLocationButtonClick: e " + e.getMessage());
        }
        return false;
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else if (gMap != null) {
            // Access to the location has been granted to the app.
            gMap.setMyLocationEnabled(true);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                    break;
                } else {

                }
        }
    }

    @SuppressLint("MissingPermission")
    private void initSearch() {
        gMap.setOnMyLocationButtonClickListener(this);
        gMap.setOnMyLocationClickListener(this);
        enableMyLocation();
        /*mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.d(TAG, "onSuccess: "+location.getLatitude() + " long "+location.getLongitude());
                        }
                    }
                });*/

        Places.initialize(context, api_key);
        PlacesClient placesClient = Places.createClient(context);
        autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_search);

        if (autocompleteFragment != null) {
            autocompleteFragment.setCountry("gt");
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    Log.d(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getId());
                    setLocationMarker(place.getLatLng());
                }

                @Override
                public void onError(Status status) {
                    Log.d(TAG, "An error occurred: " + status);
                }
            });
        } else {
            Log.d(TAG, "An error occurred: es nulo");
        }
    }

    @SuppressLint("MissingPermission")
    public void setLocationMarker(LatLng latLng) {
        latitudeSelected = latLng.latitude;
        longitudeSelected = latLng.longitude;
        float zoom = gMap.getCameraPosition().zoom;
        String snipset = "lat :" + latLng.latitude + " lng: " + latLng.longitude;
        if (markerUser == null) {
            markerUser = gMap.addMarker(new MarkerOptions()
                    .position(latLng)
            );
        } else {
            markerUser.setPosition(latLng);
        }
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        if (markerUser != null) {
            markerUser.setSnippet(snipset);
            markerUser.setTitle("Ubicacion seleccionada");
            Log.d("Hola", "getMyCurrentLocation: latitude " + markerUser.getPosition().latitude);
            Log.d("Hola", "getMyCurrentLocation: longitude " + markerUser.getPosition().latitude);
            markerUser.setDraggable(true);
            markerUser.setIcon(bitmapDescriptorFromVector(context));
            markerUser.showInfoWindow();
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_location);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_location);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private boolean isGPSEnabled() {
        try {
            int gpsSignal = Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.LOCATION_MODE);
            if (gpsSignal == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean ubicationSelected() {
        return markerUser != null;
    }

}

package net.dsdev.lailai.clientes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.dsdev.lailai.clientes.activity.LoginActivity;
import net.dsdev.lailai.clientes.model.menuDetail.MenuDetailList;
import net.dsdev.lailai.clientes.model.users.Client;
import net.dsdev.lailai.clientes.util.AddOrRemoveCallbacks;
import net.dsdev.lailai.clientes.util.Converter;
import net.dsdev.lailai.clientes.util.RandomMethods;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;


public class MainActivity extends AppCompatActivity implements AddOrRemoveCallbacks,
        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {
    public static final String GOOGLE_ACCOUNT = "google_account";
    private static final String TAG = "Hola";
    private Toolbar my_toolbar;
    private DrawerLayout drawerLayout;
    private NavController navController;
    private NavigationView navigationView;
    private TextView txtWellcome;
    private
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String json;
    Gson gson;
    private static int cart_count=0;
    SharedPreferencesMethods sharedPreferencesMethods;
    Button btnDrawerLogOut;
    ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gson = new Gson();
        prefs = getSharedPreferences("globalPreferences", Context.MODE_PRIVATE);
        my_toolbar = findViewById(R.id.my_toolbar);
        my_toolbar.setTitle("");
        btnDrawerLogOut = findViewById(R.id.btnDrawerLogOut);
        sharedPreferencesMethods = new SharedPreferencesMethods(this);
        setSupportActionBar(my_toolbar);
        setUpNavigation();
        setCartCount();
        btnDrawerLogOut.setOnClickListener(this);
        facebookSetup();
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            Boolean goToCart = extras.getBoolean("goToCart",false);
            if (goToCart){
                if (cart_count > 0) {
                    navController.navigate(R.id.directionsFragment);
                }
            }
        }
        //logData();
    }

    private void facebookSetup() {
        AccessTokenTracker fbTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
                if (accessToken2 == null) {
                    Log.d(TAG, "onCurrentAccessTokenChanged: logged out");
                    logout();
                }
            }
        };
        fbTracker.startTracking();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem menuItem = menu.findItem(R.id.cart);
        menuItem.setIcon(Converter.convertLayoutToImage(MainActivity.this, cart_count, R.drawable.ic_shopping_cart));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: click "+item.getItemId());
        int id = item.getItemId();
        switch(id){
            case R.id.user:
                if (getIsLogged()){
                    navController.navigate(R.id.profileFragment);
                    NavDestination s = navController.getCurrentDestination();
                    Log.d(TAG, "onOptionsItemSelected: test");
                } else {
                    RandomMethods.showLoginNeededAlert(this);
                }
                break;
            case R.id.logout:
                logout();
                break;
            case R.id.cart:
                if (cart_count > 0) {
                    navController.navigate(R.id.orderDetailFragment);
                }
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        /*prefs = getSharedPreferences("globalPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("isLoggedIn",false);
        edit.apply();*/
        Client client = sharedPreferencesMethods.getLoggedUser();

        if (client==null){
            Log.d(TAG, "logout: client null");
            sharedPreferencesMethods.deleteMenusTree();
            sharedPreferencesMethods.setIsLogged(false);
            RandomMethods.navigate(LoginActivity.class, this);
            //return;
        } else {
            Log.d(TAG, "logout: client no null");
            if (client.getAccountType().equals("GOOGLE")){
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        sharedPreferencesMethods.deleteLoggedUser();
                        sharedPreferencesMethods.setIsLogged(false);
                        sharedPreferencesMethods.deleteMenusTree();
                        startActivity(intent);
                    }
                });
            }else if(client.getAccountType().equals("FACEBOOK")){
                disconnectFromFacebook();
            }else {
                sharedPreferencesMethods.deleteLoggedUser();
                sharedPreferencesMethods.setIsLogged(false);
                sharedPreferencesMethods.deleteMenusTree();
            /*Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);*/
                RandomMethods.navigate(LoginActivity.class, this);
            }

        }



    }
    public void disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            sharedPreferencesMethods.deleteLoggedUser();
            sharedPreferencesMethods.setIsLogged(false);
            sharedPreferencesMethods.deleteMenusTree();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                    .Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {
                    LoginManager.getInstance().logOut();
                    sharedPreferencesMethods.deleteLoggedUser();
                    sharedPreferencesMethods.setIsLogged(false);
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }).executeAsync();
        }
    }
    @Override
    public void onAddProduct() {
        setCartCount();
        invalidateOptionsMenu();
        /*Snackbar.make((CoordinatorLayout)findViewById(R.id.parent), "Added to cart !!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();*/
    }

    @Override
    public void onRemoveProduct() {
        setCartCount();
        invalidateOptionsMenu();
        /*Snackbar.make((CoordinatorLayout)findViewById(R.id.parent), "Removed from cart !!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();*/
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.fragment), drawerLayout);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    void setCartCount(){
        json = prefs.getString("shoppingCart", "");
        if (!json.equalsIgnoreCase("")){
            MenuDetailList menuDetailList = new MenuDetailList();
            menuDetailList = gson.fromJson(json, MenuDetailList.class);
            if (menuDetailList!=null && menuDetailList.getMenus()!=null && menuDetailList.getMenus().size()>0){
                cart_count = menuDetailList.getMenus().size();
            }else{
                cart_count=0;
            }
        }else{
            cart_count=0;
        }
    }

    public SharedPreferencesMethods getSharedPreferencesMethods() {
        return sharedPreferencesMethods;
    }

    private void setUpNavigation(){
        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, my_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d(TAG, "onDrawerOpened: ");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d(TAG, "onDrawerClosed: ");
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                Log.d(TAG, "onDrawerSlide: ");
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                Log.d(TAG, "drawer onOptionsItemSelected: ");
                return super.onOptionsItemSelected(item);
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true);
        navigationView = findViewById(R.id.navigationView);
        navController = Navigation.findNavController(this, R.id.fragment);
        navigationView.setNavigationItemSelectedListener(this);

        /*--------------------------------------------
             Para agregar primer nombre en cuadro
            amarillo de imagen en menu hamburguesa
        --------------------------------------------*/
        String primerNombre = "";
        String[] cadena = sharedPreferencesMethods.getLoggedUser().getNames().split(" ");
        primerNombre = cadena[0];
        txtWellcome = navigationView.getHeaderView(0).findViewById(R.id.txtWellcome);
        txtWellcome.setText("Bienvenido/a ".concat(primerNombre));

        //MenuItem menuItem = navigationView.getMenu().getItem(0);
        //onNavigationItemSelected(menuItem);
        //menuItem.setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawerHome:
                navController.navigate(R.id.homeFragment);
                break;
            case R.id.drawerProfile:
                if (getIsLogged()){
                    navController.navigate(R.id.profileFragment);
                }else{
                    RandomMethods.showLoginNeededAlert(MainActivity.this);
                }
                break;
            case R.id.drawerOrder:
                if (getIsLogged()){
                    navController.navigate(R.id.activeOrdersFragment);
                }else{
                    RandomMethods.showLoginNeededAlert(MainActivity.this);
                }
                break;
            /*case R.id.drawerOrderHistory:
                if (getIsLogged()){
                    navController.navigate(R.id.orderHistoryFragment);
                }else{
                    RandomMethods.showLoginNeededAlert(MainActivity.this);
                }
                break;*/
            default:
                //Toast.makeText(this, "click default", Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
        item.setChecked(true);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean getIsLogged() {
        return sharedPreferencesMethods.getIsLogged() && sharedPreferencesMethods.getLoggedUser() !=null;
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: v "+v.getId());
        switch (v.getId()){
            case R.id.btnDrawerLogOut:
                logout();
                break;
            default:
                break;
        }
    }

    private void logData(){
        GoogleSignInAccount googleSignInAccount = getIntent().getParcelableExtra(GOOGLE_ACCOUNT);
        Log.d(TAG, "logData: name "+googleSignInAccount.getDisplayName());
        Log.d(TAG, "logData: email "+googleSignInAccount.getEmail());
        Log.d(TAG, "logData: idToken "+googleSignInAccount.getIdToken());
        Log.d(TAG, "logData: id "+googleSignInAccount.getId());
    }
}

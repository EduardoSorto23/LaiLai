package net.dsdev.lailai.clientes.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.dsdev.lailai.clientes.model.users.AuthResponse;
import net.dsdev.lailai.clientes.model.users.Client;
import net.dsdev.lailai.clientes.model.users.LoginApiRequest;
import net.dsdev.lailai.clientes.model.users.LoginLocalRequest;
import net.dsdev.lailai.clientes.retrofit.RetrofitInstance;
import net.dsdev.lailai.clientes.retrofit.users.UsersService;
import net.dsdev.lailai.clientes.util.Constants;
import net.dsdev.lailai.clientes.util.RandomMethods;
import net.dsdev.lailai.clientes.MainActivity;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "Hola";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Button btnRecover;
    UsersService usersService;
    TextInputEditText tietEmail, tietPassword;
    TextInputLayout tilEmail, tilPassword;
    SharedPreferencesMethods sharedPreferencesMethods;
    CardView cvGoogleLogin, cvFbLogin;

    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions googleSignInOptions;

    // Facebook
    CallbackManager callbackManager;
    LoginButton loginButton;
    public TextView fb;
    Boolean goToCart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        sharedPreferencesMethods = new SharedPreferencesMethods(this);
        prefs = getSharedPreferences("globalPreferences", Context.MODE_PRIVATE);
        bindUI();
        cvGoogleLogin.setOnClickListener(this);
        fb.setOnClickListener(this);
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        facebookLoginSetup();
        RandomMethods.setTextWatcher(tietEmail,tilEmail);
        RandomMethods.setTextWatcher(tietPassword,tilPassword);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "net.dsdev.lailai.clientes",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", "KeyHash:"+ Base64.encodeToString(md.digest(),
                        Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            Log.d("Hola", "onCreate: "+e.toString());

        }
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            goToCart = extras.getBoolean("goToCart");
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode "+requestCode);
        Log.d(TAG, "onActivityResult: resultCode "+resultCode);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            Log.d(TAG, "onActivityResult: if request");
            switch (requestCode){
                case 101:
                    Log.d(TAG, "onActivityResult: 101 case");
                    try {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        googleLoggedIn(account);
                    } catch (ApiException e){
                        Log.d(TAG, "onActivityResult: "+e.getMessage());
                    }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == fb) {
            loginButton.performClick();
        }
        switch (v.getId()){
            case R.id.btnSignUp :
                RandomMethods.navigate(RegisterActivity.class, LoginActivity.this,goToCart);
                break;
            case R.id.cvLogin:
                usersService = RetrofitInstance.getRetrofitInstance().create(UsersService.class);
                if (tietEmail.getText().toString().isEmpty()){
                    tilEmail.setError("Campo obligatorio");
                    break;
                }
                if (tietPassword.getText().toString().isEmpty()){
                    tilPassword.setError("Campo obligatorio");
                    break;
                }
                LoginLocalRequest loginLocalRequest = new LoginLocalRequest(tietEmail.getText().toString(), tietPassword.getText().toString(), "LOCAL");
                Log.d(TAG, "onClick: USER: "+ tietEmail.getText());
                Log.d(TAG, "onClick: USER: "+ tietPassword.getText());
                Call<AuthResponse> call = usersService.sendLoginLocalRequest(loginLocalRequest);
                call.enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.body()!=null){
                            Log.d(TAG, "onResponse: VALID: "+response.body().isValid());
                            if (response.body().isValid()){
                                editor = prefs.edit();
                                editor.putBoolean("isLoggedIn",true);
                                editor.apply();
                                Client client = response.body().getClient();
                                client.setAccountType("LOCAL");
                                sharedPreferencesMethods.saveLoggedUser(client);
                                //Toast.makeText(LoginActivity.this, "Acceso concedido", Toast.LENGTH_SHORT).show();
                                RandomMethods.navigate(MainActivity.class, LoginActivity.this, goToCart);
                            } else {
                                Log.d(TAG, "onResponse: denegado");
                                Toast.makeText(LoginActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "onResponse: body null");
                            Toast.makeText(LoginActivity.this, "Esta operación no pudo ser realizada. Favor intente más tarde", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });

                break;
            case R.id.btnRecover:
                goToResetPassword();
                break;
            case R.id.btnGuess:
                sharedPreferencesMethods.setIsLogged(true);
                RandomMethods.navigate(MainActivity.class, LoginActivity.this);
                break;
            case R.id.cvGoogleLogin:
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
                break;
            default:
                break;
        }

    }

    private void bindUI(){
        tietEmail = findViewById(R.id.txtEmail);
        tietPassword = findViewById(R.id.txtPassword);
        tilEmail = findViewById(R.id.tlEmail);
        tilPassword = findViewById(R.id.tlPassword);
        cvFbLogin = findViewById(R.id.cvFbLogin);
        cvGoogleLogin = findViewById(R.id.cvGoogleLogin);
        loginButton = findViewById(R.id.login_button);
        fb = findViewById(R.id.txtLoginFace);
    }

    private void facebookLoginSetup() {

        loginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_friends"));

        callbackManager = CallbackManager.Factory.create();

        AccessTokenTracker fbTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
                if (accessToken2 == null) {
                    Log.d(TAG, "onCurrentAccessTokenChanged: logged out");
                }
            }
        };
        fbTracker.startTracking();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess:logged");
                boolean loggedOut = AccessToken.getCurrentAccessToken() == null;
                Log.d(TAG, "loggedOut: "+loggedOut);
                if (!loggedOut) {
                    getUserProfile(AccessToken.getCurrentAccessToken());
                }
            }

            @Override
            public void onCancel() {
                Log.d("Hola", "Cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("Hola", "FacebookException exception" + exception.toString());
            }
        });

    }

    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                            Client client = new Client(first_name,last_name,email,"FACEBOOK");
                            facebookLoggedIn(client, id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void googleLoggedIn(GoogleSignInAccount googleSignInAccount){
        //sharedPreferencesMethods.saveLoggedUser(new Client(googleSignInAccount.getDisplayName(),"", googleSignInAccount.getEmail(), "GOOGLE"));
        sharedPreferencesMethods.setIsLogged(true);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(MainActivity.GOOGLE_ACCOUNT, googleSignInAccount);
        intent.putExtra("goToCart",goToCart);
        sendLoginAp(googleSignInAccount.getGivenName(), googleSignInAccount.getFamilyName(), googleSignInAccount.getEmail(), googleSignInAccount.getId(), intent, "GOOGLE");

    }

    private void facebookLoggedIn(Client client, String id){
        //sharedPreferencesMethods.saveLoggedUser(client);
        sharedPreferencesMethods.setIsLogged(true);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("goToCart",goToCart);
        //startActivity(intent);
        sendLoginAp(client.getNames(), client.getLastNames(), client.getEmail(), id, intent, "FACEBOOK");
    }

    private void sendLoginAp(String names, String lastNames, String userEmail, String token, final Intent intent, final String accountType){
        String account = "";
        if(accountType.equals("FACEBOOK")){
            account = Constants.accountFacebook;
        }else if(accountType.equals("GOOGLE")){
            account = Constants.accountGoogle;
        }
        usersService = RetrofitInstance.getRetrofitInstance().create(UsersService.class);
        LoginApiRequest loginApiRequest = new LoginApiRequest(names, lastNames, userEmail, token, account);
        Call<AuthResponse> call = usersService.sendLoginApiRequest(loginApiRequest);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                if (response.body()!=null){
                    if (response.body().isValid()){
                        Client client = response.body().getClient();
                        client.setAccountType(accountType);
                        sharedPreferencesMethods.saveLoggedUser(client);
                        Toast.makeText(LoginActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Esta operación no pudo ser realizada. Favor intente más tarde", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {

            }
        });
    }

    private void goToResetPassword() {
        Intent intent = new Intent(this,ResetPasswordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("goToCart",goToCart);
        startActivity(intent);
    }
}

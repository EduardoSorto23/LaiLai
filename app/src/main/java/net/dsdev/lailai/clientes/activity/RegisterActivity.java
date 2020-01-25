package net.dsdev.lailai.clientes.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.dsdev.lailai.clientes.MainActivity;
import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.model.users.AuthResponse;
import net.dsdev.lailai.clientes.model.users.Client;
import net.dsdev.lailai.clientes.model.users.LoginApiRequest;
import net.dsdev.lailai.clientes.model.users.NewUser;
import net.dsdev.lailai.clientes.retrofit.RetrofitInstance;
import net.dsdev.lailai.clientes.retrofit.users.UsersService;
import net.dsdev.lailai.clientes.util.RandomMethods;
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
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener  {

    private static final String TAG = "Hola";
    public TextView fb;
    TextInputEditText tietEmail, tietPassword, tietNames, tietLastNames;
    TextInputLayout tilEmail, tilPassword, tilNames, tilLastNames;
    CardView cvEmail, cvGoogle;
    UsersService usersService;
    SharedPreferencesMethods sharedPreferencesMethods;

    public String id, name, email, gender, birthday;

    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions googleSignInOptions;
    //FACEBOOK
    CallbackManager callbackManager;
    LoginButton loginButton;

    @Override
    protected void onStart() {
        super.onStart();
        /*GoogleSignInAccount alreadyLoggedAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (alreadyLoggedAccount!=null){
            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
            googleLoggedIn(alreadyLoggedAccount);
        } else {
            Log.d(TAG, "onStart: Not Logged In");
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        bindUI();
        sharedPreferencesMethods = new SharedPreferencesMethods(this);
        setTextWatchers();
        fb.setOnClickListener(this);
        cvEmail.setOnClickListener(this);
        cvGoogle.setOnClickListener(this);
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        facebookLoginSetup();
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
        Intent intent;
        Class clas = null;
        switch (v.getId()){
            case R.id.lblSignIn :
                clas = LoginActivity.class;
                break;
            case R.id.cvEmail:
                Log.d(TAG, "onClick: CREAR");
                if (!areFieldsValid()){
                    break;
                }

                usersService = RetrofitInstance.getRetrofitInstance().create(UsersService.class);
                NewUser newUser = new NewUser(tietNames.getText().toString(), tietLastNames.getText().toString(), tietEmail.getText().toString(), tietEmail.getText().toString(),  tietPassword.getText().toString(),  "LOCAL", "");
                Log.d(TAG, "onClick: USER:"+new Gson().toJson(newUser));
                Call<AuthResponse> call = usersService.createNewUser(newUser);
                call.enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.body()!=null){
                            if (response.body().isValid()){
                                Toast.makeText(RegisterActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                                sharedPreferencesMethods.saveLoggedUser(response.body().getClient());
                                RandomMethods.navigate(LoginActivity.class, RegisterActivity.this);
                            } else {
                                Toast.makeText(RegisterActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Esta operación no pudo ser realizada. Favor intente más tarde", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });

                break;
            case R.id.cvGoogle:
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
                break;
            default:
                clas = null;
                    break;
        }
        if (clas != null){
            intent = new Intent(this,clas);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,LoginActivity.class );
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void bindUI(){
        fb = (TextView) findViewById(R.id.txtLoginFace);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        tietEmail = findViewById(R.id.txtEmail);
        tietLastNames = findViewById(R.id.txtLastNames);
        tietNames = findViewById(R.id.txtNames);
        tietPassword = findViewById(R.id.txtPassword);
        tilEmail = findViewById(R.id.tlEmail);
        tilLastNames = findViewById(R.id.tlLastNames);
        tilNames = findViewById(R.id.tlNames);
        tilPassword = findViewById(R.id.tlPassword);
        cvEmail = findViewById(R.id.cvEmail);
        cvGoogle = findViewById(R.id.cvGoogle);
    }

    private void googleLoggedIn(GoogleSignInAccount googleSignInAccount){
        //sharedPreferencesMethods.saveLoggedUser(new Client(googleSignInAccount.getDisplayName(),"", googleSignInAccount.getEmail(), "GOOGLE"));
        sharedPreferencesMethods.setIsLogged(true);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(MainActivity.GOOGLE_ACCOUNT, googleSignInAccount);
        sendLoginAp(googleSignInAccount.getGivenName(), googleSignInAccount.getFamilyName(), googleSignInAccount.getEmail(), googleSignInAccount.getId(), intent, "GOOGLE");

    }

    private void facebookLoggedIn(Client client, String id){
        //sharedPreferencesMethods.saveLoggedUser(client);
        sharedPreferencesMethods.setIsLogged(true);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        sendLoginAp(client.getNames(), client.getLastNames(), client.getEmail(), id, intent, "FACEBOOK");
    }

    private void setTextWatchers(){
        RandomMethods.setTextWatcher(tietEmail, tilEmail);
        RandomMethods.setTextWatcher(tietLastNames, tilLastNames);
        RandomMethods.setTextWatcher(tietNames, tilNames);
        RandomMethods.setTextWatcher(tietPassword, tilPassword);
    }

    private boolean areFieldsValid(){

        if (tietNames.getText().toString().isEmpty()){
            tilNames.setError("Campo obligatorio");
            return false;
        }
        if (tietLastNames.getText().toString().isEmpty()){
            tilLastNames.setError("Campo obligatorio");
            return false;
        }
        if (tietEmail.getText().toString().isEmpty()){
            tilEmail.setError("Campo obligatorio");
            return false;
        }
        if (!RandomMethods.emailVerified(tietEmail.getText().toString())){
            tilEmail.setError("Email no válido");
            return false;
        }
        if (tietPassword.getText().toString().isEmpty()){
            tilPassword.setError("Campo obligatorio");
            return false;
        }

        return true;
    }

    private void sendLoginAp(String names, String lastNames, String userEmail, String token, final Intent intent, final String accountType){
        usersService = RetrofitInstance.getRetrofitInstance().create(UsersService.class);
        LoginApiRequest loginApiRequest = new LoginApiRequest(names, lastNames, userEmail, token);
        Call<AuthResponse> call = usersService.sendLoginApiRequest(loginApiRequest);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                if (response.body()!=null){
                    if (response.body().isValid()){
                        Client client = response.body().getClient();
                        client.setAccountType(accountType);
                        sharedPreferencesMethods.saveLoggedUser(client);
                        Toast.makeText(RegisterActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Esta operación no pudo ser realizada. Favor intente más tarde", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {

            }
        });
    }

}

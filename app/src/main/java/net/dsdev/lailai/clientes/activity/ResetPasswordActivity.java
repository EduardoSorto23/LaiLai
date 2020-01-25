package net.dsdev.lailai.clientes.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.retrofit.RetrofitInstance;
import net.dsdev.lailai.clientes.util.RandomMethods;
import net.dsdev.lailai.clientes.model.users.Accounts;
import net.dsdev.lailai.clientes.retrofit.users.UsersService;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText etEmail;
    TextInputLayout tlEmail;
    UsersService usersService;
    private String TAG = "Hola";
    private Accounts accounts;
    ProgressBar pbReset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        bindUI();
    }

    private void bindUI() {
        etEmail = findViewById(R.id.etEmail);
        tlEmail = findViewById(R.id.tlEmail);
        pbReset = findViewById(R.id.pbReset);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cvResetPassword:
                tlEmail.setError("");
                resetPassword();
                break;
            default:
                break;
        }
    }

    private void resetPassword() {

        if (etEmail.getText() == null || etEmail.getText().toString().equals("")){
            tlEmail.setError("El correo no puede ser vacío");
            return;
        }
        String email = etEmail.getText().toString();

        if (!RandomMethods.emailVerified(email)){
            tlEmail.setError("Email no válido");
            return;
        }
        hideKeyboard(this);
        pbReset.setVisibility(View.VISIBLE);
        usersService = RetrofitInstance.getRetrofitInstance().create(UsersService.class);
        Call<Accounts> call = usersService.resetPassword(new Accounts(email));
        call.enqueue(new Callback<Accounts>() {
            @Override
            public void onResponse(Call<Accounts> call, Response<Accounts> response) {
                if (response.code() == 404 || response.code() == 500){
                    Log.d(TAG, "onResponse: code "+response.code() +" " + response.message());
                    pbReset.setVisibility(View.INVISIBLE);
                    return;
                }
                if(response.body() != null){
                    accounts = response.body();
                    MaterialAlertDialogBuilder materialAlertDialogBuilder;
                    materialAlertDialogBuilder = new MaterialAlertDialogBuilder(ResetPasswordActivity.this, R.style.MyDialogTheme);
                    materialAlertDialogBuilder.setCancelable(false);
                    String tittle;
                    if (accounts.isValid()) {
                        //tittle = "Un correo ha sido enviado para recuperar contraseña";
                        tittle = accounts.getMsg();
                    }else{
                        //tittle = "No se ha podido encontrar el correo electrónico";
                        tittle = accounts.getMsg();
                    }
                    materialAlertDialogBuilder.setTitle(tittle);
                    materialAlertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "onClick: iscomplete "+accounts.isValid());
                            if (accounts.isValid()){
                                RandomMethods.navigate(LoginActivity.class,ResetPasswordActivity.this);
                            }
                            dialog.dismiss();
                        }
                    });
                    materialAlertDialogBuilder.show();
                }else{
                    Log.d(TAG, "onResponse: response.body() = null "+ response.message());
                }
                pbReset.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<Accounts> call, Throwable t) {
                Log.d(TAG, "onFailure: Throwable "+t.getMessage());
                pbReset.setVisibility(View.INVISIBLE);
            }
        });
    }

    /*private boolean emailVerified(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }*/
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        RandomMethods.navigate(LoginActivity.class,ResetPasswordActivity.this);
        super.onBackPressed();
    }
}

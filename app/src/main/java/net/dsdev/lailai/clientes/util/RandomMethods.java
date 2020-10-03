package net.dsdev.lailai.clientes.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import net.dsdev.lailai.clientes.R;
import net.dsdev.lailai.clientes.activity.LoginActivity;
import net.dsdev.lailai.clientes.fragments.OrderDetailFragment;

import java.util.regex.Pattern;

public class RandomMethods {

    public static void setTextWatcher(final TextInputEditText textInputEditText, final TextInputLayout textInputLayout){
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputLayout.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static void navigate(Class clas, Context context){
        Intent intent;
        intent = new Intent(context,clas);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
    public static void navigate(Class clas, Context context, Boolean goToCart){
        Intent intent;
        intent = new Intent(context,clas);
        intent.putExtra("goToCart",goToCart);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static boolean emailVerified(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public static void showLoginNeededAlert(final Activity activity){
        new MaterialAlertDialogBuilder(activity, R.style.MyDialogThemeMaterial)
                .setTitle("Importante")
                .setMessage("No has iniciado sesión. Para poder acceder a esta opción es necesario iniciar sesión.")
                .setPositiveButton("Iniciar sesión", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(activity, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
    public static void showLoginNeededAlert(final Activity activity, final Boolean goToCart){
        new MaterialAlertDialogBuilder(activity, R.style.MyDialogThemeMaterial)
                .setTitle("Importante")
                .setMessage("No has iniciado sesión. Para poder acceder a esta opción es necesario iniciar sesión.")
                .setPositiveButton("Iniciar sesión", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(activity, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (goToCart){
                            intent.putExtra("goToCart",true);
                        }
                        activity.startActivity(intent);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

}

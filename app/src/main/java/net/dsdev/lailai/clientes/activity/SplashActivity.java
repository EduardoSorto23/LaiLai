package net.dsdev.lailai.clientes.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import net.dsdev.lailai.clientes.MainActivity;
import net.dsdev.lailai.clientes.util.SharedPreferencesMethods;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferencesMethods sharedPreferencesMethods = new SharedPreferencesMethods(this);
        Intent intent;
        if (sharedPreferencesMethods.getIsLogged()){
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
}

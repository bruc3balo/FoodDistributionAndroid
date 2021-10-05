package com.victoria.fooddistribution.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.victoria.fooddistribution.R;
import com.victoria.fooddistribution.databinding.ActivitySplashScreenBinding;
import com.victoria.fooddistribution.globals.GlobalRepository;
import com.victoria.fooddistribution.pages.login.LoginActivity;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    private ActivitySplashScreenBinding splashScreenBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashScreenBinding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(splashScreenBinding.getRoot());

        showPb();

        GlobalRepository.init(getApplication());

        new Handler().postDelayed(this::goToNextPage, 1000);

    }

    private void goToNextPage() {
        goToLoginScreen();
    }

    private void goToLoginScreen() {
        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
    }

    private void showPb() {
        splashScreenBinding.splashScreenPb.setVisibility(View.VISIBLE);
    }

    private void hidePb() {
        splashScreenBinding.splashScreenPb.setVisibility(View.GONE);
    }
}
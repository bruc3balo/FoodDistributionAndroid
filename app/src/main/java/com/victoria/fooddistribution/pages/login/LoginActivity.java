package com.victoria.fooddistribution.pages.login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.victoria.fooddistribution.adapter.LoginPagerAdapter;
import com.victoria.fooddistribution.databinding.ActivityLoginBinding;
import com.victoria.fooddistribution.pagerTransformers.DepthPageTransformer;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpLoginPager();

    }

    private void setUpLoginPager() {
        LoginPagerAdapter loginPagerAdapter = new LoginPagerAdapter(getSupportFragmentManager(), getLifecycle());
        binding.loginViewPager.setAdapter(loginPagerAdapter);
        binding.loginViewPager.setPageTransformer(new DepthPageTransformer());
        binding.loginTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.setText(loginPagerAdapter.getLoginTitles(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        new TabLayoutMediator(binding.loginTabLayout, binding.loginViewPager, true, true,(tab, position) -> {

        }).attach();
        loginPagerAdapter.setAllTabIcons(binding.loginTabLayout);
    }

}
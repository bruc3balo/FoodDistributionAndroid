package com.victoria.fooddistribution.pages.certifiedAuth;

import static com.victoria.fooddistribution.globals.GlobalMethods.logout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.victoria.fooddistribution.R;
import com.victoria.fooddistribution.databinding.ActivityCertifiedAuthorityBinding;
import com.victoria.fooddistribution.pages.beneficiary.BeneficiaryActivity;

public class CertifiedAuthorityActivity extends AppCompatActivity {

    ActivityCertifiedAuthorityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCertifiedAuthorityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Logout").setIcon(R.drawable.logout).setOnMenuItemClickListener(menuItem -> {
            logout(CertifiedAuthorityActivity.this);
            return false;
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }
}
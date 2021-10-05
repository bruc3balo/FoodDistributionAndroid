package com.victoria.fooddistribution.pages.welcome;

import static com.victoria.fooddistribution.globals.GlobalRepository.userRepository;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;


import com.victoria.fooddistribution.R;
import com.victoria.fooddistribution.adapter.TutorialVpAdapter;
import com.victoria.fooddistribution.databinding.ActivityTutorialBinding;
import com.victoria.fooddistribution.models.Models;
import com.victoria.fooddistribution.pages.admin.AdminActivity;
import com.victoria.fooddistribution.pages.beneficiary.BeneficiaryActivity;
import com.victoria.fooddistribution.pages.certifiedAuth.CertifiedAuthorityActivity;
import com.victoria.fooddistribution.pages.seller.SellerActivity;
import com.victoria.fooddistribution.pages.transporter.TransporterActivity;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class WelcomeActivity extends AppCompatActivity {

    private ActivityTutorialBinding tutorialBinding;
    private final ArrayList<Models.TutorialModel> tutorialList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tutorialBinding = ActivityTutorialBinding.inflate(getLayoutInflater());
        setContentView(tutorialBinding.getRoot());

        populateTutorials();


        ViewPager2 tutorialViewPager = tutorialBinding.tutorialViewPager;
        TutorialVpAdapter tutorialVpAdapter = new TutorialVpAdapter(this, tutorialList);
        tutorialViewPager.setAdapter(tutorialVpAdapter);

        CircleIndicator3 indicator = tutorialBinding.tutorialIndicator;
        indicator.setViewPager(tutorialViewPager);

        // optionalA
        tutorialVpAdapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());

        Button next = tutorialBinding.nextButton;
        next.setOnClickListener(view -> {
            if (tutorialViewPager.getCurrentItem() != tutorialList.size() - 1) {
                tutorialViewPager.setCurrentItem(tutorialViewPager.getCurrentItem() + 1);
            } else {
                goToNextPage(userRepository.getUser().getRole());
            }
        });

        setWindowColors();

    }

    private void populateTutorials() {
        Models.TutorialModel tutorialModel = new Models.TutorialModel("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.clker.com%2Fcliparts%2F3%2Fm%2F1%2FO%2F7%2Fu%2Fsearch-icon-red-hi.png&f=1&nofb=1", "Look for the job you want ", "Search");
        Models.TutorialModel tutorialModel1 = new Models.TutorialModel("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fthumbs.dreamstime.com%2Fb%2Fattractive-man-sleeping-home-couch-mobile-phone-digital-tablet-pad-his-hands-young-shirt-jeans-internet-61244350.jpg&f=1&nofb=1", "At the comfort of your couch", "Convinience");
        tutorialList.add(tutorialModel);
        tutorialList.add(tutorialModel1);
    }

    private void setWindowColors() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.white));
            getWindow().setNavigationBarColor(getColor(R.color.white));
        } else {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        }

    }

    private void goToNextPage(String role) {
        switch (role) {
            case "ROLE_ADMIN": case "ROLE_ADMIN_TRAINEE":
                goToAdminPage();
                break;

            case "ROLE_TRANSPORTER":
                goToTransporterProviderPage();
                break;

            case "ROLE_CERTIFIED_AUTHORITY":
                goToCertifiedAuthorityPage();
                break;

          /*  case "ROLE_DISTRIBUTOR":
                goToDistributorPage();
                break;*/

            default: case "ROLE_BUYER":
                goToBuyerPage();
                break;

            case "ROLE_SELLER":
                goToSellerPage();
                break;
        }
    }

    private void goToAdminPage() {
        startActivity(new Intent(this, AdminActivity.class));
        finish();
    }


    private void goToBuyerPage() {
        startActivity(new Intent(this, BeneficiaryActivity.class));
        finish();
    }

    private void goToSellerPage() {
        startActivity(new Intent(this, SellerActivity.class));
        finish();
    }

 /*   private void goToDistributorPage() {
        startActivity(new Intent(this, .class));
        finish();
    }*/

    private void goToTransporterProviderPage() {
        startActivity(new Intent(this, TransporterActivity.class));
        finish();
    }

    private void goToCertifiedAuthorityPage() {
        startActivity(new Intent(this, CertifiedAuthorityActivity.class));
        finish();
    }
}
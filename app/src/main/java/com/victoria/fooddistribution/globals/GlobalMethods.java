package com.victoria.fooddistribution.globals;

import android.app.Activity;
import android.content.Intent;

import com.victoria.fooddistribution.pages.login.LoginActivity;

public class GlobalMethods {


    public static void logout(Activity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        activity.finishAffinity();

    }
}

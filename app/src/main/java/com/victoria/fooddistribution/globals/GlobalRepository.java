package com.victoria.fooddistribution.globals;

import android.app.Application;

import com.victoria.fooddistribution.globals.userDb.UserRepository;

public class GlobalRepository extends Application {

    public static UserRepository userRepository;
    public static Application application;
    public static boolean initialized = false;

    public GlobalRepository() {

    }

    public static void init(Application application) {
        if (!initialized) {
            userRepository = new UserRepository(application);
            GlobalRepository.application = application;
        }
    }
}

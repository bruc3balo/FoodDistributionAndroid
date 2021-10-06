package com.victoria.fooddistribution.globals;

import android.annotation.SuppressLint;
import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.victoria.fooddistribution.globals.userDb.UserRepository;

public class GlobalRepository extends Application {

    public static UserRepository userRepository;

    @SuppressLint("StaticFieldLeak")
    public static FirebaseFirestore firebaseFirestore;

    public static Application application;
    public static boolean initialized = false;

    public GlobalRepository() {

    }

    public static void init(Application application) {
        if (!initialized) {
            userRepository = new UserRepository(application);
            GlobalRepository.application = application;
            FirebaseApp.initializeApp(application);
            firebaseFirestore = FirebaseFirestore.getInstance();

            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();
            firebaseFirestore.setFirestoreSettings(settings);


            initialized = true;
        }
    }
}

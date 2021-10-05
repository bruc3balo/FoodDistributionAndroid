package com.victoria.fooddistribution.globals.userDb;

import static com.victoria.fooddistribution.globals.GlobalVariables.USER_COLLECTION;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.victoria.fooddistribution.domain.Domain;
import com.victoria.fooddistribution.utils.AbstractFirestoreRepository;

public class UserViewModel extends AndroidViewModel {

    private final UserRepo fireStoreRepo;

    public UserViewModel(@NonNull Application application) {
        super(application);
        fireStoreRepo = new UserRepo();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private MutableLiveData<Domain.AppUser> getUserMutable(String uid) {
        MutableLiveData<Domain.AppUser> userMutableLiveData = new MutableLiveData<>();
        fireStoreRepo.get(uid).ifPresent(userMutableLiveData::setValue);
        return userMutableLiveData;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public LiveData<Domain.AppUser> getUser (String uid) {
        return getUserMutable(uid);
    }

    private static class UserRepo extends AbstractFirestoreRepository<Domain.AppUser> {

        protected UserRepo() {
            super(USER_COLLECTION);
        }


    }

}

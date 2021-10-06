package com.victoria.fooddistribution.globals.userDb;

import static com.victoria.fooddistribution.globals.GlobalVariables.ROLE_COLLECTION;
import static com.victoria.fooddistribution.globals.GlobalVariables.USER_COLLECTION;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.victoria.fooddistribution.domain.Domain;
import com.victoria.fooddistribution.models.Models;
import com.victoria.fooddistribution.utils.AbstractFirestoreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserViewModel extends AndroidViewModel {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepo = new UserRepo();
        roleRepo = new RoleRepo();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private MutableLiveData<Domain.AppUser> getUserMutable(String uid) {
        MutableLiveData<Domain.AppUser> userMutableLiveData = new MutableLiveData<>();
        userRepo.get(uid).ifPresent(userMutableLiveData::setValue);
        return userMutableLiveData;
    }

    public MutableLiveData<Optional<Domain.AppUser>> findByUsername(String username) {
        MutableLiveData<Optional<Domain.AppUser>> mutableLiveData = new MutableLiveData<>();

        List<Domain.AppUser> users = Objects.requireNonNull(getAllUsers().getValue()).stream().filter(p -> p.getUsername().equals(username)).collect(Collectors.toList());
        if (!users.isEmpty()) {
            mutableLiveData.setValue(Optional.of(users.get(0)));
        } else {
            mutableLiveData.setValue(Optional.empty());
        }

        return mutableLiveData;
    }

    public MutableLiveData<List<String>> getUsernamesDirect() {
        MutableLiveData<List<String>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(userRepo.retrieveAll().stream().map(Domain.AppUser::getUsername).filter(Objects::nonNull).collect(Collectors.toList()));
        return mutableLiveData;
    }

    public MutableLiveData<List<String>> getPhoneNumbersDirect() {
        MutableLiveData<List<String>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(userRepo.retrieveAll().stream().map(Domain.AppUser::getPhoneNumber).filter(Objects::nonNull).collect(Collectors.toList()));
        return mutableLiveData;
    }

    public MutableLiveData<List<Domain.AppUser>> getAllUsers() {
        System.out.println("Fetching all users");
        MutableLiveData<List<Domain.AppUser>> mutableLiveData = new MutableLiveData<>();

        List<Domain.AppUser> userList = new ArrayList<>(userRepo.retrieveAll());
        mutableLiveData.setValue(userList);
        return mutableLiveData;
    }

    public MutableLiveData<Optional<Models.AppRole>> findByRoleName(String roleName) {

        MutableLiveData<Optional<Models.AppRole>> mutableLiveData = new MutableLiveData<>();

        List<Models.AppRole> roles = Objects.requireNonNull(getAllRoles().getValue()).stream().filter(p -> p.getName().equals(roleName)).collect(Collectors.toList());
        if (!roles.isEmpty()) {
            mutableLiveData.setValue(Optional.of(roles.get(0)));
        } else {
            mutableLiveData.setValue(Optional.empty());
        }

        return mutableLiveData;
    }

    public MutableLiveData<List<Models.AppRole>> getAllRoles() {
        System.out.println("Fetching all roles");
        MutableLiveData<List<Models.AppRole>> mutableLiveData = new MutableLiveData<>();

        List<Models.AppRole> roleList = new ArrayList<>(roleRepo.retrieveAll());
        mutableLiveData.setValue(roleList);
        return mutableLiveData;
    }

    //live
    public LiveData<Domain.AppUser> getUser(String uid) {
        return getUserMutable(uid);
    }

    public LiveData<List<String>> getUsernamesDirectLive() {
        return getUsernamesDirect();
    }

    public LiveData<List<String>> getPhoneNumbersDirectLive() {
        return getPhoneNumbersDirect();
    }

    public LiveData<List<Models.AppRole>> getAllRolesLive() {
        return getAllRoles();
    }

    public LiveData<Optional<Models.AppRole>> getRoleLive(String roleName) {
        return findByRoleName(roleName);
    }

    public LiveData<Optional<Domain.AppUser>> getUserLive(String username) {
        return findByUsername(username);
    }


    private static class UserRepo extends AbstractFirestoreRepository<Domain.AppUser> {

        protected UserRepo() {
            super(USER_COLLECTION);
        }


    }

    private static class RoleRepo extends AbstractFirestoreRepository<Models.AppRole> {

        protected RoleRepo() {
            super(ROLE_COLLECTION);
        }


    }

}

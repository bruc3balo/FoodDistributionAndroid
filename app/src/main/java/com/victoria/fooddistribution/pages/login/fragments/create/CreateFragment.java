package com.victoria.fooddistribution.pages.login.fragments.create;

import static com.victoria.fooddistribution.globals.GlobalRepository.firebaseFirestore;
import static com.victoria.fooddistribution.globals.GlobalRepository.userRepository;
import static com.victoria.fooddistribution.globals.GlobalVariables.API_IP;
import static com.victoria.fooddistribution.globals.GlobalVariables.HY;
import static com.victoria.fooddistribution.globals.GlobalVariables.USER_COLLECTION;
import static com.victoria.fooddistribution.pages.welcome.WelcomeActivity.goToNextPage;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.victoria.fooddistribution.R;
import com.victoria.fooddistribution.domain.Domain;
import com.victoria.fooddistribution.globals.userDb.UserViewModel;
import com.victoria.fooddistribution.models.AppRolesEnum;
import com.victoria.fooddistribution.models.Models;
import com.victoria.fooddistribution.models.Models.NewUserForm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CreateFragment extends Fragment {

    private String role = AppRolesEnum.valueOf("ROLE_BUYER").name();
    private NewUserForm newUserForm;

    private final List<String> usernames = new ArrayList<>();
    private final List<String> phoneNumbers = new ArrayList<>();

    private boolean phoneValid = false, usernameValid = false;

    private ProgressBar progressBar;

    public CreateFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_create, container, false);

        progressBar = v.findViewById(R.id.createPb);
        hidePb();

        EditText usernameF = v.findViewById(R.id.usernameF);
        EditText namesF = v.findViewById(R.id.namesF);
        EditText emailF = v.findViewById(R.id.emailF);
        EditText phoneNo = v.findViewById(R.id.phoneF);
        EditText passwordF = v.findViewById(R.id.passwordF);
        EditText cPasswordF = v.findViewById(R.id.cPasswordF);

        usernameF.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!usernames.isEmpty()) {
                    try {
                        if (usernames.contains(charSequence.toString())) {
                            usernameF.setError("Username already exists");
                            usernameValid = false;
                        } else {
                            usernameValid = true;
                        }
                    } catch (Exception ignored) {

                    }
                } else {
                    usernameValid = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        phoneNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (!phoneNumbers.isEmpty()) {
                        if (phoneNumbers.contains(charSequence.toString())) {
                            phoneValid = false;
                            phoneNo.setError("Phone number already exists");
                        } else {
                            phoneValid = true;
                        }
                    } else {
                        phoneValid = true;
                    }
                } catch (Exception ignored) {

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        RadioGroup roleGroup = v.findViewById(R.id.roleGroup);
        roleGroup.setOnCheckedChangeListener((radioGroup, id) -> {
            switch (id) {
                default:
                case R.id.roleBeneficiary:
                    role = AppRolesEnum.valueOf("ROLE_BUYER").name();
                    break;

                case R.id.roleSeller:
                    role = AppRolesEnum.valueOf("ROLE_SELLER").name();
                    break;

                case R.id.roleTransporter:
                    role = AppRolesEnum.valueOf("ROLE_TRANSPORTER").name();
                    break;

                case R.id.roleCertified:
                    role = AppRolesEnum.valueOf("ROLE_CERTIFIED_AUTHORITY").name();
                    break;
            }
        });

        Button createB = v.findViewById(R.id.createB);
        createB.setOnClickListener(view -> {
            if (usernameValid) {
                if (phoneValid) {
                    if (validateForm(usernameF, namesF, emailF, phoneNo, passwordF, cPasswordF)) {
                        try {
                            postUserFormVolley();
                        } catch (JsonProcessingException | JSONException e) {
                            e.printStackTrace();
                        }

                        // saveUserDetails(newUserForm);
                        // emergencyCreate(newUserForm);
                        //emergencyCreate(new Domain.AppUser(newUserForm.getName(), newUserForm.getUsername(), newUserForm.getEmail_address(), newUserForm.getPassword(), LocalDateTime.now().toString(), LocalDateTime.now().toString(), false, false, newUserForm.getRole()));
                    }
                } else {
                    Toast.makeText(requireContext(), "Phone Number Invalid", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Username Invalid", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    private boolean validateForm(EditText username, EditText name, EditText email, EditText phone, EditText password, EditText cPassword) {
        boolean valid = false;
        if (username.getText().toString().isEmpty()) {
            username.setError("Required");
            username.requestFocus();
        } else if (name.getText().toString().isEmpty()) {
            name.requestFocus();
            name.setError("Required");
        } else if (email.getText().toString().isEmpty()) {
            email.requestFocus();
            email.setError("Required");
        } else if (!phone.getText().toString().startsWith("+254")) {
            phone.requestFocus();
            phone.setError("Wrong phone format must start with +254");
        } else if (phone.getText().toString().length() < 12) {
            phone.requestFocus();
            phone.setError("Phone is to have 12 digits");
        } else if (password.getText().toString().length() < 6) {
            password.requestFocus();
            password.setError("Must not be less than 12 digits");
        } else if (!cPassword.getText().toString().equals(password.getText().toString())) {
            password.requestFocus();
            password.setError("Passwords don't match");
        } else {
            newUserForm = new NewUserForm(name.getText().toString(), username.getText().toString(), email.getText().toString(), password.getText().toString(), phone.getText().toString(), HY, HY, role);
            valid = true;
        }
        return valid;
    }

    private void showPb() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hidePb() {
        progressBar.setVisibility(View.GONE);
    }

    private void postUserFormVolley() throws JsonProcessingException, JSONException {
        showPb();
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = API_IP + "/api/v1/auth/authnewuser";

        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(newUserForm);

        System.out.println(data);

        JSONObject jsonObject = new JSONObject(data);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {
            hidePb();
            try {
                Object jsonString = response.get("data");

                Domain.AppUser user = mapper.convertValue(jsonString.toString(), Domain.AppUser.class);
                cacheUserDetails(user);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {

            Toast.makeText(requireActivity(), "" + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
            hidePb();
        });

        // Access the RequestQueue through your singleton class.
        queue.add(jsonObjectRequest);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveNewUser(Models.NewUserForm form) {
        authNewUser(form);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void emergencyCreate(Models.NewUserForm form) {
        authNewUser(form);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void authNewUser(Models.NewUserForm form) {
        showPb();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(form.getEmail_address(), form.getPassword()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                form.setUid(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid());
                Toast.makeText(requireContext(), form.getUsername() + " Created", Toast.LENGTH_SHORT).show();
                try {
                    saveUserDetails(form);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                hidePb();
                Toast.makeText(requireContext(), Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveUserDetails(Models.NewUserForm form) throws JsonProcessingException {


        Domain.AppUser user = new Domain.AppUser(form.getUid(), form.getName(), form.getUsername(), form.getId_number(), form.getEmail_address(), form.getPhone_number(), form.getPassword(), form.getBio(), null, LocalDateTime.now().toString(), LocalDateTime.now().toString(), null, false, false);

        try {
            String data;
            ObjectMapper mapper = new ObjectMapper();
            if (form.getRole() != null) {
                data = mapper.writeValueAsString(Objects.requireNonNull(new ViewModelProvider(this).get(UserViewModel.class).getRoleLive(form.getRole()).getValue()).orElse(null));
            } else {
                data = mapper.writeValueAsString(Objects.requireNonNull(new ViewModelProvider(this).get(UserViewModel.class).getRoleLive("ROLE_BUYER").getValue()).orElse(null));
            }
            user.setRole(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        firebaseFirestore.collection(USER_COLLECTION).document(user.getUid()).set(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(requireContext(), "User details saved", Toast.LENGTH_SHORT).show();
                cacheUserDetails(user);
                new Handler(Looper.myLooper()).postDelayed(this::proceedToNextPage, 2000);
            } else {
                hidePb();
                Toast.makeText(requireContext(), Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void cacheUserDetails(Domain.AppUser appUser) {
        userRepository.insert(appUser);
        proceedToNextPage();
    }

    private void proceedToNextPage() {
        Models.AppRole role = new ObjectMapper().convertValue(userRepository.getUser().getRole(), Models.AppRole.class);
        goToNextPage(requireActivity(), role.getName());
    }

}
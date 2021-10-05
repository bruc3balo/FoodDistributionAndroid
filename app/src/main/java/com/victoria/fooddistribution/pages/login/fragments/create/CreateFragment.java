package com.victoria.fooddistribution.pages.login.fragments.create;

import static com.victoria.fooddistribution.globals.GlobalRepository.userRepository;
import static com.victoria.fooddistribution.globals.GlobalVariables.API_IP;
import static com.victoria.fooddistribution.globals.GlobalVariables.HY;
import static com.victoria.fooddistribution.pages.welcome.WelcomeActivity.goToNextPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.util.CustomClassMapper;
import com.google.gson.Gson;
import com.victoria.fooddistribution.R;
import com.victoria.fooddistribution.domain.Domain;
import com.victoria.fooddistribution.models.AppRolesEnum;
import com.victoria.fooddistribution.models.Models.NewUserForm;
import com.victoria.fooddistribution.pages.admin.AdminActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
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
                if (usernames.contains(charSequence.toString())) {
                    usernameF.setError("Username already exists");
                    usernameValid = false;
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
                if (phoneNumbers.contains(charSequence.toString())) {
                    phoneValid = false;
                    phoneNo.setError("Phone number already exists");
                } else {
                    phoneValid = true;
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
                        postUserForm();
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
        } else if (phone.getText().toString().startsWith("+254")) {
            phone.requestFocus();
            phone.setError("Wrong phone format");
        } else if (phone.getText().toString().length() != 12) {
            phone.requestFocus();
            phone.setError("Phone is to have 12 digits");
        } else if (password.getText().toString().length() < 6) {
            password.requestFocus();
            password.setError("Required");
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

    private void postUserForm() {
        showPb();
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = "http://"+API_IP+"/api/v1/auth/authnewuser";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, newUserForm, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                hidePb();
                try {
                    Object jsonString = response.get("data");
                    Domain.AppUser user = new Domain.AppUser();
                    Gson gson = new Gson();
                    user = gson.fromJson(jsonString.toString(), Domain.AppUser.class);
                    cacheUserDetails(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, error -> {
            Toast.makeText(requireActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            hidePb();
        });

        // Access the RequestQueue through your singleton class.
        queue.add(jsonObjectRequest);

    }


    private void cacheUserDetails (Domain.AppUser appUser) {
        userRepository.insert(appUser);
        proceedToNextPage();
    }

    private void proceedToNextPage () {
        goToNextPage(requireActivity(),userRepository.getUser().getRole());
    }

}
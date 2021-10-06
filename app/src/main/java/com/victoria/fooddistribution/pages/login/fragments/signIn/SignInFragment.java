package com.victoria.fooddistribution.pages.login.fragments.signIn;

import static com.victoria.fooddistribution.globals.GlobalRepository.userRepository;
import static com.victoria.fooddistribution.globals.GlobalVariables.API_IP;
import static com.victoria.fooddistribution.pages.welcome.WelcomeActivity.goToNextPage;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.victoria.fooddistribution.R;
import com.victoria.fooddistribution.domain.Domain;
import com.victoria.fooddistribution.globals.userDb.UserViewModel;
import com.victoria.fooddistribution.models.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;


public class SignInFragment extends Fragment {

    private Models.UsernameAndPasswordAuthenticationRequest request;
    private ProgressBar progressBar;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_sign_in, container, false);

        EditText userIdF = v.findViewById(R.id.userIdF);
        EditText passwordF = v.findViewById(R.id.passwordF);

        progressBar = v.findViewById(R.id.loginPb);
        hidePb();

        Button signInB = v.findViewById(R.id.signInB);
        signInB.setOnClickListener(view -> {
            if (validateForm(userIdF, passwordF)) {
                try {
                    postLoginForm();
                } catch (JsonProcessingException | JSONException e) {
                    e.printStackTrace();
                }
                 //emergencySignIn(userIdF.getText().toString(),passwordF.getText().toString());
            }
        });
        return v;
    }

    private boolean validateForm(EditText username, EditText password) {
        boolean valid = false;
        if (username.getText().toString().isEmpty()) {
            username.setError("Required");
            username.requestFocus();
        } else if (password.getText().toString().isEmpty()) {
            password.requestFocus();
            password.setError("Required");
        } else {
            request = new Models.UsernameAndPasswordAuthenticationRequest(username.getText().toString(), password.getText().toString());
            valid = true;
        }
        return valid;
    }

    private void postLoginForm() throws JSONException, JsonProcessingException {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = API_IP + "/api/v1/login";

        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(request);
        JSONObject object = new JSONObject(data);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, response -> {
            Models.LoginResponse loginResponse = mapper.convertValue(response, Models.LoginResponse.class);

            Toast.makeText(requireContext(), "Working " + response, Toast.LENGTH_SHORT).show();

            decodeToken(loginResponse.getAccess_token());
        }, error -> Toast.makeText(requireActivity(), "Failed to login", Toast.LENGTH_SHORT).show());


        // Access the RequestQueue through your singleton class.
        queue.add(jsonObjectRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void emergencySignIn(String email, String password) {
        signInUser(email, password);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void signInUser(String s, String s1) {
        showPb();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(s, s1).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(requireContext(), "User sign in", Toast.LENGTH_SHORT).show();
                try {
                    new Handler(Looper.myLooper()).postDelayed(() -> cacheUserDetails(getUser(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid())), 2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(requireContext(), Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                hidePb();
            }
        });
    }

    private void showPb() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hidePb() {
        progressBar.setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Domain.AppUser getUser(String uid) {
        return new ViewModelProvider(this).get(UserViewModel.class).getUser(uid).getValue();
    }

    private void decodeToken(String token) {
        try {
            JWT jwt = new JWT(token);
            String issuer = jwt.getIssuer();
            Claim claim = jwt.getClaim("authorities");

            Domain.AppUser user = Objects.requireNonNull(new ViewModelProvider(this).get(UserViewModel.class).getUserLive(issuer).getValue()).orElse(null);

            if (user != null) {
                cacheUserDetails(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Token invalid", Toast.LENGTH_SHORT).show();
        }
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
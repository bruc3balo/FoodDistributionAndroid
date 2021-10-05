package com.victoria.fooddistribution.pages.login.fragments.create;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.victoria.fooddistribution.R;
import com.victoria.fooddistribution.pages.admin.AdminActivity;


public class CreateFragment extends Fragment {


    public CreateFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_create, container, false);

        Button createB = v.findViewById(R.id.createB);
        createB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), AdminActivity.class));
            }
        });
        return v;
    }
}
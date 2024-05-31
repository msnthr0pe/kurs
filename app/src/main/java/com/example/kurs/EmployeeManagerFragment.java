package com.example.kurs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;


public class EmployeeManagerFragment extends Fragment {

    TextView accessText;
    Button addButton;
    Button editButton;
    Button deleteButton;


    public EmployeeManagerFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_employee_manager, container, false);
        accessText = view.findViewById(R.id.access_level);
        addButton = view.findViewById(R.id.addBtn);
        editButton = view.findViewById(R.id.editBtn);
        deleteButton = view.findViewById(R.id.deleteBtn);

        if (getArguments() != null) {
            String access = getArguments().getString("access");
            accessText.setText("Signed in as admin");

            if (Objects.equals(access, "employee")) {
                accessText.setText("Signed in as employee");
                //hideFromEmployee(view);
            }
            if (Objects.equals(access, "manager")) {
                accessText.setText("Signed in as manager");
                // hideFromManager(view);
            }
        }

        addButton.setOnClickListener(v -> {
            Fragment fragment = new AddEmployeeFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return view;
    }
}
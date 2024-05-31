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


public class InformationFragment extends Fragment {

    TextView IDText;
    TextView firstNameText;
    TextView secondNameText;
    TextView positionText;
    TextView personalCardText;
    TextView employmentContractText;
    TextView personalDataConsentText;
    TextView vacationScheduleText;
    TextView employmentRecordText;
    TextView scheduleText;

    Button deleteBtn;
    Button editBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_information, container, false);

        deleteBtn = view.findViewById(R.id.del);
        editBtn = view.findViewById(R.id.redactor);

        if (getArguments() != null) {
            IDText = view.findViewById(R.id.textView1);
            firstNameText = view.findViewById(R.id.textView2);
            secondNameText = view.findViewById(R.id.textView3);
            positionText = view.findViewById(R.id.textView4);
            personalCardText = view.findViewById(R.id.textView5);
            employmentContractText = view.findViewById(R.id.textView6);
            personalDataConsentText = view.findViewById(R.id.textView7);
            vacationScheduleText = view.findViewById(R.id.textView8);
            employmentRecordText =  view.findViewById(R.id.textView9);
            scheduleText = view.findViewById(R.id.textView10);

            String ID = getArguments().getString("ID");
            String name = getArguments().getString("name");
            String surname = getArguments().getString("surname");
            String post = getArguments().getString("post");
            String personalCard = getArguments().getString("personalCard");
            String employmentContract = getArguments().getString("employmentContract");
            String personalDataConsent = getArguments().getString("personalDataConsent");
            String vacationSchedule = getArguments().getString("vacationSchedule");
            String employmentRecord = getArguments().getString("employmentRecord");
            String schedule = getArguments().getString("schedule");

            IDText.setText(ID);
            firstNameText.setText(surname);
            secondNameText.setText(name);
            positionText.setText(post);
            personalCardText.setText(personalCard);
            employmentContractText.setText(employmentContract);
            personalDataConsentText.setText(personalDataConsent);
            vacationScheduleText.setText(vacationSchedule);
            employmentRecordText.setText(employmentRecord);
            scheduleText.setText(schedule);
        }

        editBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putAll(getArguments());
            bundle.putString("mode", "edit");

            Fragment fragment = new ModifyEmployeeFragment();
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return view;
    }
}
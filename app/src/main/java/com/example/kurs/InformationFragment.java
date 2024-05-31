package com.example.kurs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_information, container, false);

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

        return view;
    }
}
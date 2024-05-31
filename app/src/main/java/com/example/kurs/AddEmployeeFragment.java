package com.example.kurs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddEmployeeFragment extends Fragment {

    FirebaseFirestore db;

    Button saveButton;

    View view;

    EditText IDInput;
    EditText nameInput;
    EditText surnameInput;
    EditText postInput;
    EditText personalCardInput;
    EditText employmentContractInput;
    EditText personalDataConsentInput;
    EditText vacationScheduleInput;
    EditText employmentRecordInput;
    EditText scheduleInput;

    String ID;
    String name;
    String surname;
    String post;
    String personalCard;
    String employmentContract;
    String personalDataConsent;
    String vacationSchedule;
    String employmentRecord;
    String schedule;


    public AddEmployeeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_employee, container, false);

        db = FirebaseFirestore.getInstance();

        saveButton = view.findViewById(R.id.save_btn);

        IDInput = view.findViewById(R.id.id_input);
        nameInput = view.findViewById(R.id.first_name_input);
        surnameInput = view.findViewById(R.id.second_name_input);
        postInput = view.findViewById(R.id.position_input);
        personalCardInput = view.findViewById(R.id.personal_card_input);
        employmentContractInput = view.findViewById(R.id.employment_contract_input);
        personalDataConsentInput = view.findViewById(R.id.personal_data_consent_input);
        vacationScheduleInput = view.findViewById(R.id.vacation_schedule_input);
        employmentRecordInput = view.findViewById(R.id.employment_record_input);
        scheduleInput = view.findViewById(R.id.schedule_input);

        saveButton.setOnClickListener(v -> {
            saveData();
        });

        return view;
    }

    private void saveData() {

        ID = IDInput.getText().toString();
        name = nameInput.getText().toString();
        surname = surnameInput.getText().toString();
        post = postInput.getText().toString();
        personalCard = personalCardInput.getText().toString();
        employmentContract = employmentContractInput.getText().toString();
        personalDataConsent = personalDataConsentInput.getText().toString();
        vacationSchedule = vacationScheduleInput.getText().toString();
        employmentRecord = employmentRecordInput.getText().toString();
        schedule = scheduleInput.getText().toString();

        Map<String, Object> data = new HashMap<>();
        data.put("ID", ID);
        data.put("name", name);
        data.put("surname", surname);
        data.put("post", post);
        data.put("personalCard", personalCard);
        data.put("employmentContract", employmentContract);
        data.put("personalDataConsent", personalDataConsent);
        data.put("vacationSchedule", vacationSchedule);
        data.put("employmentRecord", employmentRecord);
        data.put("schedule", schedule);

        db.collection("test")
                .whereEqualTo("ID", ID)
                .get().addOnCompleteListener(task -> {

                    if (!(task.isSuccessful() && !task.getResult().isEmpty())) {
                        db.collection("test")
                                .add(data)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(getActivity(), "Saving successful", Toast.LENGTH_SHORT).show();
                                        ViewGroup group = view.findViewById(R.id.constraintAdd);
                                        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
                                            View view2 = group.getChildAt(i);
                                            if (view2 instanceof EditText) {
                                                ((EditText)view2).setText("");
                                            }
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "Saving failed", Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                    else {
                        Toast.makeText(getActivity(), "Такой ID уже существует", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}
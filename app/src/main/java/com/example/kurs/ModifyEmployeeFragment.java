package com.example.kurs;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ModifyEmployeeFragment extends Fragment {

    FirebaseFirestore db;

    Button modificationButton;

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

    Dialog dialog;


    public ModifyEmployeeFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_employee_modify, container, false);

        db = FirebaseFirestore.getInstance();

        modificationButton = view.findViewById(R.id.done_btn);

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

        if (getArguments() != null) {

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

            IDInput.setText(ID, TextView.BufferType.EDITABLE);
            nameInput.setText(surname);
            surnameInput.setText(name);
            postInput.setText(post);
            personalCardInput.setText(personalCard);
            employmentContractInput.setText(employmentContract);
            personalDataConsentInput.setText(personalDataConsent);
            vacationScheduleInput.setText(vacationSchedule);
            employmentRecordInput.setText(employmentRecord);
            scheduleInput.setText(schedule);
        }

        if (getContext() != null) {
            dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.delete_dialog);
            if (getActivity().getDrawable(R.drawable.dialog_background) != null) {
                dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.dialog_background));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                Button yes = dialog.findViewById(R.id.btn_yes);
                Button no = dialog.findViewById(R.id.btn_no);

                yes.setOnClickListener(v1 -> {
                    DeleteData();
                    clearFields();
                    dialog.hide();
                });

                no.setOnClickListener(v2 -> dialog.hide());

            }
        }

        modificationButton.setOnClickListener(v -> {
            if (getArguments() != null) {
                if (Objects.equals(getArguments().getString("mode"), "add")) {

                    saveData();

                }
                if (Objects.equals(getArguments().getString("mode"), "edit")) {
                    DeleteData();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    saveData();
                }
                if (Objects.equals(getArguments().getString("mode"), "delete")) {
                    dialog.show();
                }
            }
        });

        return view;
    }

    private void clearFields() {
        ViewGroup group = view.findViewById(R.id.constraintAdd);
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view2 = group.getChildAt(i);
            if (view2 instanceof EditText) {
                ((EditText)view2).setText("");
            }
        }
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

        db.collection("employees")
                .whereEqualTo("ID", ID)
                .get().addOnCompleteListener(task -> {

                    if (!(task.isSuccessful() && !task.getResult().isEmpty())) {
                        db.collection("employees")
                                .add(data)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(getActivity(), "Успешно сохранено", Toast.LENGTH_SHORT).show();

                                        clearFields();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "Сохранение не удалось", Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                    else {
                        Toast.makeText(getActivity(), "Такой ID уже существует", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void DeleteData(){
        ID = IDInput.getText().toString();

        db.collection("employees")
                .whereEqualTo("ID", ID)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentID = documentSnapshot.getId();
                            db.collection("employees")
                                    .document(documentID)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            Toast.makeText(getActivity(), "Успешно", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Toast.makeText(getActivity(), "Ошибка", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                        } else {

                            Toast.makeText(getActivity(), "Такой записи не существует", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}
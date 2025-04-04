package com.example.kurs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;


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

    FirebaseFirestore db;

    Dialog dialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void hideFromEmployee(){
        deleteBtn.setVisibility(View.INVISIBLE);
        editBtn.setVisibility(View.INVISIBLE);
    }

    public void hideFromManager(){
        deleteBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_information, container, false);

        db = FirebaseFirestore.getInstance();

        deleteBtn = view.findViewById(R.id.del);
        editBtn = view.findViewById(R.id.redactor);

        if (getArguments() != null) {
            if (Objects.equals(getArguments().getString("access"), "manager")) {
                hideFromManager();
            }
            if (Objects.equals(getArguments().getString("access"), "employee")) {
                hideFromEmployee();
            }

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
                    deleteRecord();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    Fragment fragment = new EmployeesFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    dialog.hide();
                });

                no.setOnClickListener(v2 -> dialog.hide());

            }
        }

        deleteBtn.setOnClickListener(v -> {
            dialog.show();
        });


        return view;
    }

    private void deleteRecord() {
        String id = IDText.getText().toString();

        db.collection("employees")
                .whereEqualTo("ID", id)
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
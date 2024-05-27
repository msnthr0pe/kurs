package com.example.kurs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class EditCredentialsFragment extends Fragment {

    private FirebaseFirestore db;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    EditText newLogin;
    EditText newPassword;
    private String accessLevel;
    private Button enterBtn;
    private Button deleteBtn;
    private Button updateBtn;
    String login;
    String password;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_credentials, container, false);

        db = FirebaseFirestore.getInstance();

        newLogin = view.findViewById(R.id.new_login);
        newPassword = view.findViewById(R.id.new_password);

        spinner = view.findViewById(R.id.access_choice);
        refreshAccessSpinner();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                accessLevel = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        enterBtn = view.findViewById(R.id.enter);
        enterBtn.setOnClickListener(v -> {
            checkForDuplicates();
        });

        deleteBtn = view.findViewById(R.id.delete);
        deleteBtn.setOnClickListener(v -> {
            deleteRecord();
        });

        updateBtn = view.findViewById(R.id.update);
        updateBtn.setOnClickListener(v -> {
            String tempLogin = newLogin.getText().toString();
            String tempPassword = newPassword.getText().toString();
            deleteRecord();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            newLogin.setText(tempLogin);
            newPassword.setText(tempPassword);
            saveRecord();
        });
        return view;
    }

    private void refreshAccessSpinner() {
        List<String> arr = Arrays.asList("employee", "manager");

        if (getActivity() != null) {
            adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, arr);
            adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            spinner.setAdapter(adapter);
        }
    }

    private void saveRecord() {
        login = newLogin.getText().toString();
        password = newPassword.getText().toString();

        Map<String, Object> data = new HashMap<>();
        data.put("login", login);
        data.put("password", password);
        data.put("access", accessLevel);

        newLogin.setText("");
        newPassword.setText("");

        db.collection("credentials")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "Saving successful", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Saving failed", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void deleteRecord(){
        login = newLogin.getText().toString();

        newLogin.setText("");
        newPassword.setText("");

        db.collection("credentials")
                .whereEqualTo("login", login)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String access = documentSnapshot.getString("access");
                            if (Objects.equals(access, "admin")) {
                                Toast.makeText(getActivity(), "Unable to delete admin user", Toast.LENGTH_SHORT).show();
                            } else {
                                String documentID = documentSnapshot.getId();
                                db.collection("credentials")
                                        .document(documentID)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }
                        } else {

                            Toast.makeText(getActivity(), "No such record", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkForDuplicates(){
        login = newLogin.getText().toString();

        db.collection("credentials")
                .whereEqualTo("login", login)
                .get().addOnCompleteListener(task -> {

                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        Toast.makeText(getActivity(), "Account already exists", Toast.LENGTH_SHORT).show();
                        newLogin.setText("");
                        newPassword.setText("");
                    } else {
                        saveRecord();
                    }
                });
    }
}
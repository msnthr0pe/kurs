/*package com.example.kurs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MainFragment extends Fragment {
    FirebaseFirestore db;
    Button saveBtn;
    Button delBtn;
    Button updateBtn;
    EditText nameInput;
    EditText surname_input;
    EditText ageInput;
    EditText salary_input;
    EditText post_input;
    TextView accessText;

    public MainFragment() {
    }

    public void hideFromEmployee(View view){
        delBtn.setVisibility(View.INVISIBLE);
        updateBtn.setVisibility(View.INVISIBLE);
    }

    public void hideFromManager(View view){
        delBtn.setVisibility(View.INVISIBLE);
    }

    public void getData() {
        db = FirebaseFirestore.getInstance();

        db.collection("employees")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("employee", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("employee", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getData();
    }
    /*
    private void closeKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);


        saveBtn = view.findViewById(R.id.save_button);
        delBtn = view.findViewById(R.id.delete_button);
        updateBtn = view.findViewById(R.id.update_button);
        nameInput = view.findViewById(R.id.name_input);
        surname_input = view.findViewById(R.id.surname_input);
        ageInput = view.findViewById(R.id.age_input);
        salary_input = view.findViewById(R.id.salary_input);
        post_input = view.findViewById(R.id.post_input);
        accessText = view.findViewById(R.id.access_text);

        if (getArguments() != null) {
            String access = getArguments().getString("access");
            accessText.setText("Signed in as admin");

            if (Objects.equals(access, "employee")) {
                accessText.setText("Signed in as employee");
                hideFromEmployee(view);
            }
            if (Objects.equals(access, "manager")) {
                accessText.setText("Signed in as manager");
                hideFromManager(view);
            }
        }


        saveBtn.setOnClickListener(v -> saveData());

        delBtn.setOnClickListener(v -> {
            DeleteData();
            nameInput.setText("");
        });

        updateBtn.setOnClickListener(v -> {
            DeleteData();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            saveData();
        });

        return view;
    }

    private void saveData() {
        //closeKeyboard();
        String name = nameInput.getText().toString();
        String surname = surname_input.getText().toString();
        String age = ageInput.getText().toString();
        String salary = salary_input.getText().toString();
        String post = post_input.getText().toString();

        db = FirebaseFirestore.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("surname", surname);
        data.put("age", age);
        data.put("salary", salary);
        data.put("post", post);

        nameInput.setText("");
        surname_input.setText("");
        ageInput.setText("");
        salary_input.setText("");
        post_input.setText("");

        db.collection("newemployees")
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

    private void DeleteData(){
        String name = nameInput.getText().toString();

        db.collection("newemployees")
                .whereEqualTo("name", name)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentID = documentSnapshot.getId();
                            db.collection("newemployees")
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
                        } else {

                            Toast.makeText(getActivity(), "No such record", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}*/
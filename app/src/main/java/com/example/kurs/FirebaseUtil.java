package com.example.kurs;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/*
public class FirebaseUtil {

    FirebaseFirestore db;
    Context context;

    FirebaseUtil(Context context) {
        db = FirebaseFirestore.getInstance();
        this.context = context;
    }

    public void editCurrentAccount(String collectionName, String login) {

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
                        Toast.makeText(context, "Saving successful", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(, "Saving failed", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void deleteRecord(String login){




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
}
*/
package com.example.kurs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class AccountFragment extends Fragment {

    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseFirestore db;

    ImageView profilePic;
    EditText newLogin;
    EditText newPassword;
    Button applyBtn;
    String oldLogin;
    String login;
    String password;
    String access;
    String oldId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == MainActivity.RESULT_OK) {
                        Intent data = result.getData();
                        if(data != null && data.getData() != null) {
                            filePath = data.getData();
                            uploadImage();
                        }
                    }
                }
                );
    }

    private void uploadImage() {

        if(filePath != null) {

            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("profile_pic/" + getIdFromFile());
            //Toast.makeText(getActivity(), getIdFromFile(), Toast.LENGTH_SHORT).show();

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Загружено", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Ошибка" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            progressDialog.setMessage("Загрузка " + (int)progress + "%");
                        }
                    });
        }

        setProfilePic(getContext(), filePath, profilePic);
    }
    private void deleteImageAttempt() {
        StorageReference deleteFile = storageReference.child("profile_pic/" + oldId);
        deleteFile.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(getActivity(), "Previous Image Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView) {
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        db = FirebaseFirestore.getInstance();
        newLogin = view.findViewById(R.id.new_login);
        newPassword = view.findViewById(R.id.new_password);
        applyBtn = view.findViewById(R.id.apply);
        access = "";
        if (getArguments() != null) {
            access = getArguments().getString("access");
            oldLogin = getArguments().getString("login");
        }

        //Toast.makeText(getActivity(), getIdFromFile(), Toast.LENGTH_SHORT).show();

        applyBtn.setOnClickListener(v -> {
            login = newLogin.getText().toString();
            password = newPassword.getText().toString();

            DeleteData();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            oldId = getIdFromFile();
            saveData();
            deleteImageAttempt();

            newLogin.setText("");
            newPassword.setText("");
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        profilePic = view.findViewById(R.id.profileImage);
        profilePic.setOnClickListener(v -> {

            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512,512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher.launch(intent);
                            return null;
                        }
                    });
        });


        setPicture();

        return view;
    }

    private void setPicture() {
        String lastLogin = getIdFromFile();

        if (!lastLogin.isEmpty()) {
            FirebaseStorage.getInstance().getReference().child("profile_pic")
                    .child(lastLogin).getDownloadUrl()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Uri uri = task.getResult();
                            setProfilePic(getContext(), uri, profilePic);
                        }
                    });
        }
    }

    public void createFile(String filename, String login, String password) {
        String fileContents;
        if (Objects.equals(filename, "cred")) {
            fileContents = login + '\n' + password;
        }
        else {
            fileContents = login;
        }
        try (FileOutputStream fos = getActivity().openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(fileContents.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String getIdFromFile()  {
        ArrayList<String> arrayList = new ArrayList<>();
        try (FileInputStream fis = getActivity().openFileInput("currentLogin")) {
            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                arrayList.add(line);
                while (line != null) {
                    line = reader.readLine();
                    arrayList.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return arrayList.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void DeleteData(){
        db.collection("credentials")
                .whereEqualTo("login", oldLogin)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentID = documentSnapshot.getId();
                            db.collection("credentials")
                                    .document(documentID)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            //Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            //Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                        } else {

                            Toast.makeText(getActivity(), "Такой записи не существует", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void saveData() {
        Map<String, Object> data = new HashMap<>();
        data.put("login", login);
        data.put("password", password);
        data.put("access", access);

        db.collection("credentials")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "Сохранено", Toast.LENGTH_SHORT).show();
                        createFile("currentLogin", documentReference.getId(), "");
                        createFile("cred", login, password);
                        uploadImage();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Ошибка", Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
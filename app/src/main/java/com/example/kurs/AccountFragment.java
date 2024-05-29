package com.example.kurs;

import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class AccountFragment extends Fragment {

    ImageView profilePic;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if(data != null && data.getData() != null) {
                            filePath = data.getData();
                            if(filePath != null) {
                                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setTitle("Uploading...");
                                progressDialog.show();

                                StorageReference ref = storageReference.child("profile_pic/" + getIdFromFile());
                                ref.putFile(filePath)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getActivity(), "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                                double progress = (100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                                                progressDialog.setMessage("Uploaded " + (int)progress + "%");
                                            }
                                        });
                            }

                            AndroidUtil.setProfilePic(getContext(), filePath, profilePic);
                        }
                    }
                }
                );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        View view = inflater.inflate(R.layout.fragment_account, container, false);
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

        String lastLogin = getIdFromFile();

        if (!lastLogin.isEmpty()) {
            FirebaseStorage.getInstance().getReference().child("profile_pic")
                    .child(lastLogin).getDownloadUrl()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Uri uri = task.getResult();
                            AndroidUtil.setProfilePic(getContext(), uri, profilePic);
                        }
                    });
        }

        return view;
    }

    private String getIdFromFile()
    {
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
    /*

    void updateToFirestore(){
        FirebaseUtil.currentUserDetails().set(currentUserModel)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(getActivity(), "Updated successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), "Update failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }*/

}
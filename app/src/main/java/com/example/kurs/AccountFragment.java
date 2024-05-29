package com.example.kurs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class AccountFragment extends Fragment {

    ImageView profilePic;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if(data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            AndroidUtil.setProfilePic(getContext(), selectedImageUri, profilePic);
                        }
                    }
                }
                );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
            if(selectedImageUri != null) {
                FirebaseUtil.getCurrentProfileStorageRef().putFile(selectedImageUri)
                        .addOnCompleteListener(task -> {

                        });
            }
        });

        return view;
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
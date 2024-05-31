package com.example.kurs;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    String fileName = "cred";
    EditText loginInput;
    EditText passwordInput;

    static boolean isChecked = false;


    public void createFile(String filename, String login, String password) {
        String fileContents;
        if (Objects.equals(filename, "cred")) {
            fileContents = login + '\n' + password;
        }
        else {
            fileContents = login;
        }
        try (FileOutputStream fos = this.openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(fileContents.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> readFile() {
        ArrayList<String> arrayList = new ArrayList<>();
        try (FileInputStream fis = this.openFileInput("cred")) {
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
            return arrayList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String autoLogin;
        String autoPassword;
        File f = getFileStreamPath(fileName);
        if (f.length() > 1) {
            ArrayList<String> arrayList = readFile();
            autoLogin = arrayList.get(0);
            autoPassword = arrayList.get(1);
            logIntoAccount(autoLogin, autoPassword, true);
        }

        loginInput = findViewById(R.id.new_login);
        passwordInput = findViewById(R.id.new_password);
        Button enterBtn = findViewById(R.id.enter);

        enterBtn.setOnClickListener(v -> {
            String login = loginInput.getText().toString();
            String password = passwordInput.getText().toString();
            logIntoAccount(login, password, false);
        });

    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        if (checked) {
            isChecked = true;
        } else {
            isChecked = false;
        }

    }


    private void logIntoAccount(String login, String password, boolean auto) {
        Intent intent = new Intent(MainActivity.this, Activity.class);

        db = FirebaseFirestore.getInstance();
        db.collection("credentials")
                .whereEqualTo("login", login)
                .get().addOnCompleteListener(task -> {

                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        String document = documentSnapshot.getString("password");
                        String access = documentSnapshot.getString("access");
                        if (Objects.equals(document, password)) {

                            if (isChecked || auto) {
                                createFile("cred", login, password);
                            }
                            else {
                                createFile("cred", "", "");
                            }
                            intent.putExtra("access", access);
                            intent.putExtra("login", login);
                            intent.putExtra("password", password);

                            createFile("currentLogin", documentSnapshot.getId(), "");

                            startActivity(intent);

                        } else {
                            loginInput.setText("");
                            passwordInput.setText("");
                            Toast.makeText(MainActivity.this, "Неверные данные", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        loginInput.setText("");
                        passwordInput.setText("");
                        Toast.makeText(MainActivity.this, "Неверные данные", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
package com.example.kurs;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        nextBtn = findViewById(R.id.start_app);
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        nextBtn.setOnClickListener(v -> {
            startActivity(intent);
        });
    }
}
package com.example.kurs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class InformationFragment extends Fragment {

    TextView nameTextView;
    TextView surnameTextView;
    TextView ageTextView;
    TextView salaryTextView;
    TextView postTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_information, container, false);

        if (getArguments() != null) {
            nameTextView = view.findViewById(R.id.textView1);
            surnameTextView = view.findViewById(R.id.textView2);
            ageTextView = view.findViewById(R.id.textView3);
            salaryTextView = view.findViewById(R.id.textView4);
            postTextView = view.findViewById(R.id.textView5);

            String name = getArguments().getString("name");
            String surname = getArguments().getString("surname");
            String age = getArguments().getString("age");
            String salary = getArguments().getString("salary");
            String post = getArguments().getString("post");

            nameTextView.setText(name);
            surnameTextView.setText(surname);
            ageTextView.setText(age);
            salaryTextView.setText(salary);
            postTextView.setText(post);
        }

        return view;
    }
}
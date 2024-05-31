package com.example.kurs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;


public class EmployeesFragment extends Fragment implements RecyclerViewInterface{

    RecyclerView recyclerView;
    ArrayList<Employee> employeeArrayList;
    KursAdapter kursAdapter;
    FirebaseFirestore db;
    ArrayAdapter<String> adapter;
    Spinner spinner;



    public EmployeesFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employees,container,false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager((getActivity())));

        db = FirebaseFirestore.getInstance();
        employeeArrayList = new ArrayList<>();
        kursAdapter = new KursAdapter(getActivity(), employeeArrayList, this);
        recyclerView.setAdapter(kursAdapter);

        EventChangeListener();
        ArrayList<Employee> arr = new ArrayList<>();
        kursAdapter = new KursAdapter(getActivity(), arr, this);
        recyclerView.setAdapter(kursAdapter);

        spinner = view.findViewById(R.id.post_selector);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                refreshRecycler(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    @Override
    public void onItemClick(int position) {
        Fragment fragment = new InformationFragment();

        Bundle bundle = new Bundle();
        Employee pos = employeeArrayList.get(position);
        bundle.putString("ID", pos.getID());
        bundle.putString("name", pos.getName());
        bundle.putString("surname", pos.getSurname());
        bundle.putString("post", pos.getPost());
        bundle.putString("personalCard", pos.getPersonalCard());
        bundle.putString("employmentContract", pos.getEmploymentContract());
        bundle.putString("personalDataConsent", pos.getPersonalDataConsent());
        bundle.putString("vacationSchedule", pos.getVacationSchedule());
        bundle.putString("employmentRecord", pos.getEmploymentRecord());
        bundle.putString("schedule", pos.getSchedule());

        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void refreshSpinner() {
        ArrayList<String> arr = new ArrayList<>();
        arr.add("Все должности");
        for (int i=0; i<employeeArrayList.size(); i++) {
            arr.add(employeeArrayList.get(i).getPost());
        }
        HashSet<String> set = new HashSet<>(arr);
        arr.clear();
        arr.addAll(set);
        Log.d("recycle", String.valueOf(arr.size()));
        if (getActivity() != null) {
            adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, arr);
            adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            spinner.setAdapter(adapter);
        }
    }

    private void EventChangeListener() {

        db.collection("employees")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            Log.e("kurs", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {

                            if (dc.getType() == DocumentChange.Type.ADDED) {

                                employeeArrayList.add(dc.getDocument().toObject(Employee.class));
                            }
                        }
                        refreshSpinner();
                    }
                });
    }


    private void refreshRecycler(String post) {
        ArrayList<Employee> arr = new ArrayList<>(employeeArrayList);
        if (post != "Все должности") {
            int i = 0;
            while (i < arr.size()) {
                if (!Objects.equals(arr.get(i).getPost(), post)) {
                    arr.remove(i);
                } else {
                    i += 1;
                }
            }
        }
        KursAdapter kursAdapter2 = new KursAdapter(getActivity(), arr, this);
        recyclerView.setAdapter(kursAdapter2);
    }
}
package com.example.kurs;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChartFragment extends Fragment {

    FirebaseFirestore db;
    int employeeCount;
    int managerCount;
    int adminCount;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        db = FirebaseFirestore.getInstance();

        getPostCount(view);



        return view;
    }

    private void getPostCount(View view) {
        db.collection("credentials")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String access = document.getString("access");
                                Log.d("kurs", access);
                                if (Objects.equals(access, "admin")) {
                                    adminCount ++;
                                }
                                if (Objects.equals(access, "manager")) {
                                    managerCount ++;
                                }
                                if (Objects.equals(access, "employee")) {
                                    employeeCount ++;
                                }
                            }
                            makePie(view);
                        } else {
                            Toast.makeText(getActivity(), "Error getting documents", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void makePie(View view) {
        Pie pie = AnyChart.pie();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Admins", adminCount));
        data.add(new ValueDataEntry("Managers", managerCount));
        data.add(new ValueDataEntry("Employees", employeeCount));

        pie.data(data);

        AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);
        anyChartView.setChart(pie);
    }
}
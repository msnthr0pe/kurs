package com.example.kurs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KursAdapter extends RecyclerView.Adapter<KursAdapter.KursViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<Employee> employeeArrayList;

    public KursAdapter(Context context, ArrayList<Employee> employeeArrayList, RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.employeeArrayList = employeeArrayList;
    }
    /*
    public Map<String, String> getInfo(int position) {
        Map<String, String> arr = new HashMap();

        Employee employee = employeeArrayList.get(position);

        arr.put("name", employee.getName());
        arr.put("surname", employee.getSurname());
        arr.put("age", employee.getAge());
        arr.put("salary", employee.getSalary());
        arr.put("post", employee.getPost());

        return arr;
    } */

    @NonNull
    @Override
    public KursAdapter.KursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);

        return new KursViewHolder(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull KursAdapter.KursViewHolder holder, int position) {
        Employee employee = employeeArrayList.get(position);

        holder.name.setText(employee.getName());
        holder.surname.setText(employee.getSurname());
        holder.post.setText(employee.getPost());

    }

    @Override
    public int getItemCount() {
        return employeeArrayList.size();
    }

    public static class KursViewHolder extends RecyclerView.ViewHolder{

        TextView name, surname, age, salary, post;

        public KursViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            surname = itemView.findViewById(R.id.surname);
            post = itemView.findViewById(R.id.post);

            itemView.setOnClickListener(v -> {
                if (recyclerViewInterface != null) {
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onItemClick(pos);
                    }
                }
            });
        }
    }
}

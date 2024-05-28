package com.example.kurs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class KursAdapter extends RecyclerView.Adapter<KursAdapter.KursViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<Employee> employeeArrayList;

    public KursAdapter(Context context, ArrayList<Employee> employeeArrayList, RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.employeeArrayList = employeeArrayList;
    }

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
        holder.age.setText(employee.getAge());
        holder.salary.setText(employee.getSalary());
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
            age = itemView.findViewById(R.id.age);
            salary = itemView.findViewById(R.id.salary);
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

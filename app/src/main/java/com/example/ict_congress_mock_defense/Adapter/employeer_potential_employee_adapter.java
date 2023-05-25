package com.example.ict_congress_mock_defense.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ict_congress_mock_defense.Classes.companyClass;
import com.example.ict_congress_mock_defense.Classes.employeeClass;
import com.example.ict_congress_mock_defense.R;
import com.example.ict_congress_mock_defense.details_activity;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class employeer_potential_employee_adapter extends RecyclerView.Adapter<employeer_potential_employee_adapter.ViewHolder> {

    Context context;
    String userID;
    List<employeeClass> list;

    public employeer_potential_employee_adapter(Context context, String userID) {
        this.context = context;
        this.list = new ArrayList<>();
        this.userID = userID;
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public void addSkills(employeeClass timestampClass) {
        list.add(timestampClass);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        employeeClass a = list.get(position);

        holder.position.setText(a.getName());
        Picasso.get().load(a.getImage()).placeholder(R.drawable.icon_noimage).into(holder.imageRecycler);
        holder.salary_range.setText(a.getJob_salary());
        if(a.getJob_skill_set()!=null && a.getJob_skill_set().size()!=0)
        holder.company.setText(a.getJob_skill_set().get(0));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, details_activity.class);
                i.putExtra("userName",(Serializable) a);
                i.putExtra("from","Employee");
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView position,
                company,
                salary_range;
        ImageView imageRecycler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageRecycler = itemView.findViewById(R.id.imageRecycler);
            position = itemView.findViewById(R.id.position);
            company = itemView.findViewById(R.id.company);
            salary_range = itemView.findViewById(R.id.salary_range);


        }
    }
}

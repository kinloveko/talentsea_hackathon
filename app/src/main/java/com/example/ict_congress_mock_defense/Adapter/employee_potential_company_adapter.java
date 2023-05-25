package com.example.ict_congress_mock_defense.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ict_congress_mock_defense.Classes.Skills;
import com.example.ict_congress_mock_defense.Classes.companyClass;
import com.example.ict_congress_mock_defense.R;
import com.example.ict_congress_mock_defense.details_activity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class employee_potential_company_adapter extends RecyclerView.Adapter<employee_potential_company_adapter.ViewHolder> {

    Context context;
    String userID;
    List<companyClass> list;

    public employee_potential_company_adapter(Context context, String userID) {
        this.context = context;
        this.list = new ArrayList<>();
        this.userID = userID;
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public void addSkills(companyClass timestampClass) {
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
        companyClass a = list.get(position);

        holder.company.setText(a.getCompany());

        Picasso.get().load(a.getImage()).placeholder(R.drawable.icon_noimage).into(holder.imageRecycler);
        holder.salary_range.setText(a.getSalaryRange());
        if(a.getIsLookingFor()!=null && a.getIsLookingFor().size()!=0)
        holder.position.setText(a.getIsLookingFor().get(0));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, details_activity.class);
                i.putExtra("userName",(Serializable) a);
                i.putExtra("from","Employer");
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

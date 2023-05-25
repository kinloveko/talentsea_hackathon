package com.example.ict_congress_mock_defense.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ict_congress_mock_defense.Activities.Skills_activity;
import com.example.ict_congress_mock_defense.Classes.Skills;
import com.example.ict_congress_mock_defense.Classes.timestampClass;
import com.example.ict_congress_mock_defense.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class adapter_skills extends RecyclerView.Adapter<adapter_skills.ViewHolder> {

    Context context;
    String userID;
    List<Skills> list;

     public adapter_skills(Context context, String userID){
        this.context = context;
        this.list = new ArrayList<>();
        this.userID = userID;
    }
    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }
    public void addSkills(Skills timestampClass){
        list.add(timestampClass);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.skills_set_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Skills a = list.get(position);
        holder.text.setText(a.getSkills());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setTitle("Remove skill?");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     
                    }
                });
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseFirestore.getInstance().collection("Users")
                                .document(userID)
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.exists()){
                                            if(documentSnapshot.get("job_skill_set")!=null){
                                                List<String> arrString = (List<String>) documentSnapshot.get("job_skill_set");
                                                arrString.remove(position);
                                                list.remove(position);
                                                Map<String,Object> map = new HashMap<>();
                                                map.put("job_skill_set",arrString);
                                                FirebaseFirestore.getInstance().collection("Users")
                                                        .document(userID)
                                                        .set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {

                                                                Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                                                                dialog.dismiss();
                                                            }
                                                        });
                                            }


                                        }
                                    }
                                });
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         TextView text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }
}

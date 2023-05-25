package com.example.ict_congress_mock_defense.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ict_congress_mock_defense.Classes.timestampClass;
import com.example.ict_congress_mock_defense.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class adapterLogged extends RecyclerView.Adapter<adapterLogged.ViewHolder> {

    Context context;
    String userID;
    List<timestampClass> list;
     public adapterLogged(Context context,String userID){
        this.context = context;
        this.list = new ArrayList<>();
        this.userID = userID;
    }
    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }
    public void addTime(timestampClass timestampClass){
        list.add(timestampClass);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recently_logged,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        timestampClass a = list.get(position);
        holder.text.setText(a.getTime().toDate().toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("IDSS",a.getId());
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setTitle("Delete history logged?");
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
                                .collection("recentlyLogged").document(a.getTime_id()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            dialog.dismiss();
                                            Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
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
            text = itemView.findViewById(R.id.dateTime);
        }
    }
}

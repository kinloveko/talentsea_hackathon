package com.example.ict_congress_mock_defense.Activities.Settings;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ict_congress_mock_defense.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;

public class EditProfileSettings extends AppCompatActivity {

    TextView nameUpdate, addressUpdate,
            birthUpdate;
    RelativeLayout report_layout, birth_layout,
            address_layout;
    LinearLayout backLayoutService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(Color.parseColor("#e28743"));
        }
        backLayoutService  = findViewById(R.id.backLayoutService);
        backLayoutService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileSettings.this.onBackPressed();
                finish();
            }
        });
        nameUpdate = findViewById(R.id.nameUpdate);
        report_layout = findViewById(R.id.report_layout);
        addressUpdate = findViewById(R.id.addressUpdate);
        birthUpdate = findViewById(R.id.birthUpdate);
        birth_layout = findViewById(R.id.birth_layout);
        address_layout = findViewById(R.id.address_layout);

        FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) return;
                        if (value.exists()) {

                            birthUpdate.setText(value.getString("birth"));
                            nameUpdate.setText(value.getString("name"));
                            if (value.getString("address") != null || value.getString("address") != "")
                                addressUpdate.setText(value.getString("address"));

                        }
                    }
                });

        birth_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBirthDialog();
            }
        });
        address_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddressDialog();
            }
        });

        report_layout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                openNameDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void openAddressDialog() {
        AlertDialog.Builder builder3 = new AlertDialog.Builder(EditProfileSettings.this);
        View view2 = View.inflate(EditProfileSettings.this, R.layout.dialog_address, null);
        builder3.setCancelable(false);
        EditText editText = view2.findViewById(R.id.editText);

        //button
        Button cancel, okay;
        //cancel button
        cancel = view2.findViewById(R.id.cancel);
        cancel.setText("Cancel");
        //Okay button
        okay = view2.findViewById(R.id.okay);
        //EditText Layout
        builder3.setView(view2);
        AlertDialog alert3 = builder3.create();
        alert3.show();
        alert3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert3.dismiss();
            }
        });
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().isEmpty()) {
                    FirebaseFirestore.getInstance().collection("Users")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .update("address", editText.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    alert3.dismiss();
                                    Toast.makeText(EditProfileSettings.this, "Successfully Changed", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(EditProfileSettings.this, "Field name is empty", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void openBirthDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_birthday, null);
        DatePicker datePicker = view.findViewById(R.id.datePicker);
        datePicker.setMaxDate(System.currentTimeMillis());
        builder.setView(view);
        MaterialButton cancel, save;
        TextView message, title;
        title = view.findViewById(R.id.pet_add_birthday_set_title);
        message = view.findViewById(R.id.pet_add_birthday_set_message);
        cancel = view.findViewById(R.id.birthday_dialog_btn_cancel);
        save = view.findViewById(R.id.birthday_dialog_btn_done);
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                String formatedDate = sdf.format(calendar.getTime());

                FirebaseFirestore.getInstance().collection("Users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .update("birth", formatedDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(EditProfileSettings.this, "Successfully Changed", Toast.LENGTH_SHORT).show();
                            }
                        });

                birthUpdate.setText(formatedDate);
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void openNameDialog() {
        AlertDialog.Builder builder3 = new AlertDialog.Builder(EditProfileSettings.this);
        View view2 = View.inflate(EditProfileSettings.this, R.layout.dialog_name, null);
        builder3.setCancelable(false);
        EditText editText = view2.findViewById(R.id.editText);

        //button
        Button cancel, okay;
        //cancel button
        cancel = view2.findViewById(R.id.cancel);
        cancel.setText("Cancel");
        //Okay button
        okay = view2.findViewById(R.id.okay);
        //EditText Layout
        builder3.setView(view2);
        AlertDialog alert3 = builder3.create();
        alert3.show();
        alert3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert3.dismiss();
            }
        });
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().isEmpty()) {
                    FirebaseFirestore.getInstance().collection("Users")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .update("name", editText.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    alert3.dismiss();
                                    Toast.makeText(EditProfileSettings.this, "Successfully Changed", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(EditProfileSettings.this, "Field name is empty", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
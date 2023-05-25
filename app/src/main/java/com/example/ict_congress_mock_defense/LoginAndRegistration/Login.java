package com.example.ict_congress_mock_defense.LoginAndRegistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.ict_congress_mock_defense.Activities.Dashboard;
import com.example.ict_congress_mock_defense.Classes.CONSTANT;
import com.example.ict_congress_mock_defense.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    Button register, Button_LoginID;
    TextInputEditText email_Edit, password_Edit;
    TextInputLayout password_layout, email_layout;
    final Pattern passwordPattern = java.util.regex.Pattern.compile("^" +
            "(?=.*[@#$%^&!+=])" +     // at least 1 special character
            "(?=\\S+$)" +            // no white spaces
            ".{5,}" +                // at least 5 characters
            "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#212A3E"));
        CONSTANT constant = new CONSTANT();
        email_Edit = findViewById(R.id.email_Edit);
        password_Edit = findViewById(R.id.password_Edit);
        password_layout = findViewById(R.id.password_layout);
        email_layout = findViewById(R.id.email_layout);
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registration.class));
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            }
        });
        Button_LoginID = findViewById(R.id.Button_LoginID);
        Button_LoginID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = email_Edit.getText().toString();
                String pass = password_Edit.getText().toString();

                CharSequence emailcs = email_Edit.getText().toString();
                if (email.isEmpty()) {
                    email_layout.setHelperText("Please input your email");
                    email_layout.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    email_Edit.requestFocus();
                    return;
                } else if (!(Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), emailcs))) {
                    email_layout.setHelperText("Enter a valid email ex. example@gmail.com");
                    email_layout.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    email_Edit.requestFocus();
                    return;
                }
                CharSequence passwordcs = password_Edit.getText().toString();
                if (pass.isEmpty()) {
                    password_layout.setHelperText("Please input your password");
                    password_layout.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    password_Edit.requestFocus();
                    return;
                } else if (pass.length() < 5) {
                    password_layout.setHelperText("Password must have 5 characters");
                    password_layout.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    password_Edit.requestFocus();
                    return;
                } else if (!(Pattern.matches(passwordPattern.pattern(), passwordcs))) {

                    password_layout.setHelperText(constant.passError);
                    password_layout.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
                    password_Edit.requestFocus();
                    return;
                }
                else{
                    password_layout.setHelperText("");
                }
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            assert user != null;
                            if (user.isEmailVerified()) {

                                FirebaseFirestore.getInstance().collection("Users")
                                        .document(user.getUid()).update("pass",pass);

                                Map<String, Object> map = new HashMap<>();
                                map.put("time", Timestamp.now());
                                map.put("id",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                FirebaseFirestore.getInstance().collection("Users")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .collection("recentlyLogged").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                FirebaseFirestore.getInstance().collection("Users").
                                                        document(user.getUid())
                                                        .collection("recentlyLogged")
                                                        .document(documentReference.getId()).update("time_id",documentReference.getId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Intent i = new Intent(Login.this, Dashboard.class);
                                                                // set the new task and clear flags
                                                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                startActivity(i);
                                                                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                                                                finish();
                                                            }
                                                        });

                                            }
                                        });

                            } else {
                                user.sendEmailVerification();
                                Toast.makeText(Login.this, "Email Verification Required, please check your email.", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(Login.this, "Account does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, Dashboard.class));
            this.finish();
        } else {

        }
    }
}


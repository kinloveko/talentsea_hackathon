package com.example.ict_congress_mock_defense.LoginAndRegistration;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ict_congress_mock_defense.Classes.CONSTANT;
import com.example.ict_congress_mock_defense.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class Registration extends AppCompatActivity {

    CONSTANT constant = new CONSTANT();
    TextInputLayout name_layout;
    TextInputEditText name_Edit;

    TextInputLayout email_layout;
    TextInputEditText email_Edit;

    TextInputLayout password_layout;
    TextInputEditText password_Edit;

    TextInputLayout birth_layout;
    TextInputEditText birth_Edit;

    Button registration;
    Button gotoLogin;

    TextInputLayout role_layout;
    TextInputEditText role_Edit;

    final Pattern passwordPattern = java.util.regex.Pattern.compile("^" +
            "(?=.*[@#$%^&!+=])" +     // at least 1 special character
            "(?=\\S+$)" +            // no white spaces
            ".{5,}" +                // at least 5 characters
            "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#212A3E"));
        CardView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registration.this.onBackPressed();
                finish();
            }
        });
        role_layout = findViewById(R.id.role_layout);
        role_Edit = findViewById(R.id.role_Edit);
        name_layout = findViewById(R.id.name_layout);
        name_Edit = findViewById(R.id.name_Edit);

        email_layout = findViewById(R.id.email_layout);
        email_Edit = findViewById(R.id.email_Edit);

        password_layout = findViewById(R.id.password_layout);
        password_Edit = findViewById(R.id.password_Edit);

        birth_layout = findViewById(R.id.birth_layout);
        birth_Edit = findViewById(R.id.birth_Edit);

        registration = findViewById(R.id.registration);
        gotoLogin = findViewById(R.id.gotoLogin);


        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, Login.class));
                finish();
            }
        });
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput();
            }
        });

        birth_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBirthDialog();
            }
        });
        role_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRoleDialog();

            }
        });
    }

    String role;

    private void openRoleDialog() {
        String[] gender = {"I am looking for a job", "I am looking for employees"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.role_dialog, null);
        MaterialButton done, cancel;

        done = view.findViewById(R.id.okay);
        cancel = view.findViewById(R.id.cancel);

        NumberPicker picker = view.findViewById(R.id.numberpicker);
        picker.setMinValue(0);
        picker.setMaxValue(gender.length - 1);
        picker.setDisplayedValues(gender);
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        picker.setWrapSelectorWheel(false);
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                role = String.valueOf(newVal);
            }
        });
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (role == "" || role == null) {
                    role = "0";
                }
                role_Edit.setText(gender[Integer.parseInt(role)]);
                role_Edit.setTextColor(ColorStateList.valueOf(Color.BLACK));
                Toast.makeText(getBaseContext(), gender[Integer.parseInt(role)], Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        role = "";
    }

    @SuppressLint("SetTextI18n")
    private void openBirthDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                Calendar birthdate = Calendar.getInstance();
                birthdate.set(year, month, day);

                Calendar today = Calendar.getInstance();

                int age = today.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR);
                if (today.get(Calendar.MONTH) < birthdate.get(Calendar.MONTH)) {
                    age--;
                } else if (today.get(Calendar.MONTH) == birthdate.get(Calendar.MONTH)
                        && today.get(Calendar.DAY_OF_MONTH) < birthdate.get(Calendar.DAY_OF_MONTH)) {
                    age--;
                }

                if (age >= 18) {
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    String formatedDate = sdf.format(birthdate.getTime());
                    birth_Edit.setText(formatedDate);
                    birth_Edit.setTextColor(ColorStateList.valueOf(Color.BLACK));
                    dialog.dismiss();

                } else {

                    Toast.makeText(getBaseContext(), "Age restriction: 18 and above only", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void checkInput() {
        String name = name_Edit.getText().toString();
        String email = email_Edit.getText().toString();
        String pass = password_Edit.getText().toString();
        String birth = birth_Edit.getText().toString();
        String role = role_Edit.getText().toString();

        if (name.isEmpty()) {
            name_layout.setHelperText("*Please enter your name");
            name_layout.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            name_Edit.requestFocus();
            return;
        }

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
        if (birth.isEmpty()) {
            birth_layout.setHelperText("*Please set your birthday");
            birth_layout.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            birth_Edit.requestFocus();
            return;
        }

        if (role.isEmpty()) {
            role_layout.setHelperText("*Please set role");
            role_layout.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#F4511E")));
            role_Edit.requestFocus();
            return;
        }

        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                if (isNewUser) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                AuthCredential authCredential = EmailAuthProvider
                                        .getCredential(email, pass);
                                assert user != null;
                                user.reauthenticate(authCredential);
                                Map<String, Object> map = new HashMap<>();
                                map.put("id", user.getUid());
                                map.put("name", name);
                                map.put("birth", birth);
                                map.put("email", email);
                                map.put("pass", pass);
                                map.put("isAvailable", false);
                                if (role.equals("I am looking for a job"))
                                    map.put("role", "Employee");
                                else map.put("role", "Employer");

                                FirebaseFirestore.getInstance().collection("Users")
                                        .document(user.getUid()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    user.sendEmailVerification();
                                                    FirebaseAuth.getInstance().signOut();
                                                    Toast.makeText(Registration.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(Registration.this, Login.class));
                                                    finish();
                                                } else {
                                                    Toast.makeText(Registration.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    });
                } else {
                    Toast.makeText(Registration.this, "There is an account with this email. Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
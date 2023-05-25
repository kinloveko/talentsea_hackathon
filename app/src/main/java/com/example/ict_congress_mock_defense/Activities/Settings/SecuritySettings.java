package com.example.ict_congress_mock_defense.Activities.Settings;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ict_congress_mock_defense.Classes.CONSTANT;
import com.example.ict_congress_mock_defense.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecuritySettings extends AppCompatActivity {

    RelativeLayout email_layout;
    RelativeLayout password_layout;
    RelativeLayout phone_layout;
    RelativeLayout delete_layout;
    TextView email;
    TextView phoneUpdate;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(Color.parseColor("#e28743"));
        }

        //layout
        email_layout = findViewById(R.id.email_layout);
        password_layout = findViewById(R.id.password_layout);
        phone_layout = findViewById(R.id.phone_layout);
        delete_layout = findViewById(R.id.delete_layout);
        //textview to update
        email = findViewById(R.id.nameUpdate);
        phoneUpdate = findViewById(R.id.phoneUpdate);

        //retrieve data
        FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) return;
                        if (value.exists()) {
                            email.setText(value.getString("email"));
                            pass = value.getString("pass");
                            if (value.getString("phone") != null) {
                                phoneUpdate.setText(value.getString("phone"));
                            }
                            email_layout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openEmailDialog(value.getString("email"), value.getString("pass"));
                                }
                            });
                            password_layout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    changePassword(value.getString("email"), value.getString("pass"));
                                }
                            });
                            phone_layout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openPhoneDialog();
                                }
                            });
                        }
                    }
                });

        LinearLayout back = findViewById(R.id.backLayoutService);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecuritySettings.this.onBackPressed();
                finish();
            }
        });

    }

    private void openPhoneDialog() {

    }

    final Pattern passwordPattern = java.util.regex.Pattern.compile("^" +
            "(?=.*[@#$%^&!+=])" +     // at least 1 special character
            "(?=\\S+$)" +            // no white spaces
            ".{5,}" +                // at least 5 characters
            "$");

    @SuppressLint("SetTextI18n")
    private void changePassword(String email, String password) {
        AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
        View view2 = View.inflate(this, R.layout.dialog_password, null);
        builder3.setCancelable(false);
        //title

        //button
        Button cancel, okay;
        cancel = view2.findViewById(R.id.cancel);
        okay = view2.findViewById(R.id.okay);

        //EditText
        EditText editText = view2.findViewById(R.id.editText);


        AlertDialog alert3 = null;
        if (!isFinishing()) {
            builder3.setView(view2);
            alert3 = builder3.create();
            alert3.show();
            alert3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        AlertDialog finalAlert = alert3;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalAlert.dismiss();
            }
        });

        AlertDialog finalAlert1 = alert3;
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editText.getText().toString().equals("") || editText.getText().toString() == null) {
                    finalAlert1.dismiss();
                    Toast.makeText(SecuritySettings.this, "Oops. You made no entry! your password was expected", Toast.LENGTH_SHORT).show();
                } else if (!(editText.getText().toString().equals(password))) {
                    finalAlert1.dismiss();
                    Toast.makeText(SecuritySettings.this, "Oops. You entered password doesn't matched to this user", Toast.LENGTH_SHORT).show();
                } else {
                    finalAlert1.dismiss();
                    android.app.AlertDialog.Builder builder3 = new android.app.AlertDialog.Builder(SecuritySettings.this);
                    View view2 = View.inflate(SecuritySettings.this, R.layout.dialog_password_set, null);
                    builder3.setCancelable(false);
                    //title

                    Button cancel, okay;
                    //cancel button
                    cancel = view2.findViewById(R.id.cancel);
                    cancel.setText("Cancel");
                    //Okay button
                    okay = view2.findViewById(R.id.okay);

                    EditText editText = view2.findViewById(R.id.editText);

                    builder3.setView(view2);
                    android.app.AlertDialog alert = builder3.create();
                    alert.show();
                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalAlert1.dismiss();
                            alert.dismiss();
                        }
                    });
                    CharSequence passwordcs = editText.getText().toString();
                    okay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (editText.getText().toString().equals("") || editText.getText().toString() == null) {
                                count = 0;
                                Toast.makeText(SecuritySettings.this, "Oops. You made no entry! your password was expected", Toast.LENGTH_SHORT).show();
                            } else if (!Pattern.matches(passwordPattern.pattern(), passwordcs)) {
                                CONSTANT constant = new CONSTANT();
                                Toast.makeText(SecuritySettings.this, constant.passError, Toast.LENGTH_SHORT).show();
                            } else if (editText.getText().toString().equals(pass)) {
                                Toast.makeText(SecuritySettings.this, "Oops. You entered an old password", Toast.LENGTH_SHORT).show();
                            } else {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                AuthCredential credential = EmailAuthProvider.getCredential(email, password);

                                assert user != null;
                                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            user.updatePassword(editText.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                FirebaseFirestore.getInstance().collection("Users")
                                                                        .document(user.getUid())
                                                                        .update("pass", editText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    finalAlert1.dismiss();
                                                                                    alert.dismiss();
                                                                                    Toast.makeText(SecuritySettings.this, "Password Successfully Changed", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.d("myApp", e.getMessage());
                                                        }
                                                    });
                                        } else {
                                            alert.dismiss();
                                        }
                                    }
                                });

                            }
                        }
                    });

                }
            }
        });

    }

    int count = 0;
    final Pattern p = Pattern.compile("^" +
            "(?=.*[@#$%^&!+=])" +     // at least 1 special character
            "(?=\\S+$)" +            // no white spaces
            ".{5,}" +                // at least 5 characters
            "$");

    @SuppressLint("SetTextI18n")
    private void openEmailDialog(String emailCur, String pass) {

        AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
        View view2 = View.inflate(this, R.layout.dialog_password, null);
        builder3.setCancelable(false);
        //title

        Button cancel, okay;
        //cancel button
        cancel = view2.findViewById(R.id.cancel);
        cancel.setText("Cancel");
        //Okay button
        okay = view2.findViewById(R.id.okay);
        okay.setText("Enter");

        //EditText
        EditText editText = view2.findViewById(R.id.editText);

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
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                if (editText.getText().toString().equals("") || editText.getText().toString() == null) {
                    count = 0;
                    Toast.makeText(SecuritySettings.this, "Oops. You made no entry! your password was expected", Toast.LENGTH_SHORT).show();
                } else if (!(editText.getText().toString().equals(pass))) {
                    count = 1;
                    Toast.makeText(SecuritySettings.this, "Oops. You entered password doesn't matched to this user", Toast.LENGTH_SHORT).show();
                } else {
                    alert3.dismiss();

                    AlertDialog.Builder builder3 = new AlertDialog.Builder(SecuritySettings.this);
                    View view2 = View.inflate(SecuritySettings.this, R.layout.dialog_email, null);
                    builder3.setCancelable(false);
                    //title

                    //button
                    Button cancel, okay;
                    //cancel button
                    cancel = view2.findViewById(R.id.cancel);
                    cancel.setText("Cancel");
                    //Okay button
                    okay = view2.findViewById(R.id.okay);


                    //EditText
                    EditText editText = view2.findViewById(R.id.editText);

                    builder3.setView(view2);
                    AlertDialog alert4 = builder3.create();
                    alert4.show();
                    alert4.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert4.dismiss();
                        }
                    });

                    okay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Matcher m = p.matcher(editText.getText().toString());
                            if (editText.getText().toString().equals("") || editText.getText().toString() == null) {
                                count = 0;
                                Toast.makeText(SecuritySettings.this, "Oops. You made no entry! your email was expected", Toast.LENGTH_SHORT).show();
                            } else if (editText.getText().toString().equals(email)) {
                                count = 1;
                                Toast.makeText(SecuritySettings.this, "You entered same value to your existing email", Toast.LENGTH_SHORT).show();
                            } else if (!(m.matches())) {
                                Toast.makeText(SecuritySettings.this, "Oops. Enter a valid email ex. example@gmail.com", Toast.LENGTH_SHORT).show();
                                count = 0;
                            } else {
                                FirebaseAuth.getInstance().getCurrentUser().updateEmail(editText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            count = 1;
                                            email.setText(editText.getText().toString());

                                            // Save old and new email in Firestore @security --> security_doc
                                            Map<String, Object> data = new HashMap<>();
                                            data.put("old_email", emailCur);
                                            data.put("email", editText.getText().toString());
                                            FirebaseFirestore.getInstance().collection("Users")
                                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .set(data, SetOptions.merge());

                                            FirebaseAuth.getInstance().getCurrentUser().updateEmail(editText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        count = 1;
                                                        email.setText(editText.getText().toString());

                                                        // Save old and new email in Firestore @security --> security_doc
                                                        Map<String, Object> data = new HashMap<>();
                                                        data.put("old_email", emailCur);
                                                        data.put("email", editText.getText().toString());
                                                        FirebaseFirestore.getInstance().collection("Users")
                                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                .set(data, SetOptions.merge());

                                                        // Send email verification
                                                        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    // Verification email sent successfully
                                                                    alert4.dismiss();
                                                                    AlertDialog.Builder builder3 = new AlertDialog.Builder(SecuritySettings.this);
                                                                    View view2 = View.inflate(SecuritySettings.this, R.layout.dialog_email_verify, null);
                                                                    builder3.setCancelable(false);
                                                                    // Title
                                                                    Button cancel, okay;
                                                                    // Cancel button
                                                                    okay = view2.findViewById(R.id.okay);
                                                                    cancel = view2.findViewById(R.id.cancel);
                                                                    cancel.setText("Cancel");
                                                                    builder3.setView(view2);
                                                                    AlertDialog alert3 = builder3.create();
                                                                    alert3.show();
                                                                    alert3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                                                    // Add auth state listener
                                                                    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
                                                                        @Override
                                                                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                                                            if (firebaseAuth.getCurrentUser() != null) {
                                                                                firebaseAuth.getCurrentUser().reload(); // Reload user data
                                                                                if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                                                                    // User's email is verified
                                                                                    count = 1;
                                                                                    alert4.dismiss();
                                                                                    alert3.dismiss();
                                                                                    FirebaseAuth.getInstance().getCurrentUser().updateEmail(editText.getText().toString());
                                                                                    Toast.makeText(SecuritySettings.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                                                                } else {
                                                                                    // User's email is not verified
                                                                                    Toast.makeText(SecuritySettings.this, "Still not verified", Toast.LENGTH_SHORT).show();
                                                                                    count = 0;
                                                                                }
                                                                            }
                                                                        }
                                                                    };

                                                                    FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
                                                                    cancel.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            alert4.dismiss();
                                                                            alert3.dismiss();

                                                                            //getting the old email
                                                                            FirebaseFirestore.getInstance()
                                                                                    .collection("Users")
                                                                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                            if (task.isSuccessful()) {

                                                                                                DocumentSnapshot value = task.getResult();
                                                                                                String oldEmail = value.getString("old_email");
                                                                                                Map<String, Object> data = new HashMap<>();
                                                                                                data.put("email", oldEmail);
                                                                                                data.put("old_email", "");
                                                                                                FirebaseAuth.getInstance().getCurrentUser()
                                                                                                        .updateEmail(oldEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                //storing in fireStore
                                                                                                                FirebaseAuth.getInstance().getCurrentUser();
                                                                                                                FirebaseFirestore
                                                                                                                        .getInstance()
                                                                                                                        .collection("Users")
                                                                                                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                                                                        .set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                            @Override
                                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                if (task.isSuccessful()) {


                                                                                                                                }
                                                                                                                            }
                                                                                                                        });
                                                                                                                Toast.makeText(SecuritySettings.this, "No changes, cancel button clicked.", Toast.LENGTH_LONG).show();
                                                                                                            }
                                                                                                        });
                                                                                            }
                                                                                        }
                                                                                    });
                                                                        }
                                                                    });
                                                                    okay.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            // Automatically close the dialog
                                                                            alert3.dismiss();
                                                                            FirebaseAuth.getInstance().getCurrentUser().updateEmail(editText.getText().toString());
                                                                            Toast.makeText(SecuritySettings.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                                                            FirebaseAuth.getInstance().removeAuthStateListener(authStateListener); // Remove auth state listener
                                                                        }
                                                                    });

                                                                    if (count != 0) {
                                                                        alert4.dismiss();
                                                                    }
                                                                } else {
                                                                    // Failed to send verification email
                                                                    Toast.makeText(SecuritySettings.this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    alert4.dismiss();
                                                    Toast.makeText(SecuritySettings.this, "Please try again, or go Sign out and Sign in again. Then you can continue changing your email", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        alert4.dismiss();
                                        Toast.makeText(SecuritySettings.this, "Please try again, or go Sign out and Sign in again. Then you can continue changing your email", Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                            if (count != 0) {
                                alert4.dismiss();
                            }
                        }

                    });
                }

                if (count != 0) {
                    alert3.dismiss();
                }
            }
        });
    }

}
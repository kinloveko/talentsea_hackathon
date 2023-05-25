package com.example.ict_congress_mock_defense.Activities;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ict_congress_mock_defense.Adapter.adapter_skills;
import com.example.ict_congress_mock_defense.Classes.Skills;
import com.example.ict_congress_mock_defense.Classes.skills_job_class;
import com.example.ict_congress_mock_defense.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Skills_activity extends AppCompatActivity {

    LinearLayout job_description,backLayoutService;
    TextView job_description_text;

    LinearLayout job_experience;
    TextView job_experience_text;

    LinearLayout job_skill_set;
    RecyclerView recyclerView;

    LinearLayout job_range;
    TextView salary_range;
    adapter_skills adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skills);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(Color.parseColor("#e28743"));
        }
        job_description = findViewById(R.id.job_description);
        job_description_text = findViewById(R.id.job_description_text);

        job_experience = findViewById(R.id.job_experience);
        job_experience_text = findViewById(R.id.job_experience_text);
        backLayoutService = findViewById(R.id.backLayoutService);
        backLayoutService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Skills_activity.this.onBackPressed();
                finish();
            }
        });
        job_skill_set = findViewById(R.id.job_skill_set);
        recyclerView = findViewById(R.id.recyclerView);

        job_range = findViewById(R.id.job_range);
        salary_range = findViewById(R.id.salary_range);

        job_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDescription();
            }
        });

        job_experience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExp();
            }
        });

        job_skill_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSkill();
            }
        });

        job_range.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRange();
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new adapter_skills(this, FirebaseAuth.getInstance().getCurrentUser().getUid());
        recyclerView.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) return;

                        if (value.exists()) {
                            if (value.getString("job_intro") != null &&
                                    value.getString("job_exp") != null &&
                                    value.getString("job_salary") != null &&
                                    value.get("job_skill_set")!=null) {
                                FirebaseFirestore.getInstance().collection("Users")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .update("isAvailable",true);
                            }
                            else{
                                FirebaseFirestore.getInstance().collection("Users")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .update("isAvailable",false);
                            }

                            if (value.getString("job_intro") != null) {
                                job_description_text.setText(value.getString("job_intro"));
                            }
                            if (value.getString("job_exp") != null) {
                                job_experience_text.setText(value.getString("job_exp"));
                            }
                            if (value.getString("job_salary") != null) {
                                salary_range.setText(value.getString("job_salary"));
                            }
                            if (value.get("job_skill_set") != null) {
                                adapter.clear();
                                List<String> job = (List<String>) value.get("job_skill_set");

                                for (String i : job) {
                                    Skills sk = new Skills(i);
                                    adapter.addSkills(sk);
                                }
                            }
                        }

                    }
                });
    }

    int counter = 0;

    @SuppressLint("SetTextI18n")
    private void openRange() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_job_skills, null);
        Button done, cancel;
        TextView title, message;
        EditText text;
        title = view.findViewById(R.id.title);
        message = view.findViewById(R.id.message);
        text = view.findViewById(R.id.editText);
        done = view.findViewById(R.id.okay);
        cancel = view.findViewById(R.id.cancel);
        title.setText("Expected salary");
        message.setText("Tell us your expected salary");
        text.setHint("Enter your preferred salary ranged . . .");
        builder.setView(view);
        AlertDialog alert = builder.create();
        alert.show();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getText().toString() != null || text.getText().toString().equals("")) {
                    salary_range.setText(text.getText().toString());
                    Toast.makeText(getApplicationContext(), text.getText().toString(), Toast.LENGTH_SHORT).show();
                    UpdateInfo("job_salary", text.getText().toString());
                    counter = 1;
                } else {
                    Toast.makeText(Skills_activity.this, "Cannot save an empty salary range", Toast.LENGTH_SHORT).show();
                    counter = 0;
                }

                if (counter != 0) {
                    alert.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot make any changes", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
    }

    private void UpdateInfo(String field, String value) {
        FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .update(field, value).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Skills_activity.this, "Successfully Saved", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    ArrayAdapter<String> arrayAdapter;
    ListView listViews;

    private void openSkill() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_job_skills_set, null);
        skills_job_class job = new skills_job_class();
        Button done;
        EditText text;
        text = view.findViewById(R.id.editText);
        done = view.findViewById(R.id.okay);
        listViews = view.findViewById(R.id.skill_category);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, job.breed);
        listViews.setAdapter(arrayAdapter);
        builder.setView(view);
        AlertDialog alert = builder.create();
        alert.show();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (text.getText() != null || text.getText().equals("")) {
            text.addTextChangedListener(filterTextWatcher);
        }
        if (listViews == null || listViews.getCount() == 0) {
            listViews.setVisibility(View.GONE);
            listViews.setCacheColorHint(Color.TRANSPARENT);
            listViews.setBackground(new ColorDrawable(Color.TRANSPARENT));
            listViews.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        } else {
            listViews.setBackground(getDrawable(R.drawable.shape));
            listViews.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            listViews.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = listViews.getLayoutParams();
            params.height = 400;
            listViews.setLayoutParams(params);
            listViews.requestLayout();
        }

        listViews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String holder = listViews.getItemAtPosition(position).toString();
                text.setText(holder);

                Toast.makeText(Skills_activity.this, holder, Toast.LENGTH_SHORT).show();

            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getText().toString() == null || text.getText().toString().equals("")) {
                    counter = 0;
                    Toast.makeText(getApplicationContext(), "Cannot save an empty skill", Toast.LENGTH_SHORT).show();
                } else {
                    job_description_text.setText(text.getText().toString());
                    Toast.makeText(getApplicationContext(), text.getText().toString(), Toast.LENGTH_SHORT).show();
                    UpdateArray(text.getText().toString(), "job_skill_set");
                    counter = 1;
                }
                if (counter != 0) {
                    alert.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot make any changes", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void UpdateArray(String job_skill_set, String holder) {

        FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) return;
                        if (value.exists()) {
                            if (value.get("job_skill_set") != null) {

                                FirebaseFirestore.getInstance().collection("Users")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .update("job_skill_set", FieldValue.arrayUnion(job_skill_set))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(Skills_activity.this, "Successfully added", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            } else {

                                Map<String, Object> map = new HashMap<>();
                                List<String> skills = new ArrayList<>();
                                skills.add(job_skill_set);
                                map.put(holder, skills);
                                FirebaseFirestore.getInstance().collection("Users")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .set(map, SetOptions.merge())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(Skills_activity.this, "Successfully added", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void openExp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_job_skills, null);
        TextView title, message;
        title = view.findViewById(R.id.title);
        message = view.findViewById(R.id.message);
        title.setText("Job Experience");
        message.setText("Tell us about your job experience");
        Button done, cancel;
        EditText text;
        text = view.findViewById(R.id.editText);
        done = view.findViewById(R.id.okay);
        cancel = view.findViewById(R.id.cancel);

        builder.setView(view);
        AlertDialog alert = builder.create();
        alert.show();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getText().toString() == null || text.getText().toString().equals("")) {
                    counter = 0;
                    Toast.makeText(getApplicationContext(), "Cannot save an empty job experience", Toast.LENGTH_SHORT).show();
                } else {
                    job_experience_text.setText(text.getText().toString());
                    Toast.makeText(getApplicationContext(), text.getText().toString(), Toast.LENGTH_SHORT).show();
                    UpdateInfo("job_exp", text.getText().toString());
                    counter = 1;
                }
                if (counter != 0) {
                    alert.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot make any changes", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void openDescription() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_job_skills, null);

        Button done, cancel;
        EditText text;
        text = view.findViewById(R.id.editText);
        text.setHint("Describe your experience here . . .");
        done = view.findViewById(R.id.okay);
        cancel = view.findViewById(R.id.cancel);


        builder.setView(view);
        AlertDialog alert = builder.create();
        alert.show();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getText().toString() == null || text.getText().toString().equals("")) {
                    counter = 0;
                    Toast.makeText(getApplicationContext(), "Cannot save an empty job introduction", Toast.LENGTH_SHORT).show();
                } else {
                    job_description_text.setText(text.getText().toString());
                    Toast.makeText(getApplicationContext(), text.getText().toString(), Toast.LENGTH_SHORT).show();
                    UpdateInfo("job_intro", text.getText().toString());
                    counter = 1;
                }
                if (counter != 0) {
                    alert.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot make any changes", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });


    }

    private final TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

            arrayAdapter.getFilter().filter(s);

        }
    };
}
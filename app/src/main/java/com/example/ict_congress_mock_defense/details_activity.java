package com.example.ict_congress_mock_defense;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ict_congress_mock_defense.Classes.companyClass;
import com.example.ict_congress_mock_defense.Classes.employeeClass;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class details_activity extends AppCompatActivity {
    TextView details_service_schedule;
    TextView details_service_availability;
    TextView details_service_address;
    LinearLayout backLayoutService;
    TextView details_pet_description;
    Button details_button_hireNow;
    TextView details_shooter_price;
    ImageView userImage;
    TextView details_service_type;
    TextView details_shooter_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(Color.parseColor("#e28743"));
        }
        details_button_hireNow= findViewById(R.id.details_button_hireNow);
        backLayoutService = findViewById(R.id.backLayoutService);
        backLayoutService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details_activity.this.onBackPressed();
                finish();
            }
        });
        details_service_schedule = findViewById(R.id.details_service_schedule);
        details_service_availability = findViewById(R.id.details_service_availability);
        details_service_address = findViewById(R.id.details_service_address);
        details_pet_description = findViewById(R.id.details_pet_description);
        details_shooter_price = findViewById(R.id.details_shooter_price);
        details_service_type = findViewById(R.id.details_service_type);
        details_shooter_name = findViewById(R.id.details_shooter_name);
        userImage = findViewById(R.id.userImage);
        Intent i = getIntent();
        String from = i.getStringExtra("from");
        if(from.equals("Employer")){
            companyClass company = (companyClass) i.getSerializableExtra("userName");
            if(company!=null) {
                details_button_hireNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new  Intent(details_activity.this,message_conversation_activity.class);
                        i.putExtra("image",company.getImage());
                        i.putExtra("name",company.getCompany());
                        startActivity(i);
                    }
                });
                if (company.getImage() != null)
                    Picasso.get().load(company.getImage()).placeholder(R.drawable.icon_noimage).into(userImage);

                details_shooter_name.setText(company.getCompany());
                if(company.getIsLookingFor()!=null)
                    details_service_type.setText(company.getIsLookingFor().get(0));
                details_shooter_price.setText(company.getSalaryRange());
                details_pet_description.setText(company.getDepartment());
                if(company.getAvailable() !=null && company.getAvailable().equals(true)){
                    details_service_availability.setText("true");
                }
                else{
                    details_service_availability.setText("false");
                }
            }
        }
        else{
            employeeClass company = (employeeClass) i.getSerializableExtra("userName");
            if(company!=null) {
                details_button_hireNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new  Intent(details_activity.this,message_conversation_activity.class);
                        i.putExtra("image",company.getImage());
                        i.putExtra("name",company.getName());
                        startActivity(i);
                    }
                });
                if (company.getImage() != null)
                    Picasso.get().load(company.getImage()).placeholder(R.drawable.icon_noimage).into(userImage);

                details_shooter_name.setText(company.getName());
                if(company.getJob_skill_set()!=null)
                    details_service_type.setText(company.getJob_skill_set().get(0));
                details_shooter_price.setText(company.getJob_salary());
                details_pet_description.setText(company.getJob_intro());
                if(company.getAvailable() !=null && company.getAvailable().equals(true)){
                    details_service_availability.setText("true");
                }
                else{
                    details_service_availability.setText("false");
                }
            }
        }

    }
}
package com.example.ict_congress_mock_defense.Activities.Settings;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.ict_congress_mock_defense.Activities.Skills_activity;
import com.example.ict_congress_mock_defense.R;
import com.example.ict_congress_mock_defense.LoginAndRegistration.recentlyLogged;

public class AccountSettings extends AppCompatActivity {
    RelativeLayout first_layout,report_layout,password_layout,personal_details_layout;
    LinearLayout backLayoutService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(Color.parseColor("#e28743"));
        }
        personal_details_layout= findViewById(R.id.personal_details_layout);
        backLayoutService= findViewById(R.id.backLayoutService);
        backLayoutService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountSettings.this.onBackPressed();
                finish();
            }
        });
        password_layout = findViewById(R.id.password_layout);
        first_layout = findViewById(R.id.first_layout);
        report_layout = findViewById(R.id.report_layout);
        first_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountSettings.this, recentlyLogged.class));
            }
        });
        personal_details_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountSettings.this, Skills_activity.class));
            }
        });
        password_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountSettings.this, SecuritySettings.class));
            }
        });
        report_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountSettings.this, EditProfileSettings.class));
            }
        });
    }
}
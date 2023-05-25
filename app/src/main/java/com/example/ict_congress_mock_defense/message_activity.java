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
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class message_activity extends AppCompatActivity {

    ImageView backArrowImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_actvity);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));

        backArrowImage =findViewById(R.id.backArrowImage);
        backArrowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message_activity.this.onBackPressed();
                finish();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(Color.parseColor("#e28743"));
        }
        Intent i = getIntent();
        String image = i.getStringExtra("image");
        if(image!=null){
            CircleImageView profileImage = findViewById(R.id.profileImage);
            Picasso.get().load(image).placeholder(R.drawable.icon_noimage).into(profileImage);
        }
    }

}
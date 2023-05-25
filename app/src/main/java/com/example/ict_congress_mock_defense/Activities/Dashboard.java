package com.example.ict_congress_mock_defense.Activities;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.ict_congress_mock_defense.Activities.Navigations.nav_home_fragment;
import com.example.ict_congress_mock_defense.Activities.Navigations.nav_notification_fragment;
import com.example.ict_congress_mock_defense.Activities.Navigations.nav_profile_fragment;
import com.example.ict_congress_mock_defense.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayDeque;
import java.util.Deque;

public class Dashboard extends AppCompatActivity {

    BottomNavigationView bottom_nav;
    Deque<Integer> IntegerDeque = new ArrayDeque<>(3);
    boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(Color.parseColor("#e28743"));
        }
        bottom_nav = findViewById(R.id.bottom_nav);
        loadFragment(BreederGetFragment(R.id.home));
        bottom_nav.setSelectedItemId(R.id.home);
        display();
    }

    private void display() {
        IntegerDeque.push(R.id.home);
        bottom_nav.setSelectedItemId(R.id.home);
        bottom_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //get selected item id
                int id = item.getItemId();
                //check condition
                if (IntegerDeque.contains(id)) {
                    //when deque list contains selected id
                    //check condition
                    if (id == R.id.home) {
                        //if id is equal to home fragment or tab 1
                        //check condition
                        if (IntegerDeque.size() != 1) {
                            //if deque list size is not equal to 1
                            //check condition
                            if (flag) {
                                IntegerDeque.addFirst(R.id.home);
                                //set flag to true
                                flag = false;
                            }
                        }
                    }
                    //Remove selected id from deque list
                    IntegerDeque.remove(id);
                }
                //push selected id in deque list
                IntegerDeque.push(id);
                //load fragment
                loadFragment(BreederGetFragment(item.getItemId()));
                return true;
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    private Fragment BreederGetFragment(int itemId) {
        switch (itemId) {
            case R.id.home:
                //set checked dashboard fragment
                bottom_nav.getMenu().getItem(0).setChecked(true);
                //return dashboard fragment
                return new nav_home_fragment();
            case R.id.notification:
                //set checked dashboard fragment
                bottom_nav.getMenu().getItem(1).setChecked(true);
                //return dashboard fragment
                return new nav_notification_fragment();
            case R.id.profile:
                //set checked dashboard fragment
                bottom_nav.getMenu().getItem(2).setChecked(true);
                //return cart_fragment
                return new nav_profile_fragment();
        }
        bottom_nav.getMenu().getItem(0).setChecked(true);
        return new nav_home_fragment();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        IntegerDeque.pop();
        //check condition

        if (!IntegerDeque.isEmpty()) {
            //Pop to previous fragment
            //When deque list is not empty
            //load Fragment
            loadFragment(BreederGetFragment(IntegerDeque.peek()));
        } else {
            //when deque list is empty
            //finish activity
            finish();
        }
    }
}
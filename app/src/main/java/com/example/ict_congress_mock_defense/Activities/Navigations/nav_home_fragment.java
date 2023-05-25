package com.example.ict_congress_mock_defense.Activities.Navigations;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ict_congress_mock_defense.Activities.Skills_activity;
import com.example.ict_congress_mock_defense.Adapter.employee_potential_company_adapter;
import com.example.ict_congress_mock_defense.Adapter.employeer_potential_employee_adapter;
import com.example.ict_congress_mock_defense.Classes.companyClass;
import com.example.ict_congress_mock_defense.Classes.employeeClass;
import com.example.ict_congress_mock_defense.R;
import com.example.ict_congress_mock_defense.message_activity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class nav_home_fragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    CardView searchcarddView;
    CardView profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nav_home, container, false);
    }

    RecyclerView recyclerView;
    employee_potential_company_adapter adapter;
    employeer_potential_employee_adapter adapter_employee;
    CircleImageView profileImage;
    TextView textWarning;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileImage = view.findViewById(R.id.profileImage);
        textWarning = view.findViewById(R.id.textWarning);
        searchcarddView = view.findViewById(R.id.searchcarddView);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        adapter = new employee_potential_company_adapter(getContext(), FirebaseAuth.getInstance().getCurrentUser().getUid());
        adapter_employee = new employeer_potential_employee_adapter(getContext(), FirebaseAuth.getInstance().getCurrentUser().getUid());
        textWarning.setSelected(true);
        FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) return;
                        if (value.exists()) {
                            if (value.getString("role").equals("Employer")) {
                                getEmployees();
                            } else {
                                getCompany();
                            }
                            searchcarddView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(getContext(), message_activity.class);
                                    if (value.getString("image") != null)
                                        i.putExtra("image", value.getString("image"));
                                    startActivity(i);
                                }
                            });

                            if (value.get("isAvailable") != null) {
                                boolean isTrue = (boolean) value.get("isAvailable");
                                if (isTrue) {
                                    textWarning.setVisibility(View.GONE);
                                } else {
                                    textWarning.setVisibility(View.VISIBLE);
                                    textWarning.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(new Intent(getContext(), Skills_activity.class));
                                        }
                                    });
                                }
                            }
                            Picasso.get().load(value.getString("image")).placeholder(R.drawable.icon_noimage).into(profileImage);
                        }
                    }
                });
        profile = view.findViewById(R.id.cardView);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, new nav_profile_fragment())
                        .addToBackStack("name")
                        .commit();
                BottomNavigationView bottom_nav = getActivity().findViewById(R.id.bottom_nav);
                bottom_nav.getMenu().getItem(2).setChecked(true);
                nav_profile_fragment profile_fragment = new nav_profile_fragment();
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment, profile_fragment);
                fr.addToBackStack("name");
                fr.commit();
            }
        });
    }

    private void getEmployees() {

        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot values, @Nullable FirebaseFirestoreException error) {
                        if (values.exists()) {


                            FirebaseFirestore.getInstance().collection("Users")
                                    .whereEqualTo("isAvailable", true)
                                    .whereNotEqualTo("role", "Employer").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if (error != null) return;
                                            if (!value.isEmpty()) {
                                                adapter_employee.clear();
                                                for (DocumentSnapshot s : value) {
                                                    employeeClass companyClass = s.toObject(employeeClass.class);

                                                            adapter_employee.addSkills(companyClass);

                                                    }
                                                }
                                                recyclerView.setAdapter(adapter_employee);
                                        }
                                    });
                        }
                    }
                });
    }

    private void getCompany() {
        FirebaseFirestore.getInstance().collection("Users")
                .whereEqualTo("isAvailable", true)
                .whereNotEqualTo("role", "Employee").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) return;
                        if (!value.isEmpty()) {
                            adapter.clear();
                            for (DocumentSnapshot s : value) {
                                companyClass companyClass = s.toObject(companyClass.class);
                                adapter.addSkills(companyClass);
                            }
                            recyclerView.setAdapter(adapter);
                        }

                    }
                });
    }
}
package com.example.ict_congress_mock_defense.Activities.Navigations;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ict_congress_mock_defense.Activities.Settings.AccountSettings;
import com.example.ict_congress_mock_defense.LoginAndRegistration.Login;
import com.example.ict_congress_mock_defense.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class nav_profile_fragment extends Fragment {
    RelativeLayout logout_layout, first_layout;
    LinearLayout backLayoutService;
    TextView usernameTextView;
    TextView emailTextView;

    CircleImageView profileImage;
    CardView cardView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nav_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameTextView = view.findViewById(R.id.usernameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        profileImage = view.findViewById(R.id.profileImage);
        cardView = view.findViewById(R.id.cardView);

        FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) return;
                        if (value.exists()) {
                            if(value.getString("image")!=null)
                                Picasso.get().load(value.getString("image")).placeholder(R.drawable.icon_noimage)
                                                .into(profileImage);
                            usernameTextView.setText(value.getString("name"));
                            emailTextView.setText(value.getString("email"));

                        }
                    }
                });

        first_layout = view.findViewById(R.id.first_layout);
        logout_layout = view.findViewById(R.id.logout_layout);
        backLayoutService = view.findViewById(R.id.backLayoutService);
        backLayoutService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, new nav_home_fragment())
                        .commit();
                BottomNavigationView bottom_nav = getActivity().findViewById(R.id.bottom_nav);
                bottom_nav.getMenu().getItem(0).setChecked(true);
                nav_home_fragment profile_fragment = new nav_home_fragment();
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment, profile_fragment);
                fr.addToBackStack("name");
                fr.commit();
            }
        });
        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getContext(), "Successfully Logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), Login.class));
                getActivity().finish();
            }
        });
        first_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getContext(), AccountSettings.class));
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    profilePermission();
                } else {
                    requestPermission();
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted, request it
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                30);
                    }
                }
            }
        });
    }

    private void profilePermission() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int readCheck = ContextCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE);
            int writeCheck = ContextCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE);
            return readCheck == PackageManager.PERMISSION_GRANTED && writeCheck == PackageManager.PERMISSION_GRANTED;
        }
    }

    private final String[] permissions = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission")
                    .setMessage("Please give the Storage permission")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                intent.addCategory("android.intent.category.DEFAULT");
                                intent.setData(Uri.parse(String.format("package:%s", new Object[]{getActivity().getPackageName()})));
                                startActivity(intent);
                            } catch (Exception e) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                startActivity(intent);
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setCancelable(false)
                    .show();
        } else {

            ActivityCompat.requestPermissions(nav_profile_fragment.this.getActivity(), permissions, 30);

        }
    }
    Uri profile_uri;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 100) {
            Uri imageUri = data.getData();
            cropImage(imageUri, profileImage, "user");
        }
        else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP && types.equals("user")) {
            Uri croppedUri = UCrop.getOutput(data);
            // Use the cropped image URI as needed
            if (croppedUri != null) {
                if (currentImageView != null && types != null) {
                    profile_uri = croppedUri;
                    Picasso.get()
                            .load(croppedUri)
                            .placeholder(R.drawable.icon_noimage)
                            .into(profileImage);
                    saveToStorage(profile_uri.toString());
                }
            }
        }
    }

    private void saveToStorage(String toString) {
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();
        StorageReference reference = FirebaseStorage.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profile");

        reference.child("profile_pic").putFile(Uri.parse(toString),metadata).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    reference.child("profile_pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map<String,Object> map = new HashMap<>();
                            map.put("image",uri.toString());
                            FirebaseFirestore.getInstance().collection("Users")
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
                }
            }
        });
    }


    private ImageView currentImageView;
    private String types;

    private void cropImage(Uri sourceUri, ImageView imageView, String type) {
        currentImageView = imageView;
        types = type;

        Uri destinationUri = Uri.fromFile(new File(getContext().getCacheDir(), "cropped"));
        UCrop uCrop = UCrop.of(sourceUri, destinationUri);
        uCrop.start(getContext(), this);

    }
}
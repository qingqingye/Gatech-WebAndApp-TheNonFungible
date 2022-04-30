package com.example.thenonfungible.View.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.thenonfungible.Model.Avatar;
import com.example.thenonfungible.Model.Good;
import com.example.thenonfungible.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        // Grab database reference
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Reference for clothing, pants, and shoes image button
        ImageButton clothing = (ImageButton) view.findViewById(R.id.clothing);
        ImageButton pants = (ImageButton) view.findViewById(R.id.pants);
        ImageButton shoes = (ImageButton) view.findViewById(R.id.shoes);

        // Load image from database to ImageButton
        DatabaseReference avatarReference = database.getReference().child("avatars");
        final ValueEventListener avatarsDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> avatarsChildren = snapshot.getChildren();

                for (DataSnapshot avatar : avatarsChildren) {
                    String userId = avatar.getKey();
                    if (userId.equals(mAuth.getCurrentUser().getUid())) {
                        Avatar a = avatar.getValue(Avatar.class);
                        String clothingImageId = a.getClothing().getItemImageID();
                        String pantsImageId = a.getPants().getItemImageID();
                        String shoesImageId = a.getShoes().getItemImageID();
                        Glide.with(getActivity()).load(clothingImageId).into(clothing);
                        Glide.with(getActivity()).load(pantsImageId).into(pants);
                        Glide.with(getActivity()).load(shoesImageId).into(shoes);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error Loading Avatars", Toast.LENGTH_SHORT).show();
            }
        };
        avatarReference.addListenerForSingleValueEvent(avatarsDataListener);

//        clothing.setOnClickListener();
//        pants.setOnClickListener();
//        shoes.setOnClickListener();



        return view;


    }
}
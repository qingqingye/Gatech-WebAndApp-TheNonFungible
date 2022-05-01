package com.example.thenonfungible.View;

import android.os.Bundle;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thenonfungible.Model.Good;
import com.example.thenonfungible.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClothingActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    List<Good> clothing = new ArrayList<>();
    private ClothingAdapter clothingAdapter = new ClothingAdapter(this, clothing);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothing);

        GridView gridView = (GridView) findViewById(R.id.clothingGrid);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Get goods reference
        DatabaseReference goodsReference = database.getReference().child("goods");

        final ValueEventListener goodsDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> goodsChildren = snapshot.getChildren();

                clothing.clear();

                for (DataSnapshot good : goodsChildren) {
                    Good goodItem = good.getValue(Good.class);
                    if (goodItem.getOwnerId().equals(mAuth.getCurrentUser().getUid()) && goodItem.getItemType().equals("Clothing")) {
                        clothing.add(goodItem);
                    }
                }

                clothingAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        goodsReference.addListenerForSingleValueEvent(goodsDataListener);
        gridView.setAdapter(clothingAdapter);
    }
}

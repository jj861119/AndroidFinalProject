package com.example.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MemberDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail);


        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        //int imageID = intent.getIntExtra("imageID",R.drawable.baishatunbeach1);
        String imageURL = intent.getStringExtra("imageURL");
        TextView nameText = findViewById(R.id.detail_name);
        TextView descriptionText = findViewById(R.id.detail_description);
        ImageView image = findViewById(R.id.detail_image);
        nameText.setText(name);
        descriptionText.setText(description);

        Glide.with(image.getContext())
                .load(imageURL)
                .into(image);

        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
        image.getLayoutParams().height = dimensionInDp;
        //image.setImageResource(imageID);
    }
}

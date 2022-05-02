package com.example.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class QuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);


        Button one = (Button) findViewById(R.id.oneAns);
        one.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                ImageView image = (ImageView) findViewById(R.id.anserImageID);
                int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
                image.getLayoutParams().height = dimensionInDp;
                image.setVisibility(View.VISIBLE);
                image.setImageResource(R.drawable.wrong);
            }
        });
        Button two = (Button) findViewById(R.id.twoAns);
        two.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                ImageView image = (ImageView) findViewById(R.id.anserImageID);
                int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
                image.getLayoutParams().height = dimensionInDp;
                image.setVisibility(View.VISIBLE);
                image.setImageResource(R.drawable.correct);
            }
        });


        Button finishAnswer = (Button) findViewById(R.id.finishAnswer);
        finishAnswer.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(QuestionActivity.this  , testFirebase.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
}

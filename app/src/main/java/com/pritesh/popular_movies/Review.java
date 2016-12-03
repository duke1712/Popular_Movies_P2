package com.pritesh.popular_movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Review extends AppCompatActivity {
    TextView author;
    TextView review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        author=(TextView)findViewById(R.id.author);
        review=(TextView)findViewById(R.id.reviewBox);

        Intent intent=getIntent();
        author.setText("Review By: "+intent.getStringExtra("AUTHOR"));
        review.setText(intent.getStringExtra("CONTENT"));

    }
}

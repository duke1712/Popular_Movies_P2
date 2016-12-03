package com.pritesh.popular_movies;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Details_Activity extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Bundle arguments = new Bundle();
            int i=getIntent().getIntExtra("POSITION",-1);
            arguments.putString("POSITION",String.valueOf(i));
            if(getIntent().getFlags()==1)
            arguments.putBoolean("FLAG",true);
            else
            {
                arguments.putBoolean("FLAG",false);
            }
            arguments.putString("id",getIntent().getStringExtra("id"));
            Main fragment = new Main();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }
}

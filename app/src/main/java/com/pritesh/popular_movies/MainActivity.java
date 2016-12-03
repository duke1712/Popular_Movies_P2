package com.pritesh.popular_movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity{
    static boolean mTwoPane;
    @Override
    protected void onNewIntent(Intent intent) {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new Main())
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }
    @Override
    protected void onResume() {
       super.onResume();
      //  menu m=(menu) getSupportFragmentManager().findFragmentById(R.id.fragment);
    }

//    @Override
//    public void up() {
//        menu m=(menu) getSupportFragmentManager().findFragmentById(R.id.fragment);
//        // m.notif();
//        m.onResume();
//
//    }
}

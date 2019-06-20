package com.metronome;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity {
   private Fragment metronomeFragment;
   private Fragment tunerFragment;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_recordfile:
                    return true;
                case R.id.navigation_metronome:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,metronomeFragment).commit();
                    return true;
                case R.id.navigation_tuner:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,tunerFragment).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        metronomeFragment = new MetronomeFragment();
        tunerFragment = new TunerFragment();

        if(savedInstanceState!=null) return;

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,metronomeFragment).commit();

    }

}

package com.metronome;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import android.view.MenuItem;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
   private Fragment fragment = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_recordfile:
                    return true;
                case R.id.navigation_metronome:
                    return true;
                case R.id.navigation_tuner:
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

        if(savedInstanceState!=null) return;
        Fragment initFragment = new MetronomeFragment();
        initFragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,initFragment).commit();

    }

}

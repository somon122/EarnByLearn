package com.example.pta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.pta.fragments.ProfileFragment;
import com.example.pta.fragments.HomeFragment;
import com.example.pta.liveExam.ExamActivity;
import com.example.pta.result.ResultFragment;
import com.example.pta.topPlayerList.TopPlayerListFragment;
import com.example.pta.wallet.WalletActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        if (saveUserInfo.checkUser()){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                startActivity(new Intent(MainActivity.this, ProfileSetUpActivity.class));

            }else {
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
            }
        }

    }

    BottomNavigationView bottomNavigationView;
    SaveUserInfo saveUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.mainToolBar);
        setSupportActionBar(toolbar);
      /*  getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.wallet);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);*/



        saveUserInfo = new SaveUserInfo(this);
        HomeFragment homeFragment = new HomeFragment();
        fragmentSet(homeFragment);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottomHome_id);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.bottomHome_id:
                        HomeFragment homeFragment = new HomeFragment();
                        fragmentSet(homeFragment);
                        return true;

                    case R.id.bottomProfile_id:

                        ProfileFragment ProfileFragment = new ProfileFragment();
                        fragmentSet(ProfileFragment);
                        return true;

                    case R.id.bottomResult_id:
                        ResultFragment resultFragment = new ResultFragment();
                        fragmentSet(resultFragment);
                        return true;

                    case R.id.bottom_balance_wallet_id:
                        startActivity(new Intent(MainActivity.this, WalletActivity.class));
                        return true;

                  /*  case R.id.bottom_TopPlayer_id:

                        startActivity(new Intent(MainActivity.this, ExamActivity.class));

                      *//*  TopPlayerListFragment topPlayerListFragment = new TopPlayerListFragment();
                        fragmentSet(topPlayerListFragment);*//*
                        return true;
*/
                    default:
                        return false;
                }
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.main_balance_wallet_id){

            startActivity(new Intent(MainActivity.this, WalletActivity.class));

        }


        return super.onOptionsItemSelected(item);
    }

    private void fragmentSet(Fragment fragment){

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.hostFragment,fragment)
                .commit();

    }
}
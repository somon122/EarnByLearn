package com.example.pta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.codesgood.views.JustifiedTextView;

public class RulesActivity extends AppCompatActivity {
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    JustifiedTextView rulesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);


        Toolbar toolbar = findViewById(R.id.rulesToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("User Rules");
        rulesTextView = findViewById(R.id.rulesTextView);
        RulesClass rulesClass = new RulesClass();
        rulesTextView.setText(rulesClass.rules1+"\n\n" +rulesClass.rules2+"\n\n" +rulesClass.rules3+"\n\n");

    }
}
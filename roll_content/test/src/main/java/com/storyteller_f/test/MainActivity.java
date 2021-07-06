package com.storyteller_f.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private int current;
    private Timer roll_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView=findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TextAdapter textAdapter=new TextAdapter();
        recyclerView.setAdapter(textAdapter);
        roll_content = new Timer("roll content");
        roll_content.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i("TAG", "run: ");
                current+=100;
                textAdapter.progress(current);
            }
        },100,100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        roll_content.cancel();
    }
}
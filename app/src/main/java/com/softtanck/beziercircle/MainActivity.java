package com.softtanck.beziercircle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.softtanck.beziercircle.view.BezierCircle;


public class MainActivity extends AppCompatActivity {

    private BezierCircle bezierCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bezierCircle = (BezierCircle) findViewById(R.id.bezier_view);
    }

    public void StartAnim(View view) {
        bezierCircle.startAnimation();
    }
}

package com.example.a161030.ivycon20;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.LinearLayout;


public class TeacherGrade extends AppCompatActivity{

    int year;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_grade);

        year = 0;

        //ボタンのインスタンス生成
        Button grade_one = findViewById(R.id.grade_one);
        Button grade_two = findViewById(R.id.grade_two);
        Button grade_three = findViewById(R.id.grade_three);

        Intent intent = getIntent(); //インテントの作成
        Bundle extras = intent.getExtras(); //
        String depar = extras.getString("Key");

        Log.w("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",depar);

        switch (depar){
            case  "MI":
                year = 3;
                break;
            case "IN":
                year = 3;
                break;
            case "CG":
                year = 3;
                break;
            case "CAD":
                year = 3;
                break;
            case "OB":
                year = 2;
                break;
            case "IS":
                year = 2;
                break;
            case "PO":
                year = 1;
                break;
            case "MB":
                year = 2;
                break;
            case "IM":
                year = 3;
                break;
        }

        Log.w("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",String.valueOf(year));

        switch (year){
            case 1:
                grade_one.setVisibility(View.VISIBLE);
                grade_two.setVisibility(View.INVISIBLE);
                grade_three.setVisibility(View.INVISIBLE);
                break;
            case 2:
                grade_one.setVisibility(View.VISIBLE);
                grade_two.setVisibility(View.VISIBLE);
                grade_three.setVisibility(View.INVISIBLE);
                break;
            case 3:
                grade_one.setVisibility(View.VISIBLE);
                grade_two.setVisibility(View.VISIBLE);
                grade_three.setVisibility(View.VISIBLE);
                break;
        }

    }

    //バックキーが押されたらこのActivityを殺す
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            Log.w("@@@@@@@@@@@@@@@@@@@@@@@@@@@@","@@@@@@@@@@@@@@@@@@@");
            finish();
            return super.onKeyDown(keyCode, event);
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


}

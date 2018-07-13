package com.example.a161030.ivycon20;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.content.Intent;



public class TeacherGrade extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_grade);

        Intent intent = getIntent(); //インテントの作成
        Bundle extras = intent.getExtras(); //
        String aaa = extras.getString("Key");

        /* ボタンを取り出す　*/
        Button button4 = (Button)findViewById(R.id.button4);
        button4.setText("1"+aaa);


    }


}

package com.example.a161030.ivycon20;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.util.Log;

public class TeacherDepartment extends AppCompatActivity {


    //Logを使う時に必要
    private final static String TAG = TeacherDepartment.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_department);

        //医療情報学科
        findViewById(R.id.MI).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), TeacherGrade.class); //インテントの作成
                intent.putExtra("Key" ,"MI"); //Activityに学科のIDを引数として渡す
                startActivity(intent);   //画面遷移
            }
        });

        //インターネット学科
        findViewById(R.id.IN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), TeacherGrade.class); //インテントの作成
                intent.putExtra("Key" ,"IN"); //Activityに学科のIDを引数として渡す
                startActivity(intent);   //画面遷移
            }
        });

        //CGデザイン学科
        findViewById(R.id.CD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), TeacherGrade.class); //インテントの作成
                intent.putExtra("Key" ,"CD"); //Activityに学科のIDを引数として渡す
                startActivity(intent);   //画面遷移
            }
        });

        //3DCAD学科
        findViewById(R.id.CAD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), TeacherGrade.class); //インテントの作成
                intent.putExtra("Key" ,"CAD"); //Activityに学科のIDを引数として渡す
                startActivity(intent);   //画面遷移
            }
        });

        //オフィスビジネス学科
        findViewById(R.id.OB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), TeacherGrade.class); //インテントの作成
                intent.putExtra("Key" ,"OB"); //Activityに学科のIDを引数として渡す
                startActivity(intent);   //画面遷移
            }
        });

        //情報処理学科
        findViewById(R.id.IS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), TeacherGrade.class); //インテントの作成
                intent.putExtra("Key" ,"IS"); //Activityに学科のIDを引数として渡す
                startActivity(intent);   //画面遷移
            }
        });

        //公務員学科
        findViewById(R.id.PO).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), TeacherGrade.class); //インテントの作成
                intent.putExtra("Key" ,"PO"); //Activityに学科のIDを引数として渡す
                startActivity(intent);   //画面遷移
            }
        });

        //医療ビジネス学科
        findViewById(R.id.MB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int depId = v.getId(); //押された学科のIDを取得
                Intent intent = new Intent(getApplication(), TeacherGrade.class); //インテントの作成
                intent.putExtra("Key" ,"MB"); //Activityに学科のIDを引数として渡す
                startActivity(intent);   //画面遷移
            }
        });

        //モバイルシステム学科
        findViewById(R.id.IM).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), TeacherGrade.class); //インテントの作成
                intent.putExtra("Key" ,"IM"); //Activityに学科のIDを引数として渡す
                startActivity(intent);   //画面遷移
            }
        });
    }
}
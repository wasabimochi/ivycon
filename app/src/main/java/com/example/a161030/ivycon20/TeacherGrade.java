package com.example.a161030.ivycon20;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.Date;


public class TeacherGrade extends AppCompatActivity{

    //学年判別変数
    int year;

    //学科判別変数
    String dep;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_grade);

        //学年変数初期化
        year = 0;

        //現在のカレンダー取得
        Calendar calendar = Calendar.getInstance();
        //年を取得
        final int years = calendar.get(Calendar.YEAR);

        //ボタンのインスタンス生成
        Button grade_one = findViewById(R.id.grade_one);
        Button grade_two = findViewById(R.id.grade_two);
        Button grade_three = findViewById(R.id.grade_three);

        //インテントの作成
        Intent intent = getIntent();

        //前のアクティビティで渡された値を取得
        Bundle extras = intent.getExtras();

        //前のアクティビティで渡された値を学科変数に代入
        String depar = extras.getString("Key");

        //選択された学科によって学年変数の値を決める
        switch (depar){
            case  "MI":
                year = years - 2;
                dep = "-LGhFa150qB3TQHMa5z7";
                break;
            case "IN":
                year = years - 2;
                dep = "-LGhFWKgRIPC6jVByYip";
                break;
            case "CG":
                year = years - 2;
                dep = "-LGhFRm-6N8LgqM9WU9V";
                break;
            case "CAD":
                year = years - 2;
                dep = "-LGhFIYgqwB2vnnOUBG8";
                break;
            case "OB":
                year = years - 1;
                dep = "-LGhFbSuk3SdtaeE8CI5";
                break;
            case "IS":
                year = years - 1;
                dep = "-LGhFXiLSgc8ga6gesSS";
                break;
            case "CS":
                year = years;
                dep = "-LGhFTfSP-dPpDOqZ_Gh";
                break;
            case "MB":
                year = years - 1;
                dep = "-LGhFZjy3ZRKG_59GxWA";
                break;
            case "IM":
                year = years - 2;
                dep = "-LGhFVJSogdEShwcrSlP";
                break;
        }

        //学年によって表示させるボタンを決める
        switch (year){
            case 1: //1年であれば1年だけ表示させる
                grade_one.setVisibility(View.VISIBLE);
                grade_two.setVisibility(View.INVISIBLE);
                grade_three.setVisibility(View.INVISIBLE);
                break;
            case 2: //2年であれば1年と2年だけ表示させる
                grade_one.setVisibility(View.VISIBLE);
                grade_two.setVisibility(View.VISIBLE);
                grade_three.setVisibility(View.INVISIBLE);
                break;
            case 3: //3年であれば全て表示させる
                grade_one.setVisibility(View.VISIBLE);
                grade_two.setVisibility(View.VISIBLE);
                grade_three.setVisibility(View.VISIBLE);
                break;
        }

        //1年
        findViewById(R.id.grade_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //グローバル変数クラス
                UtilCommon common = (UtilCommon)getApplication();
                year = years;   //入学年を渡す
                Intent intent = new Intent(getApplication(), TeacherStudent.class); //インテントの作成
                common.setDepar(dep);
                common.setTyear(year);
                startActivity(intent);   //画面遷移
            }
        });

        //2年
        findViewById(R.id.grade_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //グローバル変数クラス
                UtilCommon common = (UtilCommon)getApplication();

                year = years - 1;   //入学年を渡す
                Intent intent = new Intent(getApplication(), TeacherStudent.class); //インテントの作成
                common.setDepar(dep);
                common.setTyear(year);

                startActivity(intent);   //画面遷移
            }
        });

        //3年
        findViewById(R.id.grade_three).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //グローバル変数クラス
                UtilCommon common = (UtilCommon)getApplication();

                year = years - 2;   //入学年を渡す
                Intent intent = new Intent(getApplication(), TeacherStudent.class); //インテントの作成
                common.setDepar(dep);
                common.setTyear(year);

                startActivity(intent);   //画面遷移
            }
        });

    }

    //バックキーが押されたらこのActivityを殺す
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return super.onKeyDown(keyCode, event);
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


}

package com.example.a161030.ivycon20;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class TeacherStudent extends AppCompatActivity {

    ListView ListView;

    //String[] students = {"hirose","takakura","kugimiya","yoshida"};
    ArrayList<String> students = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_timeline);

        students.add("hirose");
        students.add("kugimiya");
        students.add("takakura");
        students.add("yoshida");

        //Listを作る
        ListView = (ListView) findViewById(R.id.ListView);

        //arrayadapterの作成
        //レイアウトの指定
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,students);

        //アダプターの設定
        ListView.setAdapter(arrayAdapter);

        students.add("fujita");

        //arraylistに追加

    }

}

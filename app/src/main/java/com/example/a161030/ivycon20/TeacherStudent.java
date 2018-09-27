package com.example.a161030.ivycon20;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TeacherStudent extends AppCompatActivity {

    //String[] students = {"hirose","takakura","kugimiya","yoshida"};
    ArrayList<String> students = new ArrayList<String>();

    //リストビュー
    private ListView ListView;

    //リスト
    private ArrayList<StudentListItem> listItems = new ArrayList<>();

    //レイアウト
    private ArrayAdapter<String> arrayAdapter;

    //オリジナルのアダプター
    private StudentListAdapter Adapter;

    //FirebaseAuthオブジェクト作成
    private FirebaseAuth mAuth;

    //DatabaseReferenceオブジェクト作成
    private DatabaseReference mDatabase;

    //FireBaseストレージ
    private FirebaseStorage storage;

    //ストレージ
    private StorageReference storageRef;

    //画像の参照取得
    private StorageReference spaceRef;

    //サムネイルのビットマップ
    private Bitmap Thumbnail;

    //入室リスト配列
    private ArrayList<String> inDate = new ArrayList<>();

    //生徒のリスト配列
    private ArrayList<String> sName = new ArrayList<>();

    //生徒のUIDのリスト配列
    private ArrayList<String> sUID = new ArrayList<>();

    //カウント変数
    private int Count = 0;

    //Logを使う時に必要
    private final static String TAG = StudentTimeline.class.getSimpleName();

    //ユーザーID取得変数
    private String myUID;

    //今日の日付
    Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_timeline);

        //インテントの作成
        Intent intent = getIntent();

        //前のアクティビティで渡された値を取得
        Bundle extras = intent.getExtras();

        //前のアクティビティで渡された値を学科変数に代入
        String depar = extras.getString("Depar");
        int year = extras.getInt("Year");

        //FirebaseAuthオブジェクトの共有インスタンスを取得
        mAuth = FirebaseAuth.getInstance();

        //Databaseへの参照取得
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //FireBaseストレージへのアクセスインスタンス
        storage = FirebaseStorage.getInstance();

        //ストレージへの参照
        storageRef = storage.getReference();

        ////////////////////////////FireBaseのデータの取得処理//////////////////////////////////
        //FireBaseのイベント
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            //一度データを読み込み、そのあとはデータの中身が変わるたびに実行される
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Ivycon2/Loginの子要素分繰り返すしかも順番に見ていってくれる
                for (DataSnapshot postSnapshot : dataSnapshot.child("Ivycon2").child("Student").getChildren()) {

                    //UIDをとってくる
                    Object UID = postSnapshot.child("UID").getValue();
                }
            }

            @Override
            //データがとりに行けなかった場合
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TeacherStudent.this, "データが取得できませんでした", Toast.LENGTH_SHORT).show();
                Log.w("エラー", databaseError.toException());
            }
        });

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

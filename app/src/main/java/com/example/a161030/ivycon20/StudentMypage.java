package com.example.a161030.ivycon20;

import android.bluetooth.le.BluetoothLeScanner;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentMypage extends AppCompatActivity {

    //FirebaseAuthオブジェクト作成
    private FirebaseAuth mAuth;

    //DatabaseReferenceオブジェクト作成
    private DatabaseReference mDatabase;

    //Logを使う時に必要
    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_mypage);

        //FirebaseAuthオブジェクトの共有インスタンスを取得
        mAuth = FirebaseAuth.getInstance();

        BluetoothLeScanner mBluetoothLeScanner;

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //FireBaseのイベント。最後に走る
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            //一度データを読み込み、そのあとはデータの中身が変わるたびに実行される
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //全データを取ってくる
                Object Value = dataSnapshot.getValue();
                //厳選してとる
                Object Value2 = dataSnapshot.child("Ivycon").child("Student").child("161019").child("Photo").getValue();

                //Log.w("ゲット",Value.toString());
                assert Value2 != null;
                Log.w("ゲット2",Value2.toString());

            }

            @Override
            //データがとりに行けなかった場合
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("エラー", databaseError.toException());
            }
        });
    }
}

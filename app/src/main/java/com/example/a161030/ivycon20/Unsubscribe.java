package com.example.a161030.ivycon20;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Unsubscribe extends AppCompatActivity {

    private StudentTimeline studentTimeline;

    //DatabaseReferenceオブジェクト作成
    private DatabaseReference mDatabase;

    //Logを使う時に必要
    private final static String TAG = Unsubscribe.class.getSimpleName();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unsubscribe);


        //Reference取得
        mDatabase = FirebaseDatabase.getInstance().getReference();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("確認");
                builder.setMessage("アカウントを削除しますか？削除したときはアプリを終了します。");

        builder.setPositiveButton("削除する", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //ユーザ情報を取得
                        FirebaseUser user = studentTimeline.user;

                        //student内の　uid 名前　学年　番号 プロフィールを削除
                        mDatabase.child("Ivycon2").child("Student").child(StudentTimeline.myUID).child("Depar").setValue(null);
                        mDatabase.child("Ivycon2").child("Student").child(StudentTimeline.myUID).child("Name").setValue(null);
                        mDatabase.child("Ivycon2").child("Student").child(StudentTimeline.myUID).child("Num").setValue(null);
                        mDatabase.child("Ivycon2").child("Student").child(StudentTimeline.myUID).child("Prof").setValue(null);
                        mDatabase.child("Ivycon2").child("Student").child(StudentTimeline.myUID).child("Year").setValue(null);

                        //ログアウト処理
                        studentTimeline.mAuth.signOut();


                        //ユーザーの情報を削除
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //削除に成功したら
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "アカウント消えた");
                                    Toast.makeText(Unsubscribe.this, "アカウントを削除しました。", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(Unsubscribe.this, "アプリを終了します。", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        finish();
                        System.exit(0);

                    }
                })
                .setNegativeButton("削除しない", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent logout = new Intent(getApplication(), StudentTimeline.class);    //インテントの作成
                        startActivity(logout);
                        finish();
                    }
                })
                .show();
        }

}


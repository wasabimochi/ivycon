package com.example.a161030.ivycon20;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

public class Unsubscribe extends AppCompatActivity{

    private StudentTimeline studentTimeline;

    //Logを使う時に必要
    private final static String TAG = Unsubscribe.class.getSimpleName();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unsubscribe);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("今からアプリを起動してもいいですか？")
                .setPositiveButton("起動", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    // ボタンをクリックしたときの動作
                        //ユーザーの情報を削除
                        studentTimeline.user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "アカウント消えた");
                                }
                            }
                        });

                    }
                });
        builder.show();
        }
    }

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
import com.google.firebase.storage.StorageReference;

import static android.app.PendingIntent.getActivity;
import static com.example.a161030.ivycon20.StudentTimeline.user;

public class Unsubscribe extends AppCompatActivity {

    private StudentTimeline studentTimeline;

    //DatabaseReferenceオブジェクト作成
    private DatabaseReference mDatabase;

    //Logを使う時に必要
    private final static String TAG = Unsubscribe.class.getSimpleName();

    String User = String.valueOf(user);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unsubscribe);

        //Reference取得
        mDatabase = FirebaseDatabase.getInstance().getReference();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("確認");
                builder.setMessage("アカウントを削除しますか");

        builder.setPositiveButton("削除する", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // OK button pressed
                        mDatabase.child(StudentTimeline.user.getUid()).child(null).setValue(null);
                        Log.d(TAG, "アカウント消えた");
                        //ログイン画面へ
                        Intent intent = new Intent(getApplication(),LoginStudent.class);
                        finish();
                        startActivity(intent);  //画面遷移


                        /*
                        //ユーザーの情報を削除
                        studentTimeline.user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "アカウント消えた");
                                    //ログイン画面へ
                                    Intent intent = new Intent(getApplication(),LoginStudent.class);
                                    finish();
                                    startActivity(intent);  //画面遷移
                                }
                            }
                        });
*/

                        Toast.makeText(Unsubscribe.this, "アカウントを削除しました。", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("削除しない", null)
                .show();
        }

}


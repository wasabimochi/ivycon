package com.example.a161030.ivycon20;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordForget extends AppCompatActivity {

    //FirebaseAuthオブジェクト作成
    FirebaseAuth auth;

    //メールアドレス取得変数
    EditText adress;

    //Logを使う時に必要
    private final static String TAG = PasswordForget.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_forget);

        //FirebaseAuthオブジェクトの共有インスタンスを取得
        auth = FirebaseAuth.getInstance();

        //xmlからメールアドレス取得
        adress = findViewById(R.id.forget_mail);

        //メール送信ボタン
        findViewById(R.id.transmission_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",adress.getText().toString());
                //メール送信
                auth.sendPasswordResetEmail(adress.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        //メールが無事送信できればログを出す
                        if (task.isSuccessful()) {
                            //アラートを表示
                            Toast.makeText(PasswordForget.this, "送信できました。", Toast.LENGTH_SHORT).show();

                            //ログイン画面に戻る
                            finish();

                        }else{  //送信できなければアラートを表示
                            Toast.makeText(PasswordForget.this, "メールアドレスが見つかりませんでした。もう一度、入力してください。", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}

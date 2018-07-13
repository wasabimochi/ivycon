package com.example.a161030.ivycon20;
//これが本物 2018/06/28
//あーうんち

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginStudent extends AppCompatActivity implements OnClickListener {

    //FirebaseAuthオブジェクト作成
    private FirebaseAuth mAuth;

    //FirebaseUserオブジェクト作成
    FirebaseUser user;

    private Button button_segue;

    //打ち込まれたメールアドレスとパスワードを取得する変数
    EditText login_aders,login_paswd;

    //取得したメールアドレスとパスワードを代入する変数
    String adres,passwd;

    //Logを使う時に必要
    private final static String TAG = LoginStudent.class.getSimpleName();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //頭にappthemaつけないとスプラッシュのまんまになるので注意
        setTheme(R.style.AppTheme);

        setContentView(R.layout.login_student);
        //xmlからメールアドレスとパスワード取得
        login_aders = findViewById(R.id.login_address);
        login_paswd = findViewById(R.id.login_password);

        //FirebaseAuthオブジェクトの共有インスタンスを取得
        mAuth = FirebaseAuth.getInstance();

        //ユーザーの現在の状況を取得(ログインしているかなど)
        user = FirebaseAuth.getInstance().getCurrentUser();

        button_segue=(Button)findViewById(R.id.login_button);    //button3を見つけ出す
        button_segue.setOnClickListener(this);              //button3にクリックイベントをぶち込む
    }

    //テキスト（アカウント新規作成）のクリックイベント
    public void NewAccountMove(View view) {
        Intent intent = new Intent(getApplication(), NewAccountActivity.class); //インテントの作成
        startActivity(intent);   //画面遷移
    }

    //テキスト（パスワード忘れ）のクリックイベント
    public void PassForget(View view){
        Intent intent = new Intent(getApplication(), PasswordForget.class); //インテントの作成
        startActivity(intent);   //画面遷移
    }

    //管理者ログインのイベント
    public void TeacherDepartmentMove(View view){
        Intent intent = new Intent(getApplication(), TeacherDepartment.class);    //インテントの作成
        startActivity(intent);  //画面遷移
    }

    //ログインボタンが押された時の処理
    public void onClick(View view){

        try {
            adres = login_aders.getText().toString();
            passwd = login_paswd.getText().toString();

            //ログイン処理
            mAuth.signInWithEmailAndPassword(adres, passwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {  //ログインに成功し、ログインしたユーザーの情報でUIを更新
                        // Log.d(TAG, "ログインうまくいった");
                        user = mAuth.getCurrentUser();
                        Intent intent = new Intent(getApplication(), StudentTimeline.class);    //インテントの作成
                        startActivity(intent);  //画面遷移
                    } else {                       //サインインに失敗した場合は、ユーザーにメッセージを表示
                        Log.w(TAG, "ログインダメでした", task.getException());
                        Toast.makeText(LoginStudent.this, "もう一度入力してください。", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }catch(Exception e){
            Toast.makeText(LoginStudent.this, "メールアドレスとパスワードを入力してください。", Toast.LENGTH_SHORT).show();
        }
    }




}

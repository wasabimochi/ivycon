package com.example.a161030.ivycon20;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginTeacher extends AppCompatActivity {

    //FirebaseAuthオブジェクト作成
    private FirebaseAuth mAuth;

    //FirebaseUserオブジェクト作成
    FirebaseUser user;

    //打ち込まれたメールアドレスとパスワードを取得する変数
    EditText teacher_login_aders,teacher_login_paswd;

    //Logを使う時に必要
    private final static String TAG = LoginStudent.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_login);

        //FirebaseAuthオブジェクトの共有インスタンスを取得
        mAuth = FirebaseAuth.getInstance();

        //ユーザーの現在の状況を取得(ログインしているかなど)
        user = FirebaseAuth.getInstance().getCurrentUser();

        //ログインボタンを押されたらログイン処理を実行
        findViewById(R.id.teacher_login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //login();
                //インテントの作成
                Intent intent = new Intent(getApplication(), StudentMypageEdit.class);

                //画面遷移
                startActivity(intent);
            }
        });
    }


    void login(){

        //xmlからメールアドレスとパスワード取得
        EditText teacher_login_aders = findViewById(R.id.teacher_login_address);
        EditText teacher_login_paswd = findViewById(R.id.teacher_login_password);

        //try {
            //ログイン処理
            mAuth.signInWithEmailAndPassword(teacher_login_aders.getText().toString(), teacher_login_paswd.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {  //ログインに成功し、ログインしたユーザーの情報でUIを更新
                        // Log.d(TAG, "ログインうまくいった");
                        user = mAuth.getCurrentUser();
                        Intent intent = new Intent(getApplication(), TeacherDepartment.class);    //インテントの作成
                        startActivity(intent);  //画面遷移
                    } else {                       //サインインに失敗した場合は、ユーザーにメッセージを表示
                        Log.w(TAG, "ログインダメでした", task.getException());
                        Toast.makeText(LoginTeacher.this, "もう一度入力してください。", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        //}catch(Exception e){
            //Toast.makeText(LoginTeacher.this, "メールアドレスとパスワードを入力してください。", Toast.LENGTH_SHORT).show();
        //}
    }
}

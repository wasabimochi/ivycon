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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static android.util.Pair.create;

public class NewAccountActivity extends AppCompatActivity {

    //FirebaseAuthオブジェクト生成
    private FirebaseAuth mAuth;

    //DatabaseReferenceオブジェクト作成
    private DatabaseReference mDatabase;

    //FirebaseUserオブジェクト作成
    FirebaseUser user;

    //ユーザーID取得変数
    String UID;

    //Logを使う時に必要
    private final static String TAG = NewAccountActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.new_account);

        //FirebaseAuthオブジェクトの共有インスタンスを取得
        mAuth = FirebaseAuth.getInstance();

        //ユーザーの現在の状況を取得(ログインしているかなど)
        user = FirebaseAuth.getInstance().getCurrentUser();

        //Reference取得
        mDatabase = FirebaseDatabase.getInstance().getReference();

        findViewById(R.id.new_account_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create();
            }
        });
    }


    //アカウント作成関数
    public void create() {

        //登録する項目を取得する
        final EditText number = findViewById(R.id.new_number);
        final EditText department = findViewById(R.id.new_department);
        final EditText year = findViewById(R.id.new_year);
        final EditText name = findViewById(R.id.new_name);
        EditText mail = findViewById(R.id.new_mail);
        EditText passwd = findViewById(R.id.new_password);
        EditText passwd_again = findViewById(R.id.password_again);

        //学籍番号がきちんと入力されていなければ
        if (number.getText().length() == 0) {

            //アラートを表示
            Toast.makeText(NewAccountActivity.this, "学籍番号を入力してください。", Toast.LENGTH_SHORT).show();

            return;
        }
        //学科がきちんと入力されていなければ
        if (department.getText().length() == 0) {

            //アラートを表示
            Toast.makeText(NewAccountActivity.this, "学科を入力してください。", Toast.LENGTH_SHORT).show();

            return;
        }
        //学年がきちんと入力されていなければ
        if (year.getText().length() == 0) {

            //アラートを表示
            Toast.makeText(NewAccountActivity.this, "学年を入力してください。", Toast.LENGTH_SHORT).show();

            return;
        }
        //名前がきちんと入力されていなければ
        if (name.getText().length() == 0) {

            //アラートを表示
            Toast.makeText(NewAccountActivity.this, "名前を入力してください。", Toast.LENGTH_SHORT).show();

            return;
        }
        //メールアドレスがきちんと入力されていなければ
        if (mail.getText().length() == 0) {

            //アラートを表示
            Toast.makeText(NewAccountActivity.this, "メールアドレスを入力してください。", Toast.LENGTH_SHORT).show();

            return;
        }
        //パスワードがきちんと入力されていなければ
        if (passwd.getText().length() == 0) {

            //アラートを表示
            Toast.makeText(NewAccountActivity.this, "パスワードを入力してください。", Toast.LENGTH_SHORT).show();

            return;
        }
        //確認用パスワードがきちんと入力されていなければ
        if (passwd_again.getText().length() == 0) {

            //アラートを表示
            Toast.makeText(NewAccountActivity.this, "確認用パスワードを入力してください。", Toast.LENGTH_SHORT).show();
            return;
        }
        
        
        if(!passwd.getText().toString().equals(passwd_again.getText().toString())) {
            //パスワードが一致していなければアラートを表示させる
            Toast.makeText(NewAccountActivity.this, "パスワードが一致しませんでした。", Toast.LENGTH_SHORT).show();
            return;
        }

        
        mAuth.createUserWithEmailAndPassword(mail.getText().toString(), passwd.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // ログインに成功し、ログインしたユーザーの情報でUIを更新します
                    FirebaseUser user = mAuth.getCurrentUser();

                    //UIDの取得
                    assert user != null;
                    UID = user.getUid();

                    //インスタンス取得
                    Map<String, Object> childUpdates = new HashMap<>();

                    //UIDのrefの習得
                    mDatabase = mDatabase.child("Ivycon2").child("Student").child(UID).getRef();

                    //データ書き込みのイベント複数セッティング

                    //学籍番号
                    childUpdates.put("Num", number.getText().toString());
                    //学科
                    childUpdates.put("Depar", department.getText().toString());
                    //学年
                    childUpdates.put("Year", year.getText().toString());
                    //名前
                    childUpdates.put("Name", name.getText().toString());

                    //イベント実行
                    mDatabase.updateChildren(childUpdates);

                    //インテントの作成
                    Intent intent = new Intent(getApplication(), StudentTimeline.class);

                    //画面遷移
                    startActivity(intent);
                } else {
                    // サインインに失敗した場合は、ユーザーにメッセージを表示します。
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(NewAccountActivity.this, "認証に失敗しました。", Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"作れなかった");
                }
            }
        });
    }
}
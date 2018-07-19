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

    //EditTextからデータを取得する変数
    EditText number,department,year,name,mail,passwd,passwd_again;

    //EditTextを代入する変数
    Object num,depar,yer,nam;

    String adress,pass,pass_ag;

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

        //登録する項目を取得する
        number = findViewById(R.id.new_number);
        department = findViewById(R.id.new_department);
        year = findViewById(R.id.new_year);
        name = findViewById(R.id.new_name);
        mail = findViewById(R.id.new_mail);
        passwd = findViewById(R.id.new_password);
        passwd_again = findViewById(R.id.password_again);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Reference取得
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //
        findViewById(R.id.new_account_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //EditeTextをStringに変換
                num = number.getText().toString();
                depar = department.getText().toString();
                yer = year.getText().toString();
                nam = name.getText().toString();
                adress = mail.getText().toString();
                pass = passwd.getText().toString();
                pass_ag = passwd_again.getText().toString();

                if(pass.equals(pass_ag)) {
                    //アカウント作成関数
                    create(adress, pass);
                }else {
                    Toast.makeText(NewAccountActivity.this, "パスワード打ち直し",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //アカウント作成関数
    public void create(String new_adress,String new_passwd){
        mAuth.createUserWithEmailAndPassword(new_adress, new_passwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // ログインに成功し、ログインしたユーザーの情報でUIを更新します
                    FirebaseUser user = mAuth.getCurrentUser();

                    //UIDの取得
                    UID = user.getUid().toString();

                    //UIDのrefの取得
                    mDatabase = mDatabase.child("Ivycon2").child("Student").child(UID.toString()).getRef();

                    //インスタンス取得
                    Map<String, Object> childUpdates = new HashMap<>();

                    //データ書き込みのイベント複数セッティング
                    childUpdates.put("UID", UID);
                    childUpdates.put("Num", num);
                    childUpdates.put("Depar", depar);
                    childUpdates.put("Year", yer);
                    childUpdates.put("Name", nam);
                    childUpdates.put("Profiel", "よろしくお願いします");

                    //イベント実行
                    mDatabase.updateChildren(childUpdates);

                    //インテントの作成
                    Intent intent = new Intent(getApplication(), StudentTimeline.class);

                    //画面遷移
                    startActivity(intent);

                } else {
                    // サインインに失敗した場合は、ユーザーにメッセージを表示します。
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());

                    //メッセージ表示
                    Toast.makeText(NewAccountActivity.this, "認証に失敗しました。", Toast.LENGTH_SHORT).show();

                    Log.d(TAG,"作れなかった");
                }
            }
        });
    }
}

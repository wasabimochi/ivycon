package com.example.a161030.ivycon20;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
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

    Object Depar;

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

        //スピナーを登録
        Spinner spinner = findViewById(R.id.new_department);

        // android.R.Layout.simple_spinner_itemをR.layout.spinner_itemに変更
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.sample_array, R.layout.simple_spinner);
        // spinner に adapter をセット
        spinner.setAdapter(adapter);

        //レイアウトを設定
        adapter.setDropDownViewResource(R.layout.dropdown_spinner);

        // リスナーを登録
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //　アイテムが選択された時
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                Spinner spinner = (Spinner)parent;
                String item = (String)spinner.getSelectedItem();
            }

            //　アイテムが選択されなかった
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    //アカウント作成関数
    public void create() {

        //登録する項目を取得する
        final EditText number = findViewById(R.id.new_number);

        // Spinnerオブジェクトを取得
        Spinner department = (Spinner)findViewById(R.id.new_department);

        // 選択されているアイテムのIndexを取得
        int department_idx = department.getSelectedItemPosition();

        // 選択されているアイテムを取得
        String department_item = (String)department.getSelectedItem();

        Spinner year = (Spinner)findViewById(R.id.new_year);

        // 選択されているアイテムのIndexを取得
        final int year_idx = department.getSelectedItemPosition();

        // 選択されているアイテムを取得
        String year_item = (String)department.getSelectedItem();

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

        //度の学科が選択されたか
        if (department_idx == 0) {
            department_item = "-LGhFa150qB3TQHMa5z7";
        }else if(department_idx == 1){
            department_item ="-LGhFWKgRIPC6jVByYip";
        }else if(department_idx == 2){
            department_item = "-LGhFRm-6N8LgqM9WU9V";
        }else if(department_idx == 3){
            department_item = "-LGhFIYgqwB2vnnOUBG8";
        }else if(department_idx ==4){
            department_item = "-LGhFbSuk3SdtaeE8CI5";
        }else if(department_idx == 5){
            department_item = "-LGhFXiLSgc8ga6gesSS";
        }else if(department_idx == 6){
            department_item = "-LGhFTfSP-dPpDOqZ_Gh";
        }else if(department_idx == 7){
            department_item = "-LGhFZjy3ZRKG_59GxWA";
        }else if(department_idx == 8){
            department_item = "-LGhFVJSogdEShwcrSlP";
        }

        //FireBaseのイベント
        final String finalDepartment_item1 = department_item;
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            //一度データを読み込み、そのあとはデータの中身が変わるたびに実行される
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Deparをとってくる
                Depar = dataSnapshot.child("Ivycon2").child("Deper").child(finalDepartment_item1).getValue();
                Log.w("きたああああああああああああああああああ",Depar.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("エラー", databaseError.toException());
            }
        });

        //現在のカレンダー取得
        Calendar calendar = Calendar.getInstance();
        //年を取得
        final int years = calendar.get(Calendar.YEAR);

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
        if (passwd.getText().length() == 0 || passwd.getText().length() <= 6) {

            //アラートを表示
            Toast.makeText(NewAccountActivity.this, "パスワードを入力してください。", Toast.LENGTH_SHORT).show();

            return;
        }
        //確認用パスワードがきちんと入力されていなければ
        if (passwd_again.getText().length() == 0 || passwd_again.getText().length() <= 6) {

            //アラートを表示
            Toast.makeText(NewAccountActivity.this, "確認用パスワードを入力してください。", Toast.LENGTH_SHORT).show();
            return;
        }
        
        
        if(!passwd.getText().toString().equals(passwd_again.getText().toString())) {
            //パスワードが一致していなければアラートを表示させる
            Toast.makeText(NewAccountActivity.this, "パスワードが一致しませんでした。", Toast.LENGTH_SHORT).show();
            return;
        }


        final String finalDepartment_item = department_item;
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
                    childUpdates.put("Depar", Depar.toString());
                    //入学年
                    childUpdates.put("Year", String.valueOf(years - year_idx));
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
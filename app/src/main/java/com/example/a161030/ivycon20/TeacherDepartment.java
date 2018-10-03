package com.example.a161030.ivycon20;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TeacherDepartment extends AppCompatActivity {

    //FirebaseAuthオブジェクト作成
    private FirebaseAuth mAuth;

    //Logを使う時に必要
    private final static String TAG = TeacherDepartment.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_department);

        //FirebaseAuthオブジェクトの共有インスタンスを取得
        mAuth = FirebaseAuth.getInstance();

        NavigationView mNavigationView = (NavigationView) findViewById(R.id.teacher_department_navigationview);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //ここまではいってる
                switch (item.getItemId()) {

                    case R.id.myPage:
                        break;

                    case R.id.logout:
                        //ログアウト
                        mAuth.signOut();

                        //ユーザーの現在の状況を取得(ログインしているかなど)
                        FirebaseUser user = mAuth.getCurrentUser();

                        //ログインしているかどうかの判定
                        if (user != null) { //ログインしていればログを出すだけ
                            Log.d(TAG, "ログインしてる");
                        } else {            //ログインしていなければログを出しログイン画面に遷移する
                            //アラートを表示
                            Toast.makeText(TeacherDepartment.this, "ログアウトしました。", Toast.LENGTH_SHORT).show();
                            Intent logout = new Intent(getApplication(), LoginStudent.class);    //インテントの作成
                            startActivity(logout);
                            finish();
                            Log.d(TAG, "ログインしてない");
                        }
                        break;

                        default:
                        break;
                }
                    return false;
                }
        });

                //医療情報学科
                findViewById(R.id.MI).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplication(), TeacherGrade.class); //インテントの作成
                        intent.putExtra("Key", "MI"); //Activityに学科のIDを引数として渡す
                        startActivity(intent);   //画面遷移
                    }
                });

        //インターネット学科
        findViewById(R.id.IN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), TeacherGrade.class); //インテントの作成
                intent.putExtra("Key" ,"IN"); //Activityに学科のIDを引数として渡す
                startActivity(intent);   //画面遷移
            }
        });

        //CGデザイン学科
        findViewById(R.id.CD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), TeacherGrade.class); //インテントの作成
                intent.putExtra("Key" ,"CG"); //Activityに学科のIDを引数として渡す
                startActivity(intent);   //画面遷移
            }
        });

        //3DCAD学科
        findViewById(R.id.CAD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), TeacherGrade.class); //インテントの作成
                intent.putExtra("Key" ,"CAD"); //Activityに学科のIDを引数として渡す
                startActivity(intent);   //画面遷移
            }
        });

        //オフィスビジネス学科
        findViewById(R.id.OB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), TeacherGrade.class); //インテントの作成
                intent.putExtra("Key" ,"OB"); //Activityに学科のIDを引数として渡す
                startActivity(intent);   //画面遷移
            }
        });

        //情報処理学科
        findViewById(R.id.IS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), TeacherGrade.class); //インテントの作成
                intent.putExtra("Key" ,"IS"); //Activityに学科のIDを引数として渡す
                startActivity(intent);   //画面遷移
            }
        });

        //公務員学科
        findViewById(R.id.PO).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), TeacherGrade.class); //インテントの作成
                intent.putExtra("Key" ,"CS"); //Activityに学科のIDを引数として渡す
                startActivity(intent);   //画面遷移
            }
        });

        //医療ビジネス学科
        findViewById(R.id.MB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int depId = v.getId(); //押された学科のIDを取得
                Intent intent = new Intent(getApplication(), TeacherGrade.class); //インテントの作成
                intent.putExtra("Key" ,"MB"); //Activityに学科のIDを引数として渡す
                startActivity(intent);   //画面遷移
            }
        });

        //モバイルシステム学科
        findViewById(R.id.IM).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), TeacherGrade.class); //インテントの作成
                intent.putExtra("Key" ,"IM"); //Activityに学科のIDを引数として渡す
                startActivity(intent);   //画面遷移
            }
        });
    }

    //バックキーが押されたらこのActivityを殺す
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            Log.w("@@@@@@@@@@@@@@@@@@@@@@@@@@@@","@@@@@@@@@@@@@@@@@@@");
            finish();
            return super.onKeyDown(keyCode, event);
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /*/バックキーが押されたらこのActivityを殺す
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_HOME) {
            //ホームボタンが押された時や、他のアプリが起動した時に呼ばれる
            //戻るボタンが押された場合には呼ばれない
            //ホームボタンが押されたら強制的にログアウトする
            Toast.makeText(getApplicationContext(), "Good bye!" , Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            finish();
        }
    }

    @Override
    public void onUserLeaveHint(){
        //ホームボタンが押された時や、他のアプリが起動した時に呼ばれる
        //戻るボタンが押された場合には呼ばれない
        //ホームボタンが押されたら強制的にログアウトする
        Toast.makeText(getApplicationContext(), "Good bye!" , Toast.LENGTH_SHORT).show();
        mAuth.signOut();
        finish();
    }*/
}
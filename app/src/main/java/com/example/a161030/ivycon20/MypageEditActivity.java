package com.example.a161030.ivycon20;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

//マイページの編集ボタンから呼出される。

public class MypageEditActivity extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //編集前の情報を引き継いで表示
        //アイコンをクリックすると画像をアップロードする。要アップロードクラス。
        //編集画面を押すと入力した情報をFBに送信し、マイページに戻る。戻った際にマイページを更新し最新の情報にする。
    }
}

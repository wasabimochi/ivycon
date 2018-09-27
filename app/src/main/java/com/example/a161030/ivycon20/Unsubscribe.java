package com.example.a161030.ivycon20;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public class Unsubscribe extends AppCompatActivity{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unsubscribe);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("今からアプリを起動してもいいですか？")
                .setPositiveButton("起動", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    // ボタンをクリックしたときの動作
                    }
                });
        builder.show();
        }
    }

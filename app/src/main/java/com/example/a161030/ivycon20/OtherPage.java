package com.example.a161030.ivycon20;

import android.bluetooth.le.BluetoothLeScanner;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

import static java.lang.Integer.parseInt;

public class OtherPage extends AppCompatActivity {
    //FirebaseAuthオブジェクト作成
    private FirebaseAuth mAuth;

    //DatabaseReferenceオブジェクト作成
    private DatabaseReference mDatabase;

    //Logを使う時に必要
    private final static String TAG = MainActivity.class.getSimpleName();

    //FireBaseストレージ
    private FirebaseStorage storage;

    //ストレージ
    private StorageReference storageRef;

    //画像の参照取得
    private StorageReference spaceRef;

    //イメージビュー
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_mypage);

        //オプションボタンを非表示に
        ImageView Option = (ImageView)findViewById(R.id.option_button);
        Option.setVisibility(View.INVISIBLE);

        //FirebaseAuthオブジェクトの共有インスタンスを取得
        mAuth = FirebaseAuth.getInstance();

        BluetoothLeScanner mBluetoothLeScanner;

        //firebaseのリファレンス
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //FireBaseストレージへのアクセスインスタンス
        storage = FirebaseStorage.getInstance();

        //ストレージへの参照
        storageRef = storage.getReference();

        //アイコン
        image = (ImageView)findViewById(R.id.imageView3);

        TextView mypage = findViewById(R.id.textView10);
        mypage.setText(null);

        //FireBaseのイベント
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            //一度データを読み込み、そのあとはデータの中身が変わるたびに実行される
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //グローバル変数クラス
                UtilCommon common = (UtilCommon)getApplication();

                //生徒のデータを取ってくる
                Object Depar = dataSnapshot.child("Ivycon2").child("Student").child(common.getothersUID()).child("Depar").getValue();
                Object Name = dataSnapshot.child("Ivycon2").child("Student").child(common.getothersUID()).child("Name").getValue();
                Object Num = dataSnapshot.child("Ivycon2").child("Student").child(common.getothersUID()).child("Num").getValue();
                Object Prof = dataSnapshot.child("Ivycon2").child("Student").child(common.getothersUID()).child("Prof").getValue();
                Object Year = dataSnapshot.child("Ivycon2").child("Student").child(common.getothersUID()).child("Year").getValue();


                //UIDからIN、IMの形式でデータを取ってくる
                Depar = dataSnapshot.child("Ivycon2").child("Deper").child(Depar.toString()).getValue();

                //学科
                TextView DeperView = findViewById(R.id.textView14);
                DeperView.setText(Depar.toString());

                //学籍番号
                TextView NumView = findViewById(R.id.textView15);
                NumView.setText(Num.toString());

                //名前
                TextView NameView = findViewById(R.id.textView6);
                NameView.setText(Name.toString());

                //カレンダーインスタンス
                Calendar cal = Calendar.getInstance();

                //今の年
                final int NowYear = cal.get(Calendar.YEAR);

                //今の月
                final int Month = cal.get(Calendar.MONTH);

                //生徒の入学年
                int AdmissionYear = parseInt(Year.toString());


                //入学と今の年の差と最低学年
                int DifYear = NowYear - AdmissionYear + 1;

                //4月より前なら
                if (Month <= 4){
                    DifYear--;
                }

                //型変換
                String y = String.valueOf(DifYear);
                //学年
                TextView YearView = findViewById(R.id.textView16);
                YearView.setText(y);

                if(Prof != null) {
                    //プロフィール
                    TextView ProfView = findViewById(R.id.textView11);
                    ProfView.setText(Prof.toString());
                }

                ////////////////////////////サムネイルの画像取得処理//////////////////////////////////
                //画像の参照取得
                spaceRef = storageRef.child("Image/Icon/" + common.getothersUID() + "/" + common.getothersUID() +".jpeg");

                //メモリ
                final long ONE_MEGABYTE = 1024 * 1024;

                //ストレージイベント
                spaceRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        //画像取得
                        Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);

                        //設定
                        image.setImageBitmap(bmp);

                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });
            }
            @Override
            //データがとりに行けなかった場合
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("エラー", databaseError.toException());
            }
        });
    }

}

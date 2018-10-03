package com.example.a161030.ivycon20;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherStudent extends AppCompatActivity {

    //リストビュー
    ListView ListView;

    //リスト
    private ArrayList<TeacherStudentListItem> listItems = new ArrayList<>();

    //オリジナルのアダプター
    private TecherStudentListAdapter Adapter;

    //サムネイルのビットマップ
    private Bitmap Thumbnail;

    //ログインの緑○のビットマップ
    private Bitmap Login;

    //DatabaseReferenceオブジェクト作成
    private DatabaseReference mDatabase;

    //FireBaseストレージ
    private FirebaseStorage storage;

    //ログインデータ
    private FirebaseAuth mAuth;
    //ストレージ
    private StorageReference storageRef;

    //画像の参照取得
    private StorageReference spaceRef;

    //生徒のリスト配列
    private ArrayList<String> sName = new ArrayList<>();

    //生徒のUIDのリスト配列
    private ArrayList<String> sUID = new ArrayList<>();

    //オンラインかオフラインか
    private ArrayList<Boolean> ONorOFF = new ArrayList<Boolean>();

    //カウント変数
    private int Count = 0;

    //ログインしているか
    private Object Online;

    //学科
    private String depar;

    //年
    private String year;

    //今日の日付
    private Calendar calendar;

    //フラグ
    private boolean Flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_student);

        //Listを作る
        ListView = (ListView) findViewById(R.id.TeacherView);

        // タップ時のイベントを追加
        //ListView.setOnItemClickListener(onItemClickListener);

        //グローバル変数クラス
        UtilCommon common = (UtilCommon)getApplication();

        depar = common.getDepar();
        int y = common.getTyear();
        year = String.valueOf(y);

        //今日の日付
        calendar = Calendar.getInstance();

        //FirebaseAuthオブジェクトの共有インスタンスを取得
        mAuth = FirebaseAuth.getInstance();

        //Databaseへの参照取得
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //FireBaseストレージへのアクセスインスタンス
        storage = FirebaseStorage.getInstance();

        //ストレージへの参照
        storageRef = storage.getReference();

        ////////////////////////////FireBaseのデータの取得処理//////////////////////////////////
        //FireBaseのイベント
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            //一度データを読み込み、そのあとはデータの中身が変わるたびに実行される
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //リストビューとリスト中身の削除
                if(sUID.size() > 0){
                    listItems.clear();
                    //呼出し
                    OriginalAdapter();
                    //リストビュー作成
                    ListView.setAdapter(Adapter);
                    sUID.clear();
                    sName.clear();
                    ONorOFF.clear();
                    Count = 0;
                }

                Toast.makeText(TeacherStudent.this, "データ取得中。", Toast.LENGTH_SHORT).show();

                //Ivycon2/Loginの子要素分繰り返すしかも順番に見ていってくれる
                for (DataSnapshot postSnapshot : dataSnapshot.child("Ivycon2").child("Student").getChildren()) {

                    Object key = postSnapshot.getKey();

                    //学科と学年
                    Object Depar = postSnapshot.child("Depar").getValue();
                    Object Year = postSnapshot.child("Year").getValue();

                    //nllチェック
                    if(Depar != null && Year != null){

                        //学科と学年が一致したら
                        if(Depar.toString().equals(depar) && Year.toString().equals(year)) {

                            //UIDをとってくる
                            Object UID = postSnapshot.getKey();

                            //名前をとってくる
                            Object StudentName = postSnapshot.child("Name").getValue();


                            //UIDの比較
                            //取得データのnullチェック
                            if (UID != null && StudentName != null) {
                                Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@UID", UID.toString());
                                Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@NAME", StudentName.toString());

                                //リストに格納
                                sUID.add(UID.toString());
                                sName.add(StudentName.toString());

                                //Ivycon2/Loginの子要素分繰り返すしかも順番に見ていってくれる
                                for (DataSnapshot loginSnapshot : dataSnapshot.child("Ivycon2").child("Login").child(String.valueOf(calendar.get(Calendar.DATE))).getChildren()) {

                                    //ログインしてるか探しに行く
                                    Online = loginSnapshot.child("UID").getValue();

                                    Flag = false;
                                    if(Online != null) {
                                        //ログインしているかしてないか
                                        Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@Online", Online.toString());
                                        if (UID.toString().equals(Online.toString())) {
                                            Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@Online", "おったわ");
                                            ONorOFF.add(true);
                                            //ログインしてるのが分かればループを抜ける
                                            Flag = true;
                                            break;
                                        }
                                    }
                                }

                                //ログインが見つからなければ
                                if (!Flag) {
                                    ONorOFF.add(false);
                                }

                                ////////////////////////////サムネイルの画像取得処理//////////////////////////////////
                                //画像の参照取得
                                spaceRef = storageRef.child("Image/Icon/" + UID.toString() + "/" + UID.toString() + "_icon.jpeg");

                                //メモリ
                                final long ONE_MEGABYTE = 1024 * 1024;

                                //ストレージイベント
                                spaceRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        //サムネイル画像取得
                                        Thumbnail = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                        //デフォルト画像のビットマップ
                                        Login = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

                                        //緑○を赤にする
                                        if (!ONorOFF.get(Count)) {
                                            Login = setColor(Login, Color.argb(255, 255, 0, 0));
                                        }else{
                                            //緑にする
                                            Login = setColor(Login, Color.argb(255, 0, 255, 0));
                                        }
                                        //リストアイテム作成
                                        TeacherStudentListItem TimelineObject = new TeacherStudentListItem(Thumbnail, sName.get(Count), sUID.get(Count), Login);

                                        //リストに追加
                                        listItems.add(TimelineObject);

                                        Count++;

                                        if (Count == sName.size()) {
                                            //呼出し
                                            OriginalAdapter();

                                            //リストビュー作成
                                            ListView.setAdapter(Adapter);

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                        //デフォルト画像のビットマップ
                                        Thumbnail = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

                                        //ログイン画像のビットマップ
                                        Login = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

                                        //緑○を赤にする
                                        if (!ONorOFF.get(Count)) {
                                            Login = setColor(Login, Color.argb(255, 255, 0, 0));
                                        }else{
                                            //緑にする
                                            Login = setColor(Login, Color.argb(255, 0, 255, 0));
                                        }

                                        //リストアイテム作成
                                        TeacherStudentListItem TimelineObject = new TeacherStudentListItem(Thumbnail, sName.get(Count), sUID.get(Count), Login);

                                        //リストに追加
                                        listItems.add(TimelineObject);

                                        Count++;

                                        if (Count == sName.size()) {
                                            //呼出し
                                            OriginalAdapter();

                                            //リストビュー作成
                                            ListView.setAdapter(Adapter);
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
            @Override
            //データがとりに行けなかった場合
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TeacherStudent.this, "データが取得できませんでした", Toast.LENGTH_SHORT).show();
                Log.w("エラー", databaseError.toException());
            }
        });


    }

    //Bitmapデータをmutable状態にするので、生成時はimutableでもOKです。
    private Bitmap setColor(Bitmap bitmap, int color) {
        //mutable化する
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        bitmap.recycle();

        Canvas myCanvas = new Canvas(mutableBitmap);

        int myColor = mutableBitmap.getPixel(0,0);
        ColorFilter filter = new LightingColorFilter(myColor, color);

        Paint pnt = new Paint();
        pnt.setColorFilter(filter);
        myCanvas.drawBitmap(mutableBitmap,0,0,pnt);

        return mutableBitmap;
    }

    //オリジナルのアダプターを作る
    private void OriginalAdapter(){
        //arraylistに追加
        //アダプターの設定
        Adapter = new TecherStudentListAdapter(this, R.layout.teacher_student_item, listItems);
    }

}

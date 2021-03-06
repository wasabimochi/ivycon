package com.example.a161030.ivycon20;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.Map;

public class StudentTimeline extends AppCompatActivity{

    //ユーザー情報
    public static FirebaseUser user ;

    //リストビュー
    private ListView ListView;

    //リスト
    private ArrayList<StudentListItem> listItems = new ArrayList<>();

    //レイアウト
    private ArrayAdapter<String> arrayAdapter;

    //オリジナルのアダプター
    private StudentListAdapter Adapter;

    //FirebaseAuthオブジェクト作成
    public static FirebaseAuth mAuth;

    //DatabaseReferenceオブジェクト作成
    private DatabaseReference mDatabase;


    //サムネイルのビットマップ
    private Bitmap Thumbnail;

    //入室リスト配列
    private ArrayList<String> inDate = new ArrayList<>();

    //生徒のリスト配列
    private ArrayList<String> sName = new ArrayList<>();

    //生徒のUIDのリスト配列
    private ArrayList<String> sUID = new ArrayList<>();

    //カウント変数
    private int Count = 0;

    //Logを使う時に必要
    private final static String TAG = StudentTimeline.class.getSimpleName();

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final String UUID = "48534442-4C45-4144-80C0-180000000000";

    private static String uuid=null;
    private static boolean Inivy = false; //ivyにいるか

    //ユーザーID取得変数
    public static String myUID;

    //今日の日付
    private Calendar calendar;

    //画像の参照取得
    private ArrayList<StorageReference> spaceRef = new ArrayList<>();

    //ログインの数を計算
    private int LoginCount = 300;

    //ぐるぐるのやつ
    private ProgressDialog progressDialog;

    //ログインの判別
    private boolean ImLogin = false;

    //ステイステイ
    private boolean Stey = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_timeline);

        //2018/9/19　最新版
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.navigationview);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //ここまではいってる
                switch (menuItem.getItemId()) {
                    case R.id.myPage:
                        //インテントの作成
                        Intent myPage = new Intent(getApplication(),StudentMypage.class);
                        startActivity(myPage);
                        //メニュー：ホームがタップされた場合の動作を記述する
                        Log.d(TAG, "マイページがタップされました");
                        break;

                    case R.id.logout:
                        //ログアウト
                        mAuth.signOut();

                        //ユーザーの現在の状況を取得(ログインしているかなど)
                        user = mAuth.getCurrentUser();

                        //ログインしているかどうかの判定
                        if (user != null) { //ログインしていればログを出すだけ
                            Log.d(TAG,"ログインしてる");
                        } else {            //ログインしていなければログを出しログイン画面に遷移する
                            //アラートを表示
                            Toast.makeText(StudentTimeline.this, "ログアウトしました。", Toast.LENGTH_SHORT).show();
                            Intent logout = new Intent(getApplication(),LoginStudent.class);    //インテントの作成
                            startActivity(logout);
                            Log.d(TAG,"ログインしてない");
                            finish();
                        }
                        break;

                    case R.id.taikai:
                        user = mAuth.getCurrentUser();
                        Intent taikai = new Intent(getApplication(),Unsubscribe.class);
                        startActivity(taikai);
                        finish();
                        break;

                    default:
                        break;
                }

                return false;
            }
        });

        //FirebaseAuthオブジェクトの共有インスタンスを取得
        mAuth = FirebaseAuth.getInstance();

        // ログインに成功し、ログインしたユーザーの情報でUIを更新します
        FirebaseUser user = mAuth.getCurrentUser();

        //UIDの取得
        assert user != null;
        myUID = user.getUid();

        BluetoothLeScanner mBluetoothLeScanner;

        //Databaseへの参照取得
        mDatabase = FirebaseDatabase.getInstance().getReference();


        //Listを作る
        ListView = (ListView) findViewById(R.id.ListView);

        // タップ時のイベントを追加
        ListView.setOnItemClickListener(onItemClickListener);

        //今日の日付
        calendar = Calendar.getInstance();

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
                    inDate.clear();
                    spaceRef.clear();
                    Count = 0;
                    progressDialog.dismiss();
                }


                progressDialog = new ProgressDialog(StudentTimeline.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("データの取得をしています");
                progressDialog.setCancelable(true);
                progressDialog.show();

                //自分のデータを取りに行く
                MyDate();

                //Ivycon2/Loginの子要素分繰り返すしかも順番に見ていってくれる
                for (DataSnapshot postSnapshot : dataSnapshot.child("Ivycon2").child("Login").child(String.valueOf(calendar.get(Calendar.DATE))).getChildren()) {

                    //UIDをとってくる
                    Object UID = postSnapshot.child("UID").getValue();

                    //Dataをとってくる
                    Object Data = postSnapshot.child("Data").getValue();

                    //取得データのnullチェック
                    if(UID != null && Data != null) {
                        //UIDを元に名まえを取ってくる
                        Object StudentName = dataSnapshot.child("Ivycon2").child("Student").child(UID.toString()).child("Name").getValue();

                        //nullチェック
                        if(StudentName != null) {
                            //リストに格納
                            sUID.add(UID.toString());
                            sName.add(StudentName.toString());
                            inDate.add(Data.toString());

                            ////////////////////////////サムネイルの画像取得処理//////////////////////////////////
                            //FireBaseストレージ
                            FirebaseStorage storage = FirebaseStorage.getInstance();

                            //ストレージ
                            StorageReference storageRef = storage.getReference();

                            //画像の参照取得
                            spaceRef.add(storageRef.child("Image/Icon/" + UID.toString() + "/" + UID.toString() + "_icon.jpeg"));

                        }
                    }
                }

                //サムネイル取得
                getThumnail();


                int Spacesize = spaceRef.size();
                //ぐるぐるをとめる
                if(Spacesize < 1){
                    progressDialog.dismiss();
                    Stey = true;
                }
            }
            @Override
            //データがとりに行けなかった場合
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StudentTimeline.this, "データが取得できませんでした", Toast.LENGTH_SHORT).show();
                Log.w("エラー", databaseError.toException());
            }
        });


        //バージョンの確認
        if (this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
        }

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        assert bluetoothManager != null;
        BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();

        mBluetoothAdapter.startLeScan(mLeScanCallback);
        //mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();


    }

    //オリジナルのアダプターを作る
    private void OriginalAdapter(){
        //arraylistに追加
        //アダプターの設定
        Adapter = new StudentListAdapter(this, R.layout.student_item, listItems);
    }


    //自分のデータを取りに行く
    private void MyDate() {
        //FireBaseのイベント
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            //一度データを読み込み、そのあとはデータの中身が変わるたびに実行される
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //名前
                Object Name = dataSnapshot.child("Ivycon2").child("Student").child(myUID).child("Name").getValue();

                TextView NameView = findViewById(R.id.textView3);

                NameView.setText(Name.toString());

                ////////////////////////////サムネイルの画像取得処理//////////////////////////////////
                //FireBaseストレージ
                FirebaseStorage storage = FirebaseStorage.getInstance();

                //ストレージ
                StorageReference storageRef = storage.getReference();

                //画像の参照取得
                StorageReference spaceRef = storageRef.child("Image/Icon/" + myUID + "/" + myUID + "_icon.jpeg");

                //メモリ
                final long ONE_MEGABYTE = 1024 * 1024;

                //ストレージイベント
                spaceRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        ImageView image = (ImageView)findViewById(R.id.myIcon);
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

    //サムネイル取得
    private void getThumnail() {
        int Listsize = spaceRef.size();
        if(Count < Listsize) {
            final long ONE_MEGABYTE = 1024 * 1024;
            //ストレージイベント
            spaceRef.get(Count).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    int Listsize = sName.size();
                    //sizeチェック
                    if (Count < Listsize) {
                        //サムネイル画像取得
                        Thumbnail = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                        //リストアイテム作成
                        StudentListItem TimelineObject = new StudentListItem(Thumbnail, sName.get(Count) + inDate.get(Count), sUID.get(Count));

                        //リストに追加
                        listItems.add(TimelineObject);

                        Count++;

                        getThumnail();

                        if (Count == sName.size()) {
                            //呼出し
                            OriginalAdapter();

                            //リストビュー作成
                            ListView.setAdapter(Adapter);

                            //ぐるぐるをとめる
                            progressDialog.dismiss();
                            Stey = true;
                        }

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors

                    //sizeチェック
                    int Listsize = sName.size();
                    //sizeチェック
                    if (Count < Listsize) {
                        //デフォルト画像のビットマップ
                        Thumbnail = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

                        //リストアイテム作成
                        StudentListItem TimelineObject = new StudentListItem(Thumbnail, sName.get(Count) + inDate.get(Count), sUID.get(Count));

                        //リストに追加
                        listItems.add(TimelineObject);

                        Count++;

                        getThumnail();

                        if (Count == sName.size()) {
                            //呼出し
                            OriginalAdapter();

                            //リストビュー作成
                            ListView.setAdapter(Adapter);

                            //ぐるぐるをとめる
                            progressDialog.dismiss();
                            Stey = true;

                        }

                    }
                }
            });
        }
    }

    //ログインデータの書き込み
    private void putData (){
        //FireBaseのイベント
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            //一度データを読み込み、そのあとはデータの中身が変わるたびに実行される
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Ivycon2/Loginの子要素分繰り返すしかも順番に見ていってくれる
                for (DataSnapshot postSnapshot : dataSnapshot.child("Ivycon2").child("Login").child(String.valueOf(calendar.get(Calendar.DATE))).getChildren()) {

                    //UIDをとってくる
                    Object UID = postSnapshot.child("UID").getValue();

                    //取得データのnullチェック
                    if (UID != null) {
                        //キー
                        Object Key = postSnapshot.getKey();

                        int Keyint = Integer.parseInt(Key.toString());

                        if(LoginCount >= Keyint){
                            LoginCount = Integer.parseInt(Key.toString());
                            LoginCount--;

                        }
                        //UIDの比較
                        if (myUID.equals(UID.toString())) {
                            //ログインしてるかの識別
                            ImLogin = true;
                        }
                    }
                }
                //ログインしていなければ実行
                if(!ImLogin){
                    //データを書き込む
                    //書き込む内容
                    //UID
                    Object postValuesUID = myUID;

                    //今日の時間
                    Object postValuesDate  = calendar.get(Calendar.HOUR_OF_DAY);
                    postValuesDate = postValuesDate.toString() + ":" + calendar.get(Calendar.MINUTE);

                    //インスタンス取得
                    Map<String, Object> childUpdates = new HashMap<>();

                    String C = Integer.toString(LoginCount);

                    //当日以外のログインを消す
                    //UID
                    childUpdates.put("/Ivycon2/Login/" + String.valueOf(calendar.get(Calendar.DATE)) + "/" + C + "/UID"  , postValuesUID);
                    //日付
                    childUpdates.put("/Ivycon2/Login/" + String.valueOf(calendar.get(Calendar.DATE)) + "/" + C + "/Data", postValuesDate);

                    //イベント実行
                    mDatabase.updateChildren(childUpdates);

                }
            }
            @Override
            //データがとりに行けなかった場合
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("エラー", databaseError.toException());
            }
        });
    }
        //intデータを 2桁16進数に変換するメソッド
    public String IntToHex2(int i) {
        char hex_2[] = {Character.forDigit((i >> 4) & 0x0f, 16), Character.forDigit(i & 0x0f, 16)};
        String hex_2_str = new String(hex_2);
        return hex_2_str.toUpperCase();
    }

    //タップイベント
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // タップしたアイテムの取得
            ListView listView = (ListView)parent;

            // SampleListItemにキャスト
            StudentListItem item = (StudentListItem)listView.getItemAtPosition(position);

            //グローバル変数クラス
            UtilCommon common = (UtilCommon)getApplication();

            //タップしたところのUIDを取ってグローバルに設定
            common.setothersUID(item.getUID());

            //インテントの作成
            Intent intent = new Intent(getApplication(), OtherPage.class);

            //画面遷移
            startActivity(intent);
        }
    };

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            // デバイスが検出される度に呼び出されます。
            if (scanRecord.length > 30) {
                uuid = null;
                Log.d(TAG,"検索中");

                //iBeacon の場合 6 byte 目から、 9 byte 目はこの値に固定されている。
                if ((scanRecord[5] == (byte) 0x4c) && (scanRecord[6] == (byte) 0x00) &&
                        (scanRecord[7] == (byte) 0x02) && (scanRecord[8] == (byte) 0x15)) {
                    uuid = IntToHex2(scanRecord[9] & 0xff)
                            //取得したパケットをデコードする
                            + IntToHex2(scanRecord[10] & 0xff)
                            + IntToHex2(scanRecord[11] & 0xff)
                            + IntToHex2(scanRecord[12] & 0xff)
                            + "-"
                            + IntToHex2(scanRecord[13] & 0xff)
                            + IntToHex2(scanRecord[14] & 0xff)
                            + "-"
                            + IntToHex2(scanRecord[15] & 0xff)
                            + IntToHex2(scanRecord[16] & 0xff)
                            + "-"
                            + IntToHex2(scanRecord[17] & 0xff)
                            + IntToHex2(scanRecord[18] & 0xff)
                            + "-"
                            + IntToHex2(scanRecord[19] & 0xff)
                            + IntToHex2(scanRecord[20] & 0xff)
                            + IntToHex2(scanRecord[21] & 0xff)
                            + IntToHex2(scanRecord[22] & 0xff)
                            + IntToHex2(scanRecord[23] & 0xff)
                            + IntToHex2(scanRecord[24] & 0xff);

                    String major = IntToHex2(scanRecord[25] & 0xff) + IntToHex2(scanRecord[26] & 0xff);
                    String minor = IntToHex2(scanRecord[27] & 0xff) + IntToHex2(scanRecord[28] & 0xff);

                    //ivyのビーコンと一致した場合
                    if (UUID.equals(uuid)) {
                        Inivy = true;
                        Log.d(TAG,"ようこそ！");
                        //Log.d(TAG,"きたああああああ！ UUID" + uuid + "major" + major + "minor" + minor);
                    }

                    if (!(UUID.equals(uuid))) {
                        Log.d(TAG,"やったぜ！");
                    }
                }
            }
            //ビーコンを見つけたらfirebaseにアクセスさせてステータスを書き換える
            if(Inivy) {
                if(Stey) {
                    //ログイン
                    putData();

                    //スキャン停止
                    final BluetoothManager bluetoothManager =
                            (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
                    assert bluetoothManager != null;
                    BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }
        }
    };

}

package com.example.a161030.ivycon20;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentTimeline extends AppCompatActivity {

    //リストビュー
    ListView ListView;

    //リスト
    ArrayList<StudentListItem> listItems = new ArrayList<>();

    //レイアウト
    ArrayAdapter<String> arrayAdapter;

    //オリジナルのアダプター
    StudentListAdapter Adapter;

    //FirebaseAuthオブジェクト作成
    private FirebaseAuth mAuth;

    //DatabaseReferenceオブジェクト作成
    private DatabaseReference mDatabase;

    //ストレージ
    private StorageReference storageRef;

    //Logを使う時に必要
    private final static String TAG = StudentTimeline.class.getSimpleName();

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final String UUID = "48534442-4C45-4144-80C0-180000000000";

    private static String uuid=null;
    private static boolean Inivy = false; //ivyにいるか


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_timeline);

        //FirebaseAuthオブジェクトの共有インスタンスを取得
        mAuth = FirebaseAuth.getInstance();

        BluetoothLeScanner mBluetoothLeScanner;

        //Databaseへの参照取得
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Listを作る
        ListView = (ListView) findViewById(R.id.ListView);

        //FireBaseのイベント
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            //一度データを読み込み、そのあとはデータの中身が変わるたびに実行される
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Ivycon2/Loginの子要素分繰り返すしかも順番に見ていってくれる
                for (DataSnapshot postSnapshot: dataSnapshot.child("Ivycon2").child("Login").getChildren()) {

                    //Dataをとってくる
                    Object Data = postSnapshot.child("Data").getValue();

                    //UIDをとってくる
                    Object UID = postSnapshot.child("UID").getValue();

                    //UIDを元に名まえを取ってくる
                    Object StudentName = dataSnapshot.child("Ivycon2").child("Student").child(UID.toString()).child("Name").getValue();

                    /*
                    Bitmap Thumbnail;

                    //ストレージへの参照
                    storageRef = storage.getReference();

                    //画像の参照取得
                    StorageReference spaceRef = storageRef.child("smile.png");

                    final long ONE_MEGABYTE = 1024 * 1024;
                    spaceRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            // Data for "images/island.jpg" is returns, use this as needed
                            Bitmap Thumbnail = BitmapFactory.decodeByteArray(bytes,0,bytes.length);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
*/
                    //画像のビットマップ
                    Bitmap Thumbnail = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

                    //リストアイテム作成
                    StudentListItem TimelineObject = new StudentListItem(Thumbnail, StudentName.toString() + Data.toString());

                    //リストに追加
                    listItems.add(TimelineObject);


                }

                //呼出し
                OriginalAdapter();

                //リストビュー作成
                ListView.setAdapter(Adapter);
            }

            @Override
            //データがとりに行けなかった場合
            public void onCancelled(@NonNull DatabaseError databaseError) {
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

    //intデータを 2桁16進数に変換するメソッド
    public String IntToHex2(int i) {
        char hex_2[] = {Character.forDigit((i >> 4) & 0x0f, 16), Character.forDigit(i & 0x0f, 16)};
        String hex_2_str = new String(hex_2);
        return hex_2_str.toUpperCase();
    }


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
                        //uuid = "48534442-4C45-4144-80C0-18FFFFFFFFFF";
                        Log.d(TAG,"きたああああああ！ UUID" + uuid + "major" + major + "minor" + minor);
                    }

                    if (!(UUID.equals(uuid))) {
                        Log.d(TAG,"やったぜ！");
                    }
                }
            }
            //ビーコンを見つけたらfirebaseにアクセスさせてステータスを書き換える
            if(Inivy) {
                //書き込む内容
                Object postValues = "beaconからのプレセント!";
                //インスタンス取得
                Map<String, Object> childUpdates = new HashMap<>();
                //データ書き込みのイベント複数セッティング
                childUpdates.put("/Ivycon/Student/161019/Profile/", postValues);
                //イベント実行
                mDatabase.updateChildren(childUpdates);
            }
        }
    };

}

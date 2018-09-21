package com.example.a161030.ivycon20;

import android.Manifest;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.pm.PackageManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.content.Intent;

//このクラスいらないです

public class BeaconManager extends AppCompatActivity {

    //Logを使う時に必要
    private final static String TAG = StudentTimeline.class.getSimpleName();
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final String UUID = "48534442-4C45-4144-80C0-180000000000";
    private static String uuid = null;
    static boolean InIvy;
    StudentTimeline studentTimeline = new StudentTimeline();

    //ビーコンの捜査
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //頭にappthemaつけないとスプラッシュのまんまになるので注意
        setTheme(R.style.AppTheme);
        setContentView(R.layout.beacon_manager);
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        assert bluetoothManager != null;
        BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
        mBluetoothAdapter.startLeScan(mLeScanCallback);
        Log.d(TAG, "in");
    }

    //intデータを 2桁16進数に変換するメソッド
    public String IntToHex2(int i) {
        char hex_2[] = {Character.forDigit((i >> 4) & 0x0f, 16), Character.forDigit(i & 0x0f, 16)};
        String hex_2_str = new String(hex_2);
        return hex_2_str.toUpperCase();
    }

    public BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            // デバイスが検出される度に呼び出されます。
            Log.d(TAG, "scanning");
            if (scanRecord.length > 30) {
                uuid = null;
                Log.d(TAG, "検索中");

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
                        InIvy = true;
                        Log.d(TAG, "ようこそ！");
                        Log.d(TAG, "きたああああああ！ UUID" + uuid + "major" + major + "minor" + minor);
                        Intent intent = new Intent();
                        intent.putExtra("inIvy",1);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    if (!(UUID.equals(uuid))) {
                        Log.d(TAG, "やったぜ！");
                    }
                }
            }
        }
    };
}

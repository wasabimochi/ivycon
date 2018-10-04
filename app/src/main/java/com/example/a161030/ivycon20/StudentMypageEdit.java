package com.example.a161030.ivycon20;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class StudentMypageEdit extends AppCompatActivity {

    //FirebaseAuthオブジェクト生成
    private FirebaseAuth mAuth;

    //DatabaseReferenceオブジェクト作成
    private DatabaseReference mDatabase;

    //FirebaseUserオブジェクト作成
    FirebaseUser user;

    private static final int REQUEST_GALLERY = 0;

    private ImageView imgView;

    //えらばれた画像を入れる変数
    Bitmap img,img2,Thumbnail;

    //FireBaseストレージ
    private FirebaseStorage storage;
    //ストレージ
    private StorageReference storageRef;

    //ユーザーID取得変数
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.student_mypage_edit);

        //FirebaseAuthオブジェクトの共有インスタンスを取得
        mAuth = FirebaseAuth.getInstance();

        //ユーザーの現在の状況を取得(ログインしているかなど)
        user = FirebaseAuth.getInstance().getCurrentUser();

        //Reference取得
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //FireBaseストレージへのアクセスインスタンス
        storage = FirebaseStorage.getInstance();

        //ストレージへの参照
        storageRef = storage.getReference();

        //選択した写真を表示させる
        imgView = (ImageView)findViewById(R.id.icon_edit);

        // ログインに成功し、ログインしたユーザーの情報でUIを更新します
        FirebaseUser user = mAuth.getCurrentUser();

        //UIDの取得
        assert user != null;
        UID = user.getUid();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            //一度データを読み込み、そのあとはデータの中身が変わるたびに実行される
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Ivycon2/Student/UID/Nameの子要素取得
                Object name = dataSnapshot.child("Ivycon2").child("Student").child(UID).child("Name").getValue();

                //Ivycon2/Student/UID/Nameの子要素取得
                Object depar = dataSnapshot.child("Ivycon2").child("Student").child(UID).child("Depar").getValue();
                Object student_depar = dataSnapshot.child("Ivycon2").child("Deper").child(depar.toString()).getValue();

                //Ivycon2/Student/UID/Nameの子要素取得
                Object num = dataSnapshot.child("Ivycon2").child("Student").child(UID).child("Num").getValue();

                //設定するTextViewを取得
                TextView name_text = findViewById(R.id.edit_name);
                TextView depar_text = findViewById(R.id.edit_depar);
                TextView num_text = findViewById(R.id.edit_num);

                //テキストをセットする
                name_text.setText(name.toString());
                depar_text.setText(student_depar.toString());
                num_text.setText(num.toString());
            }
            @Override
            //データがとりに行けなかった場合
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("エラー", databaseError.toException());
            }
        });


        // ギャラリー呼び出し
        findViewById(R.id.icon_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intentのインスタンスを生成
                Intent intent = new Intent();

                //Intent#setTypeメソッドで, 画像全般("image/*")を指定する
                intent.setType("image/*");

                //Intent#setActionメソッドで, Intent.ACTION_GET_CONTENTを指定する
                intent.setAction(Intent.ACTION_GET_CONTENT);

                //startActivityForResuitメソッドで, リクエストコードを指定してインテント呼出しする
                startActivityForResult(intent, REQUEST_GALLERY);
            }
        });

        findViewById(R.id.decision_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ストレージの参照                                       directory/directory/ファイル名
                StorageReference mountainImagesRef = storageRef.child("Image/Icon/" + UID + "/" + UID + ".jpeg");
                StorageReference mountainImagesRef2 = storageRef.child("Image/Icon/" + UID + "/" + UID + "_icon.jpeg");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ByteArrayOutputStream baos2 = new ByteArrayOutputStream();

                if(img != null && Thumbnail != null) {
                    img2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    Thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos2);
                    byte[] data = baos.toByteArray();
                    byte[] data2 = baos2.toByteArray();


                    UploadTask uploadTask = mountainImagesRef.putBytes(data);
                    UploadTask uploadTask2 = mountainImagesRef2.putBytes(data2);

                    // Register observers to listen for when the download is done or if it fails
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Log.w("アップロード", "失敗");

                            //アラートを表示
                            Toast.makeText(StudentMypageEdit.this, "更新に失敗しました。", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            Log.w("アップロード", "成功");

                            //登録する項目を取得する
                            final EditText profile = findViewById(R.id.profile_edit);

                            //書き込む内容
                            Object prof = profile.getText().toString();

                            //ミリ秒を入れて強制的にデータを取りに行かせる
                            Calendar calendar = Calendar.getInstance();

                            Object NowMin = calendar.get(Calendar.MILLISECOND);
                            //インスタンス取得
                            Map<String, Object> childUpdates = new HashMap<>();

                            //プロフィール
                            childUpdates.put("/Ivycon2/Student/" + UID + "/Prof", prof);

                            //////
                            //プロフィール
                            childUpdates.put("/Edit", NowMin);

                            //イベント実行
                            mDatabase.updateChildren(childUpdates);

                            Toast.makeText(StudentMypageEdit.this, "更新しました", Toast.LENGTH_SHORT).show();
                            // ...

                            finish();
                        }
                    });
                }else{
                    //登録する項目を取得する
                    final EditText profile = findViewById(R.id.profile_edit);

                    //書き込む内容
                    Object prof = profile.getText().toString();

                    //ミリ秒を入れて強制的にデータを取りに行かせる
                    Calendar calendar = Calendar.getInstance();

                    Object NowMin = calendar.get(Calendar.MILLISECOND);
                    //インスタンス取得
                    Map<String, Object> childUpdates = new HashMap<>();

                    //プロフィール
                    childUpdates.put("/Ivycon2/Student/" + UID + "/Prof", prof);

                    //プロフィール
                    childUpdates.put("/Edit", NowMin);

                    //イベント実行
                    mDatabase.updateChildren(childUpdates);

                    Toast.makeText(StudentMypageEdit.this, "更新しました", Toast.LENGTH_SHORT).show();

                    finish();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //リクエストコードをチェックし, ギャラリーからのイベントか判断する
        if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            try {
                //ContentResolver#openInputStremaメソッドで, InputStreamをオープンする
                InputStream in = getContentResolver().openInputStream(data.getData());

                //BitmapFactory#decodeStreamメソッドで, ビットマップに変換する
                img = BitmapFactory.decodeStream(in);
                img2 = img;
                Thumbnail = img;

                //InputStream#closeメソッドで, InputStreamをクローズする
                in.close();

                //取得した画像をサムネイル用の大きさにリサイズ
                Thumbnail = Bitmap.createScaledBitmap(img, 70, 70, false);
                img2 = Bitmap.createScaledBitmap(img, 205, 205, false);

                // 選択した画像を表示
                imgView.setImageBitmap(img2);

            } catch (Exception e) {
                //アラートを表示
                Toast.makeText(StudentMypageEdit.this, "Exceptionでた", Toast.LENGTH_SHORT).show();
            }
        }
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
}

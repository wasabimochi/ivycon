package com.example.a161030.ivycon20;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StudentMypageEdit extends AppCompatActivity {

    private static final int REQUEST_GALLERY = 0;

    private ImageView imgView;

    //えらばれた画像を入れる変数
    Bitmap img,Thumbnail;

    //FireBaseストレージ
    private FirebaseStorage storage;
    //ストレージ
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.student_mypage_edit);

        //FireBaseストレージへのアクセスインスタンス
        storage = FirebaseStorage.getInstance();

        //ストレージへの参照
        storageRef = storage.getReference();

        imgView = (ImageView)findViewById(R.id.icon_edit);

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
                StorageReference mountainImagesRef = storageRef.child("Image/Icon/test.jpeg");
                //アップロード
                //UploadTask uploadTask = mountainImagesRef.putFile(img);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainImagesRef.putBytes(data);
                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Log.w("アップロード","失敗");

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        Log.w("アップロード","成功");

                        // ...
                    }
                });
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
                Thumbnail = img;

                //InputStream#closeメソッドで, InputStreamをクローズする
                in.close();

                // 選択した画像を表示
                imgView.setImageBitmap(img);

                //取得した画像をサムネイル用の大きさにリサイズ
                Thumbnail = Bitmap.createScaledBitmap(img, 70, 70, false);
            } catch (Exception e) {
                //アラートを表示
                Toast.makeText(StudentMypageEdit.this, "Exceptionでた", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

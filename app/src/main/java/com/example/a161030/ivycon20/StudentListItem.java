package com.example.a161030.ivycon20;

import android.graphics.Bitmap;

public class StudentListItem {

    private Bitmap mThumbnail = null;
    private String mTitle = null;
    private String mUID = null;

    /**
     * 空のコンストラクタ
     */
    public StudentListItem() {};

    /**
     * コンストラクタ
     * @param thumbnail サムネイル画像
     * @param title タイトル
     * @param UID UID
     */
    public StudentListItem(Bitmap thumbnail, String title, String UID) {
        mThumbnail = thumbnail;
        mTitle = title;
        mUID = UID;
    }

    /**
     * サムネイル画像を設定
     * @param thumbnail サムネイル画像
     */
    public void setThumbnail(Bitmap thumbnail) {
        mThumbnail = thumbnail;
    }

    /**
     * タイトルを設定
     * @param title タイトル
     */
    public void setmTitle(String title) {
        mTitle = title;
    }

    /**
     * サムネイル画像を取得
     * @return サムネイル画像
     */
    public Bitmap getThumbnail() {
        return mThumbnail;
    }

    /**
     * タイトルを取得
     * @return タイトル
     */
    public String getTitle() {
        return mTitle;
    }


    /**
     * UIDを取得
     * @return UID
     */
    public String getUID() {
        return mUID;
    }
}

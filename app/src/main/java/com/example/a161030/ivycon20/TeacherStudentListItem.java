package com.example.a161030.ivycon20;

import android.graphics.Bitmap;

public class TeacherStudentListItem {

    private Bitmap mThumbnail = null;
    private String mTitle = null;
    private String mUID = null;
    private Bitmap mLogin = null;

    /**
     * 空のコンストラクタ
     */
    public TeacherStudentListItem() {};

    /**
     * コンストラクタ
     * @param thumbnail サムネイル画像
     * @param title タイトル
     * @param UID UID
     */
    public TeacherStudentListItem(Bitmap thumbnail, String title, String UID, Bitmap Login) {
        mThumbnail = thumbnail;
        mTitle = title;
        mUID = UID;
        mLogin = Login;
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
     * タイトルを設定
     * @param Login ログイン情報
     */
    public void setmLogin(Bitmap Login) {
        mLogin = Login;
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
     * @return UIDの情報
     */
    public String getUID() {
        return mUID;
    }

    /**
     * ログイン情報を取得
     * @return ログインの情報
     */
    public Bitmap getLogin() {
        return mLogin;
    }

}

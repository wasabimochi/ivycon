package com.example.a161030.ivycon20;

import android.app.Application;

import java.util.ArrayList;

//UIDを取得するクラス
public class UtilCommon extends Application {

    private String othersUID = null;

    //オンラインかオフラインか
    private ArrayList<Boolean> ONorOFF = new ArrayList<Boolean>();

    //カウントの変数
    private int Count = 0;

    //学年
    private int Tyear = 0;

    //学科
    private String Depar = null;

    /**
     * 他の人のUIDを設定
     * @param UID 他の人のUID
     */
    public void setothersUID(String UID) {
        othersUID = UID;
    }

    /**
     * 他の人のUIDを取得
     * @return 他の人のUID
     */

    public String getothersUID() {
        return othersUID;
    }

    /**
     * 生徒の年Yearを設定
     * @param Year  生徒の年Year
     */
    public void setTyear(int Year) {
        Tyear = Year;
    }

    /**
     * 生徒の年Yearを取得
     * @return 生徒の年Year
     */

    public int getTyear() {
        return Tyear;
    }

    /**
     * 生徒のDeparを設定
     * @param dep 生徒の学科Depar
     */
    public void setDepar(String dep) {
        Depar = dep;
    }

    /**
     * 生徒のDeparを取得
     * @return 生徒のDepar
     */

    public String getDepar() {
        return Depar;
    }

}

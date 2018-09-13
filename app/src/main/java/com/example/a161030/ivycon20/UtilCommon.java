package com.example.a161030.ivycon20;

import android.app.Application;
//UIDを取得するクラス
public class UtilCommon extends Application {

    private String othersUID = null;

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

}

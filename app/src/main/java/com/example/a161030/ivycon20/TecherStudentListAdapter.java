package com.example.a161030.ivycon20;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TecherStudentListAdapter extends ArrayAdapter<TeacherStudentListItem> {

    private int mResource;
    private List<TeacherStudentListItem> mItems;
    private LayoutInflater mInflater;

    /**
     * コンストラクタ
     * @param context コンテキスト
     * @param resource リソースID
     * @param items リストビューの要素
     */
    public TecherStudentListAdapter(Context context, int resource, List<TeacherStudentListItem> items) {
        super(context, resource, items);

        mResource = resource;
        mItems = items;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = mInflater.inflate(mResource, null);
        }

        // リストビューに表示する要素を取得
        TeacherStudentListItem item = mItems.get(position);

        // サムネイル画像を設定
        ImageView thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        thumbnail.setImageBitmap(item.getThumbnail());

        // タイトルを設定
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(item.getTitle());

        //ログインの情報
        ImageView Login = (ImageView) view.findViewById(R.id.login);
        Login.setImageBitmap(item.getLogin());

        return view;
    }
}

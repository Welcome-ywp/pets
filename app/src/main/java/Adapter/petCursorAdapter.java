package Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.ywp.yi.pets.R;

import data.petContract.petEntry;

/**
 * Created by yi on 2017/9/17.
 */

public class petCursorAdapter extends CursorAdapter {
    public petCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        //返回布局
        return LayoutInflater.from(context).inflate(R.layout.layout_petslist, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (cursor == null){
            return;
        }
        //获取id
        TextView petName = (TextView) view.findViewById(R.id.tv_petNme);
        TextView petBreed = (TextView) view.findViewById(R.id.tv_petBreed);
        //获取数据
        String name = cursor.getString(cursor.getColumnIndex(petEntry.PET_NAME));
        String breed = cursor.getString(cursor.getColumnIndex(petEntry.PET_BREED));
        //更新UI
        petName.setText(name);
        petBreed.setText(breed);
    }
}

package Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ywp.yi.pets.R;
import com.ywp.yi.pets.petsList;

import java.util.ArrayList;

/**
 * Created by YI on 2017/8/24.
 */

public class petsListAdapter extends ArrayAdapter<petsList> {
    public petsListAdapter(@NonNull Context context, ArrayList<petsList> petsLists) {
        super(context, 0, petsLists);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate
                    (R.layout.layout_petslist,parent,false);
        }

        petsList list = getItem(position);
        //给TextView绑定Id
        TextView tvPetName = (TextView) convertView.findViewById(R.id.tv_petNme);
        TextView tvPetBreed = (TextView) convertView.findViewById(R.id.tv_petBreed);

        tvPetName.setText(list.getPetName());
        tvPetBreed.setText(list.getPetBreed());

        return convertView;
    }
}

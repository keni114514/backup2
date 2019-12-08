package com.hiramine.bledataglancer;

import android.content.ClipData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<ListData> {
    private LayoutInflater layoutInflater;

    public ListViewAdapter(Context context, int resource, List<ListData> objects) {
        super(context, resource, objects);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        ListData num = new ListData();

        ListData data = (ListData)getItem(position);
        ListData Menu = (ListData)getItem(position);
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        

        TextView noText;
        TextView guzaiText;
        TextView cminText;
        TextView cmaxText;
        TextView timeText;
        TextView mes;

        noText = (TextView)convertView.findViewById(R.id.no);
        guzaiText = (TextView)convertView.findViewById(R.id.guzai);
        cminText = (TextView)convertView.findViewById(R.id.cmin);
        cmaxText = (TextView)convertView.findViewById(R.id.cmax);
        timeText = (TextView)convertView.findViewById(R.id.time1);
        mes = (TextView)convertView.findViewById(R.id.mes);

        noText.setText(data.getNo());
        guzaiText.setText(data.getGuzai());
        cminText.setText(data.getCmin());
        cmaxText.setText(data.getCmax());
        timeText.setText(data.getTime());
        mes.setText(data.getMes());

        //guzaiText = (TextView)convertView.findViewById(R.id.guzai);

        //guzaiText.setText(Menu.getGuzai());



        return convertView;
    }
}
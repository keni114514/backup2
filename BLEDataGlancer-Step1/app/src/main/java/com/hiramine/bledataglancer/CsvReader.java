package com.hiramine.bledataglancer;

import android.content.Context;
import android.content.res.AssetManager;
import android.inputmethodservice.Keyboard;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vuzix.hud.actionmenu.ActionMenuActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CsvReader extends ActionMenuActivity {
        List<ListData> objects = new ArrayList<ListData>();
        public void reader(Context context) {
            AssetManager assetManager = context.getResources().getAssets();
            try {
                // CSVファイルの読み込み
                InputStream inputStream = assetManager.open("resipi.csv");
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferReader = new BufferedReader(inputStreamReader);
                String line;
                int i = 0;

                while ((line = bufferReader.readLine()) != null) {

                    //カンマ区切りで１つづつ配列に入れる
                    ListData data = new ListData();

                    ListData Menu = new ListData();
                    String[] RowData = line.split(",");

                    String menu[][];
                    menu = new String[40][6];

                    for(int k = 0;k < RowData.length;k++){
                        menu[i][k] = RowData[k];
                    }

                    i++;

                    ListView listView = (ListView)findViewById(R.id.list);

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Collections.singletonList(menu[1][2]));
                    listView.setAdapter(arrayAdapter);



                    //String[] menu = new String[i];


                    //CSVの左([0]番目)から順番にセット
                    /*
                    data.setNo(RowData[0]);
                    data.setGuzai(RowData[1]);
                    data.setCmin(RowData[2]);
                    data.setCmax(RowData[3]);
                    data.setTime(RowData[4]);
                    data.setMes(RowData[5]);

                    objects.add(data);

                     */


                }
                bufferReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}

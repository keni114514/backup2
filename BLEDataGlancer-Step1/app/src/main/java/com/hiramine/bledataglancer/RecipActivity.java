package com.hiramine.bledataglancer;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;

import com.vuzix.hud.actionmenu.ActionMenuActivity;

import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.StringTokenizer;
import android.app.Activity;
import android.widget.TextView;
import android.widget.Toast;


public class RecipActivity extends ActionMenuActivity {

    ListView listView;
    private Button mButtonNext;
    private Button mButtonMain;
    private Button mButtonGuzai;


    private Handler mHandler = new Handler();
    /** テキストオブジェクト */
    private Runnable updateText;

    private int ond = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recip);

        mButtonNext = (Button)findViewById( R.id.button_next );

        mButtonMain = (Button)findViewById( R.id.button_main );

        mButtonGuzai = (Button)findViewById( R.id.button_guzai );


        mButtonNext.setVisibility(View.GONE);
        mButtonMain.setVisibility(View.GONE);
        mButtonGuzai.setVisibility(View.GONE);
        findViewById(R.id.value_table).setVisibility(View.GONE);

        Intent intent = this.getIntent();
        final String text = intent.getStringExtra("sendvalue");

        listView = (ListView)findViewById(R.id.list);

        final String menu[][];
        menu = new String[40][6];

        final int num[];
        num = new int[10];


        int x = 0;
        String menu2[];
        menu2 = new String[10];
        ArrayList<String> list = new ArrayList<String>();



        try {
            InputStream inputStream = getResources().getAssets().open("resipi.csv");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            String line;
            int i = 0;

            while ((line = bufferReader.readLine()) != null) {
                String[] RowData = line.split(",",-1);


                for(int k = 0;k < RowData.length;k++){
                    menu[i][k] = RowData[k];
                }

                if(Arrays.deepEquals(new String[]{menu[i][0]}, new String[]{menu[1][0]})){
                    list.add(menu[i-1][1]);
                    num[x] = i-1;
                    x++;
                }

                i++;


            }
            bufferReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Collections.singletonList(menu[39][1]));
        //listView.setVisibility(View.VISIBLE);
        //final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);
        listView.setVisibility(View.VISIBLE);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                listView.setVisibility(View.GONE);
                final TextView textView = (TextView) findViewById(R.id.textView);
                final TextView textView2 = (TextView) findViewById(R.id.textView2);
                final TextView textView3 = (TextView) findViewById(R.id.textView3);
                //final TextView textView3 = (TextView) findViewById(R.id.textView);
                //textView.setText(menu[num[position]][5]);
                //textView.setText(text);

                findViewById(R.id.value_table).setVisibility(View.VISIBLE);
                mButtonNext.setVisibility(View.VISIBLE);
                mButtonMain.setVisibility(View.VISIBLE);


                textView.setText(menu[num[position]][1]);
                //textView.setText(Integer.toString(num[position]));
                mButtonNext.setOnClickListener(new View.OnClickListener() {
                    int z = 0;;
                    @Override
                    public void onClick(View view) {
                            z++;
                            if (Arrays.deepEquals(new String[]{menu[num[position] + z][0]}, new String[]{menu[num[position] + z][0]})) {
                                int x = Integer.parseInt(menu[num[position]][2]);
                                int y = Integer.parseInt(menu[num[position] + 1][3]);
                                final int t = Integer.parseInt(menu[num[position]+2][4]);
                                String cmin = menu[num[position]][2];
                                String mes = menu[num[position]][5];
                                String q = mes.replace("CMIN", cmin);

                                String cmax = menu[num[position] + 1][3];


                                textView.setText(q);

                                textView2.setText(Integer.toString(ond));
                                ond++;
                                if(ond > x){
                                    String mes2 = menu[num[position]+1][5];
                                    String guzai = menu[num[position]][1];
                                    String p = mes2.replace("{GUZAI}",guzai);
                                    textView.setText("必要な温度まで達しました"+p + cmin +"℃～" + cmax + "℃の間を維持してください");

                                    mButtonGuzai.setVisibility(View.VISIBLE);

                                    //textView.setText(cmax);
                                    mButtonGuzai.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {


                                            mButtonGuzai.setVisibility(View.GONE);
                                            mButtonNext.setVisibility(View.GONE);
                                            final Handler handler = new Handler();
                                            final Runnable r = new Runnable() {
                                                int  time = Integer.parseInt(menu[num[position]+2][4]);
                                                String ti ="";
                                                @Override
                                                public void run() {
                                                    if(time == 0){
                                                        textView.setText("具材を取り出してください");
                                                        textView2.setText("完成です！");
                                                        return;
                                                    }
                                                    time--;
                                                    ti = Integer.toString(time);
                                                    textView.setText(menu[num[position]+2][5]);
                                                    textView3.setText("残り時間" + ti);
                                                    textView2.setText("現在の温度" + ond + "℃");



                                                    handler.postDelayed(this,1000);
                                                }
                                            };
                                            handler.post(r);





                                        }
                                    });
                                }
                                if(ond > y){
                                    textView.setText("火力を下げてください");
                                }


                            }
                    }
                });
                mButtonMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

            }
        });

    }


}


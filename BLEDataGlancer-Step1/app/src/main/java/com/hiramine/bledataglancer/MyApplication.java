package com.hiramine.bledataglancer;

import android.app.Application;

public class MyApplication extends Application {

    private String value = "";
    @Override
    public void onCreate() {
        //アプリケーションクラス作成時
        super.onCreate();
    }
    public String getValue(){

        return this.value;
    }
    public void setValue(String value){
        this.value = value;
    }
}

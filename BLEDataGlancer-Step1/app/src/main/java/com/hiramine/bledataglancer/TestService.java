package com.hiramine.bledataglancer;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vuzix.hud.actionmenu.ActionMenuActivity;

import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


import android.support.annotation.Nullable;

public class TestService extends Service {

    private Timer mTimer = null;
    Handler mHandler = new Handler();

    @Override
    public void onCreate() {
        Log.i("TestService", "onCreate");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("TestService", "onStartCommand");

        // タイマーの設定 1秒毎にループ
        mTimer = new Timer(true);
        mTimer.schedule( new TimerTask(){
            @Override
            public void run(){
                mHandler.post( new Runnable(){
                    public void run(){
                        Log.d( "TestService" , "Timer run" );
                    }
                });
            }
        }, 1000, 1000);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("TestService", "onDestroy");

        // タイマー停止
        if( mTimer != null ){
            mTimer.cancel();
            mTimer = null;
        }
        Toast.makeText(this, "MyService　onDestroy", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.i("TestService", "onBind");
        return null;
    }

}
package com.hiramine.bledataglancer;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import junit.framework.Test;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;



import android.support.annotation.Nullable;

public class TestServiceStart extends Service {

    // 定数（Bluetooth LE Gatt UUID）
    // Private Service
    private static final UUID UUID_SERVICE_PRIVATE         = UUID.fromString( "0000181A-0000-1000-8000-00805F9B34FB" );
    private static final UUID UUID_CHARACTERISTIC_PRIVATE1 = UUID.fromString( "00002A6E-0000-1000-8000-00805F9B34FB" );
    // for Notification
    private static final UUID UUID_NOTIFY                  = UUID.fromString( "00002902-0000-1000-8000-00805F9B34FB" );

    // 定数
    private static final int REQUEST_ENABLEBLUETOOTH = 1; // Bluetooth機能の有効化要求時の識別コード
    private static final int REQUEST_CONNECTDEVICE   = 2; // デバイス接続要求時の識別コード

    // メンバー変数
    private BluetoothAdapter mBluetoothAdapter;    // BluetoothAdapter : Bluetooth処理で必要
    private BluetoothGatt mBluetoothGatt = null;    // Gattサービスの検索、キャラスタリスティックの読み書き

//---------------------------------------------------------------------------------------------------------------------
    private boolean mScanned = false;

    private final int PERMISSION_REQUEST = 100;

    @Nullable

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // to do something
        // BluetoothGattコールバックオブジェクト
         final BluetoothGattCallback mGattcallback = new BluetoothGattCallback()
        {
            // 接続状態変更（connectGatt()の結果として呼ばれる。）
            @Override
            public void onConnectionStateChange( BluetoothGatt gatt, int status, int newState )
            {
                if( BluetoothGatt.GATT_SUCCESS != status )
                {
                    return;
                }

                if( BluetoothProfile.STATE_CONNECTED == newState )
                {    // 接続完了
                    mBluetoothGatt.discoverServices();    // サービス検索

                    return;
                }
                if( BluetoothProfile.STATE_DISCONNECTED == newState )
                {    // 切断完了（接続可能範囲から外れて切断された）
                    // 接続可能範囲に入ったら自動接続するために、mBluetoothGatt.connect()を呼び出す。
                    mBluetoothGatt.connect();

                    return;
                }
            }

            // サービス検索が完了したときの処理（mBluetoothGatt.discoverServices()の結果として呼ばれる。）
            @Override
            public void onServicesDiscovered( BluetoothGatt gatt, int status )
            {
                if( BluetoothGatt.GATT_SUCCESS != status )
                {
                    return;
                }

                // 発見されたサービスのループ
                for( BluetoothGattService service : gatt.getServices() )
                {
                    // サービスごとに個別の処理
                    if( ( null == service ) || ( null == service.getUuid() ) )
                    {
                        continue;
                    }
                    if( UUID_SERVICE_PRIVATE.equals( service.getUuid() ) )
                    {    // プライベートサービス
                        setCharacteristicNotification( UUID_SERVICE_PRIVATE, UUID_CHARACTERISTIC_PRIVATE1, true );
                        continue;
                    }
                }
            }

            // キャラクタリスティックが読み込まれたときの処理
            @Override
            public void onCharacteristicRead( BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status )
            {
                if( BluetoothGatt.GATT_SUCCESS != status )
                {
                    return;
                }
                // キャラクタリスティックごとに個別の処理
            }

            // キャラクタリスティック変更が通知されたときの処理
            @Override
            public void onCharacteristicChanged( BluetoothGatt gatt, BluetoothGattCharacteristic characteristic )
            {
                // キャラクタリスティックごとに個別の処理
                if( UUID_CHARACTERISTIC_PRIVATE1.equals( characteristic.getUuid() ) )
                {    // キャラクタリスティック１：データサイズは、2バイト（数値を想定。0～65,535）
                    byte[] byteChara    = characteristic.getValue();
                    String strChar_temp = "";
                    if( 0 == byteChara.length )
                    {
                        strChar_temp = "";
                    }
                    else if( 1 == byteChara.length )
                    {    // 00～FF : 0～255
                        int iValue = (char)byteChara[0];
                        strChar_temp = String.valueOf( iValue );
                    }
                    else if( 2 == byteChara.length )
                    {    // 0000～FFFF : 0～65,535
                        ByteBuffer bb     = ByteBuffer.wrap( byteChara );
                        int        iValue = (char)bb.getShort();
                        strChar_temp = String.valueOf( iValue );
                    }
                    else if( 4 == byteChara.length )
                    {    // 00000000～FFFFFFFF : -2,147,483,648～2,147,483,647
                        ByteBuffer bb     = ByteBuffer.wrap( byteChara );
                        int        iValue = bb.getInt();
                        strChar_temp = String.valueOf( iValue );
                    }
                    else
                    {
                        strChar_temp = "";
                    }
                    final String strChara = strChar_temp;

                    return;
                }
            }
            private void setCharacteristicNotification( UUID uuid_service, UUID uuid_characteristic, boolean enable )
            {
                if( null == mBluetoothGatt )
                {
                    return;
                }
                BluetoothGattCharacteristic blechar = mBluetoothGatt.getService( uuid_service ).getCharacteristic( uuid_characteristic );
                mBluetoothGatt.setCharacteristicNotification( blechar, enable );
                BluetoothGattDescriptor descriptor = blechar.getDescriptor( UUID_NOTIFY );
                descriptor.setValue( BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE );
                mBluetoothGatt.writeDescriptor( descriptor );
            }
        };
        Intent intent1 = new Intent(this,RecipActivity.class);
        intent1.putExtra("sendvalue","111111");

        Toast.makeText(this, "qqqqqqqq", Toast.LENGTH_SHORT).show();


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
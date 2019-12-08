package com.hiramine.bledataglancer;

import android.app.Activity;
import android.app.Service;
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
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.UUID;

import android.support.annotation.Nullable;

public class BleService extends Service
{

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public int onStartCommand(Intent intent, int flags, int startId){
		sendMessage("こんにちは");
		stopSelf();
		return START_NOT_STICKY;
	}

	protected void sendMessage(String msg){
		Intent broadcast = new Intent();
		broadcast.putExtra("message", msg);
		broadcast.setAction("DO_ACTION");
		getBaseContext().sendBroadcast(broadcast);
	}

}

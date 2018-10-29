package com.example.aidldemo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.aidldemo.aidl.Book;
import com.example.aidldemo.aidl.BookMangerService;
import com.example.aidldemo.aidl.IBookManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ServiceConnection conn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager bookManager=IBookManager.Stub.asInterface(service);
            try {
                List<Book> bookList = bookManager.getBookList();
                Log.e(TAG, "onServiceConnected: getBookList type"
                        +bookList.getClass().getCanonicalName() );
                Log.e(TAG, "onServiceConnected: "+bookList.toString() );

            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, BookMangerService.class);
        bindService(intent,conn,Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(conn);
        super.onDestroy();

    }
}

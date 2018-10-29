package com.example.aidldemo;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.aidldemo.aidl.Book;
import com.example.aidldemo.aidl.BookMangerService;
import com.example.aidldemo.aidl.IBookManager;
import com.example.aidldemo.aidl.IOnNewBookArrivedListener;

import java.util.List;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int MESSAGE_BOOK_ARRIVED = 1;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRemoteBookManager = IBookManager.Stub.asInterface(service);
            try {
                /**
                 * 难点，客户端调用远程服务的方法时，被调用的方法运行在服务端的binder线程池中，
                 * 同时，客户端线程挂起，如果服务端方法耗时，可能会导致anr，
                 * 服务端方法若耗时，也不必开线程
                 *
                 */
                List<Book> bookList = mRemoteBookManager.getBookList();
                Log.e(TAG, "onServiceConnected: getBookList type"
                        + bookList.getClass().getCanonicalName());
                Log.e(TAG, "onServiceConnected: " + bookList.toString());
                mRemoteBookManager.addBook(new Book(3, "android 进阶"));
                Log.e(TAG, "onServiceConnected: " + mRemoteBookManager.getBookList().toString());
                mRemoteBookManager.registerListener(mOnBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    IOnNewBookArrivedListener.Stub mOnBookArrivedListener =
            new IOnNewBookArrivedListener.Stub() {
                /**
                 * 服务端调用客户端listener方法，被调用方法运行在binder线程池中，
                 * 只不过是客户端的线程池，因此主线程更新ui
                 * 如果此方法耗时，要确保服务端运行在非ui线程
                 * @param newBook
                 * @throws RemoteException
                 */
                @Override
                public void onNewBookArrived(Book newBook) throws RemoteException {
                    mHandler.obtainMessage(MESSAGE_BOOK_ARRIVED, newBook);

                }
            };
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_BOOK_ARRIVED:
                    Log.e(TAG, "handleMessage: " + msg.obj);
                    break;
                default:
                    break;
            }
        }
    };
    private IBookManager mRemoteBookManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, BookMangerService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(conn);
        super.onDestroy();

    }
}

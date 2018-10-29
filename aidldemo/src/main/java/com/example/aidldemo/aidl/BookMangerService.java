package com.example.aidldemo.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookMangerService extends Service {

    private static final String TAG = "BookMangerService";

    private AtomicBoolean mIsServiceDestroyed = new AtomicBoolean(false);
    CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<Book>();
//    CopyOnWriteArrayList<IOnNewBookArrivedListener> mListenerList =
//            new CopyOnWriteArrayList<IOnNewBookArrivedListener>();
    /**
     * RemoteCallbackList内部有个map结构专门用来保存所有aidl回调，
     * key是IBinder，value是callback
     */
    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList =
            new RemoteCallbackList<IOnNewBookArrivedListener>();


    private Binder mBinder = new IBookManager.Stub() {

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) {
//            if (!mListenerList.contains(listener)) {
//                mListenerList.add(listener);
//            } else {
//                Log.e(TAG, "registerListener: alread exists");
//            }
            mListenerList.register(listener);
            final int N = mListenerList.beginBroadcast();
            Log.e(TAG, "registerListener:current size:" + N);

        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) {
//            if (mListenerList.contains(listener)) {
//                mListenerList.remove(listener);
//                Log.e(TAG, "unregisterListener: unregister succeed");
//            } else {
//                Log.e(TAG, "unregisterListener: not found,can not unregister");
//            }
            boolean success = mListenerList.unregister(listener);

            if (success) {
                Log.d(TAG, "unregister success.");
            } else {
                Log.d(TAG, "not found, can not unregister.");
            }
            final int N = mListenerList.beginBroadcast();
            mListenerList.finishBroadcast();
            Log.d(TAG, "unregisterListener, current size:" + N);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "android"));
        mBookList.add(new Book(1, "ios"));

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private class ServiceWorker implements Runnable {

        @Override
        public void run() {
            while (!mIsServiceDestroyed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId, "new Book" + bookId);
                onNewBookArrived(newBook);
            }

        }
    }

    private void onNewBookArrived(Book newBook) {
        mBookList.add(newBook);
    }

    @Override
    public void onDestroy() {
        mIsServiceDestroyed.set(true);

        super.onDestroy();
    }
}

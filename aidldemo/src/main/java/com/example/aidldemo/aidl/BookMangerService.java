package com.example.aidldemo.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BookMangerService extends Service {

    private static final String TAG = "BookMangerService";
    CopyOnWriteArrayList<Book> mBookList= new CopyOnWriteArrayList<Book>();

   private Binder mBinder= new IBookManager.Stub(){

       @Override
       public List<Book> getBookList() throws RemoteException {
           return mBookList;
       }

       @Override
       public void addBook(Book book) throws RemoteException {
           mBookList.add(book);

       }
   };

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1,"android"));
        mBookList.add(new Book(1,"ios"));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

}

package com.example.aidldemo.aidl;
import  com.example.aidldemo.aidl.Book;
interface IOnNewBookArrivedListener{
    void onNewBookArrived(in Book newBook);
}

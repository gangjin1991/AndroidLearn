package com.example.aidldemo.aidl;
import  com.example.aidldemo.aidl.Book;//注意此处到导包
import  com.example.aidldemo.aidl.IOnNewBookArrivedListener;//注意此处到导包
interface IBookManager {
    List<Book>getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}

package com.example.aidldemo.aidl;
import  com.example.aidldemo.aidl.Book;
interface IBookManager {
    List<Book>getBookList();
    void addBook(in Book book);
}

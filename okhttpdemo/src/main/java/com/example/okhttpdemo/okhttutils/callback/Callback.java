package com.example.okhttpdemo.okhttutils.callback;


import okhttp3.Request;

public abstract class Callback {

    /**
     * ui thread
     * @param request
     * @param id
     */
    public void onBefore(Request request,int id){

    }
    /**
     * ui thread
     * @param id
     */
    public void onAfter(int id){

    }

    /**
     * ui thread
     * @param progress
     * @param total
     * @param id
     */
    public void inProgerss(float progress,long total,int id){

    }

//    public boolean validateReponse(Reponse reponse,int id)


}

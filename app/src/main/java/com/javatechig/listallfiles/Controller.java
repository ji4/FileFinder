package com.javatechig.listallfiles;

import android.os.Handler;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by chiaying.wu on 2017/8/9.
 */

public class Controller {
    String TAG = "jia";

    public void startSearching(Handler handler, List<String> strListInputText) {
        Runnable done = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "ALL THREADS FINISHED");
                Log.d(TAG, "executed in: " + Thread.currentThread().getName());
            }
        };

        CallBack forSearchAndFilter = new SharedFiles();

        CyclicBarrier barrier = new CyclicBarrier(2, done);
        Runnable searchRunnable  = new FileSearcher(forSearchAndFilter, barrier);
        Thread searchThread = new Thread(searchRunnable);
        searchThread.start();

        if (strListInputText != null) { //has input
            Runnable filterRunnable  = new FileFilter(forSearchAndFilter, handler, strListInputText);
            Thread filterThread = new Thread(filterRunnable);
            filterThread.start();
        }
    }
}

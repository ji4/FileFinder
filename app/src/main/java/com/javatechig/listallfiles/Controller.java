package com.javatechig.listallfiles;

import android.os.Handler;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by chiaying.wu on 2017/8/9.
 */

public class Controller {

    public void startSearching(Handler handler, List<String> strListInputText) {
        final CallBack forSearchAndFilter = new SharedFiles();

        Runnable done = new Runnable() {
            @Override
            public void run() {
                forSearchAndFilter.setPutFileDone(true);
            }
        };

        CyclicBarrier barrier = new CyclicBarrier(2, done);

        Runnable searchRunnable  = new FileSearcher(forSearchAndFilter, barrier);
        Thread searchThread = new Thread(searchRunnable);
        searchThread.start();

        Runnable secSearchRunnable  = new FileSearcher(forSearchAndFilter, barrier);
        Thread secSearchThread = new Thread(secSearchRunnable);
        secSearchThread.start();

        if (strListInputText != null) { //has input
            Runnable filterRunnable  = new FileFilter(forSearchAndFilter, handler, strListInputText);
            Thread filterThread = new Thread(filterRunnable);
            filterThread.start();
        }
    }
}

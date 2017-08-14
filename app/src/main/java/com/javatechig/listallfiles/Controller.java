package com.javatechig.listallfiles;

import android.os.Handler;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by chiaying.wu on 2017/8/9.
 */

public class Controller {

    public void SearchFilesByInput(Handler handler, List<String> strListInputText) {
        final CallBack forSearchAndFilter = new SharedFiles();
        Runnable done = new Runnable() {
            @Override
            public void run() {
                forSearchAndFilter.setPutFileDone(true);
            }
        };
        CyclicBarrier barrier = new CyclicBarrier(2, done);

//        Runnable secSearchRunnable  = new FileSearcher(forSearchAndFilter, barrier);
//        Thread secSearchThread = new Thread(secSearchRunnable);
//        secSearchThread.start();

        Runnable searchRunnable;

        if (strListInputText != null) { //has input
            barrier = null; //set null if there is only one search thread
            searchRunnable = new FileSearcher(forSearchAndFilter, barrier);

            Runnable filterRunnable = new FileFilter(forSearchAndFilter, handler, strListInputText);
            Thread filterThread = new Thread(filterRunnable);
            filterThread.start();
        } else {
            searchRunnable = new FileSearcher(forSearchAndFilter, handler);
        }

        Thread searchThread = new Thread(searchRunnable);
        searchThread.start();
    }

//    public void searchDupFiles(Handler handler){
//        final CallBack forSearchAndDupChecker = new SharedFiles();
//
//        Runnable searchRunnable  = new FileSearcher(forSearchAndDupChecker);
//        Thread searchThread = new Thread(searchRunnable);
//        searchThread.start();
//    }
}

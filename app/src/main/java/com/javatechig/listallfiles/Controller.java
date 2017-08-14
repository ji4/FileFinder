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
        int iSearchThreadCount = 2;
        CyclicBarrier barrier = new CyclicBarrier(iSearchThreadCount, done);

        Runnable searchRunnable;

        if (strListInputText != null) { //has input
            Runnable filterRunnable = new FileFilter(forSearchAndFilter, handler, strListInputText);
            Thread filterThread = new Thread(filterRunnable);
            filterThread.start();
        }

        for (int i = 0; i < iSearchThreadCount; i++) {
            if (strListInputText != null) { //has input
                searchRunnable = new FileSearcher(forSearchAndFilter, barrier);
            } else {
                searchRunnable = new FileSearcher(forSearchAndFilter, barrier, handler);
            }
            Thread searchThread = new Thread(searchRunnable);
            searchThread.start();
        }
    }

//    public void searchDupFiles(Handler handler){
//        final CallBack forSearchAndDupChecker = new SharedFiles();
//
//        Runnable searchRunnable  = new FileSearcher(forSearchAndDupChecker);
//        Thread searchThread = new Thread(searchRunnable);
//        searchThread.start();
//    }
}

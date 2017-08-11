package com.javatechig.listallfiles;

import android.os.Handler;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by chiaying.wu on 2017/8/9.
 */

public class Controller {

    public void startSearching(Handler handler, List<String> strListInputText) {
        CallBack forSearchAndFilter = new SharedFiles();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 2; i++) {
            Runnable searchRunnable = new FileSearcher(forSearchAndFilter);
            executor.execute(searchRunnable);
        }
        executor.shutdown();

        if (strListInputText != null) { //has input
            Runnable filterRunnable = new FileFilter(forSearchAndFilter, handler, strListInputText);
            Thread filterThread = new Thread(filterRunnable);
            filterThread.start();
        }
        while (!executor.isTerminated()) {
        }
//        try {
////            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//            executor.awaitTermination(1, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        forSearchAndFilter.setIsFinishedPut(true);
    }
}

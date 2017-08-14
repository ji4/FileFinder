package com.javatechig.listallfiles;

import android.os.Handler;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by chiaying.wu on 2017/8/9.
 */

public class Controller {
    private Handler m_handler;
    private List<String> m_strListInputText;
    private int m_iSearchThreadCount = 2;

    public Controller(Handler handler) {
        this.m_handler = handler;
    }

    public void searchFilesByInput(List<String> strListInputText) {
        this.m_strListInputText = strListInputText;

        final CallBack forSearchAndFilter = new SharedFiles();
        Runnable done = new Runnable() {
            @Override
            public void run() {
                forSearchAndFilter.setPutFileDone(true);
            }
        };
        CyclicBarrier barrier = new CyclicBarrier(m_iSearchThreadCount, done);

        enableSearcher(forSearchAndFilter, barrier);
        enableFilterIfInputted(forSearchAndFilter);
    }

    private void enableFilterIfInputted(CallBack forSearchAndFilter) {
        if (m_strListInputText != null) { //has input
            Runnable filterRunnable = new FileFilter(forSearchAndFilter, m_handler, m_strListInputText);
            Thread filterThread = new Thread(filterRunnable);
            filterThread.start();
        }
    }

    private void enableSearcher(CallBack forSearchAndFilter, CyclicBarrier barrier) {
        Runnable searchRunnable;

        for (int i = 0; i < m_iSearchThreadCount; i++) {
            if (m_strListInputText != null) { //has input
                searchRunnable = new FileSearcher(forSearchAndFilter, barrier);
            } else {
                searchRunnable = new FileSearcher(forSearchAndFilter, barrier, m_handler);
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

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
    private int m_iSearchThreadCount = 1;
    private CallBack m_fileSharer = new SharedFiles();
    private static final int SEARCH_ONLY = 1;
    private static final int SEARCH_FOR_AUTHENTICATE = 2;
    private int searcherConstroctor;


    Runnable done = new Runnable() {
        @Override
        public void run() {
            m_fileSharer.setPutFileDone(true);
        }
    };
    CyclicBarrier barrier = new CyclicBarrier(m_iSearchThreadCount, done);

    public Controller(Handler handler) {
        this.m_handler = handler;
    }

    public void searchFilesByInput(List<String> strListInputText) {
        this.m_strListInputText = strListInputText;

        enableSearcher();
        enableFilterIfInputted();
    }

    public void searchDupFiles() {
        searcherConstroctor = SEARCH_FOR_AUTHENTICATE;
        enableSearcher();
        enableDupChecker();
    }

    private void enableSearcher() {
        Runnable searchRunnable = null;

        for (int i = 0; i < m_iSearchThreadCount; i++) {
            if (m_strListInputText != null) { //has input
                searchRunnable = new FileSearcher(m_fileSharer, barrier);
            } else if (searcherConstroctor == SEARCH_FOR_AUTHENTICATE) {
                searchRunnable = new FileSearcher(m_fileSharer, barrier);
            } else {
                searchRunnable = new FileSearcher(m_fileSharer, barrier, m_handler);
            }

            Thread searchThread = new Thread(searchRunnable);
            searchThread.start();
        }
    }

    private void enableFilterIfInputted() {
        if (m_strListInputText != null) { //has input
            Runnable filterRunnable = new FileFilter(m_fileSharer, m_handler, m_strListInputText);
            Thread filterThread = new Thread(filterRunnable);
            filterThread.start();
        }
    }

    private void enableDupChecker() {
        Runnable dupCheckRunnable = new FileDupChecker(m_fileSharer, m_handler);
        Thread dupCheckerThread = new Thread(dupCheckRunnable);
        dupCheckerThread.start();
    }
}

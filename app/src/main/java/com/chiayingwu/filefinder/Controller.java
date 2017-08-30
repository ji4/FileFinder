package com.chiayingwu.filefinder;

import android.os.Handler;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by chiaying.wu on 2017/8/9.
 */

public class Controller {
    private Handler m_handler;
    private List<String> m_strListInputText;
    private CallBack m_fileSharer = new SharedFiles();
    //thrread count
    private int m_iSearchThreadCount = 2;
    private int m_iFilterThreadCount = 2;

    private static final int SEARCH_ONLY = 1;
    private static final int SEARCH_FOR_DUP = 2;
    private static final int SEARCH_FOR_FILTER = 3;
    private int m_searcherConstroctor;

    public Controller(Handler handler) {
        this.m_handler = handler;
    }

    private Runnable done = new Runnable() {
        @Override
        public void run() {
            m_fileSharer.setPutFileDone(true);
            if (m_searcherConstroctor == SEARCH_FOR_DUP)
                enableDupChecker();
        }
    };
    private CyclicBarrier barrier = new CyclicBarrier(m_iSearchThreadCount, done);


    public void searchFilesByInput(List<String> strListInputText) {
        this.m_strListInputText = strListInputText;
        if (m_strListInputText != null) { //has input
            m_searcherConstroctor = SEARCH_FOR_FILTER;
        } else {
            m_searcherConstroctor = SEARCH_ONLY;
        }
        reset();

        enableSearcher();
        enableFilterIfInputted();
    }

    public void searchDupFiles() {
        reset();
        m_searcherConstroctor = SEARCH_FOR_DUP;

        enableSearcher();
    }

    private void enableSearcher() {
        Runnable searchRunnable;

        for (int i = 0; i < m_iSearchThreadCount; i++) {
            if (m_searcherConstroctor == SEARCH_FOR_FILTER) {
                searchRunnable = new FileSearcher(m_fileSharer, barrier);
            } else if (m_searcherConstroctor == SEARCH_FOR_DUP) {
                searchRunnable = new FileSearcher(m_fileSharer, barrier);
            } else { //SEARCH_ONLY
                searchRunnable = new FileSearcher(m_fileSharer, barrier, m_handler);
            }

            Thread searchThread = new Thread(searchRunnable);
            searchThread.start();
        }
    }

    private void enableFilterIfInputted() {
        if (m_strListInputText != null) { //has input
            for (int i = 0; i < m_iFilterThreadCount; i++) {
                Runnable filterRunnable = new FileFilter(m_fileSharer, m_handler, m_strListInputText);
                Thread filterThread = new Thread(filterRunnable);
                filterThread.start();
            }
        }
    }

    private void enableDupChecker() {
        Runnable dupCheckRunnable = new FileDupChecker(m_fileSharer, m_handler);
        Thread dupCheckerThread = new Thread(dupCheckRunnable);
        dupCheckerThread.start();
    }

    private void reset() { //reset fileSharer
        m_fileSharer.setHasPutRootPath(false);
        m_fileSharer.setPutFileDone(false);
        m_fileSharer.takeDirectories().clear();
        m_fileSharer.takeFiles().clear();
    }
}

package com.javatechig.listallfiles;

import android.os.Handler;

import java.util.List;

/**
 * Created by chiaying.wu on 2017/8/9.
 */

public class Controller {

    public void startSearching(Handler handler, List<String> strListInputText) {
        CallBack forSearchAndFilter = new SharedFiles();

        Runnable searchRunnable  = new FileSearcher(forSearchAndFilter);
        Thread searchThread = new Thread(searchRunnable);
        searchThread.start();

        if (strListInputText != null) { //has input
            Runnable filterRunnable  = new FileFilter(forSearchAndFilter, handler, strListInputText);
            Thread filterThread = new Thread(filterRunnable);
            filterThread.start();
        }
    }
}

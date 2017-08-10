package com.javatechig.listallfiles;

import android.os.Handler;

import java.util.List;

/**
 * Created by chiaying.wu on 2017/8/9.
 */

public class Controller {

    public void startSearching(Handler handler, List<String> strListInputText) {
        CallBack fileForFilter = new SharedFiles();

        Runnable searchRunnable  = new FileSearcher(fileForFilter);
        Thread searchThread = new Thread(searchRunnable);
        searchThread.start();

        if (strListInputText != null) { //has input
            Runnable filterRunnable  = new FileFilter(fileForFilter, handler, strListInputText);
            Thread filterThread = new Thread(filterRunnable);
            filterThread.start();
        }
    }
}

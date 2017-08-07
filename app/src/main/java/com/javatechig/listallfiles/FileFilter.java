package com.javatechig.listallfiles;

/**
 * Created by chiaying.wu on 2017/8/7.
 */

public class FileFilter implements Runnable {
    private Drop drop;
    private int m_iFileFilteredCount = 0;

    public FileFilter(Drop drop) {
        this.drop = drop;
    }

    @Override
    public void run() {

    }
}

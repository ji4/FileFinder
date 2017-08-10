package com.javatechig.listallfiles;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by chiaying.wu on 2017/8/9.
 */

public class SharedFiles implements CallBack {
    private ArrayList<File> m_arrltDirectories = new ArrayList<File>();
    private ArrayList<File> m_arrltFileProvided = new ArrayList<File>();
    private Boolean isProviderFinished = false;

    @Override
    public synchronized void putDirectory(File directory) {
        m_arrltDirectories.add(directory);
    }
    @Override
    public synchronized ArrayList<File> takeDirectories() {
        return m_arrltDirectories;
    }

    @Override
    public synchronized void put(File fileToProvide) {
        m_arrltFileProvided.add(fileToProvide);
    }

    @Override
    public synchronized ArrayList<File> take() {
        return m_arrltFileProvided;
    }

    @Override
    public void setIsFinishedPut(Boolean isFininshed) {
        this.isProviderFinished = isFininshed;
    }

    @Override
    public Boolean getIsProviderFinished() {
        return isProviderFinished;
    }
}

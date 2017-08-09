package com.javatechig.listallfiles;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by chiaying.wu on 2017/8/7.
 */

public class Drop {
    private ArrayList<File> m_arrltFileFound = new ArrayList<File>();
    private ArrayList<File> m_arrltFileFiltered = new ArrayList<File>(); //new container for matched files

    private Boolean m_isFinishSearching = false;
    private Boolean m_isFinishFiltering = false;

    public void setIsFinishSearching(Boolean m_isFinishSearching) {
        this.m_isFinishSearching = m_isFinishSearching;
    }

    public void setIsFinishFiltering(Boolean m_isFinishFiltering) {
        this.m_isFinishFiltering = m_isFinishFiltering;
    }

    public Boolean getIsFinishSearching() {
        return m_isFinishSearching;
    }

    public Boolean getIsFinishFiltering() {
        return m_isFinishFiltering;
    }

    /*-------Methods invoked for thread fileSearcher & fileFilter------*/
    public synchronized void put(File fileFound) {
        m_arrltFileFound.add(fileFound);
        notifyAll();
    }

    public synchronized ArrayList<File> take() {
        notifyAll();
        return m_arrltFileFound;
    }

    /*-------Methods invoked for thread fileFilter & main------*/
    public synchronized void addToMatchedList(File fileFiltered) {
        m_arrltFileFiltered.add(fileFiltered);
        notifyAll();
    }

    public synchronized ArrayList<File> getMatchedFiles() {
        notifyAll();
        return m_arrltFileFiltered;
    }
}

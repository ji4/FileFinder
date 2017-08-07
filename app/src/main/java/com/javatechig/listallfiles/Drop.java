package com.javatechig.listallfiles;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by chiaying.wu on 2017/8/7.
 */

public class Drop {
    public ArrayList<File> m_arrltFileFound = new ArrayList<File>();
    public ArrayList<File> m_arrltFileFiltered = new ArrayList<File>(); //new container for matched files

    private Boolean m_isFinishSearching = false;

    public void setIsFinishSearching(Boolean m_isFinishSearching) {
        this.m_isFinishSearching = m_isFinishSearching;
    }

    public Boolean getIsFinishSearching() {
        return m_isFinishSearching;
    }

    /*-------Methods invoked for thread fileSearcher & fileFilter------*/
    public synchronized void put(File fileFound){
        m_arrltFileFound.add(fileFound);
        notifyAll();
    }

    public synchronized ArrayList<File> take(){
        notifyAll();
        return m_arrltFileFound;
    }

    /*-------Methods invoked for thread fileFilter & main------*/
    public synchronized void addToMatchedList(File fileFiltered){
        m_arrltFileFiltered.add(fileFiltered);
        notifyAll();
    }

    public synchronized ArrayList<File> getMatchedFiles(){
        notifyAll();
        return m_arrltFileFiltered;
    }
}

package com.javatechig.listallfiles;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by chiaying.wu on 2017/8/7.
 */

public class Drop {
    public ArrayList<File> m_arrltFoundFiles = new ArrayList<File>();
    private Boolean m_isFinishSearching = false;

    public void setIsFinishSearching(Boolean m_isFinishSearching) {
        this.m_isFinishSearching = m_isFinishSearching;
    }

    public Boolean getIsFinishSearching() {
        return m_isFinishSearching;
    }

    public synchronized void put(File fileFound){
        m_arrltFoundFiles.add(fileFound);
    }
}

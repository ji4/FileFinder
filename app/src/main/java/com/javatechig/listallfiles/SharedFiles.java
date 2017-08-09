package com.javatechig.listallfiles;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by chiaying.wu on 2017/8/9.
 */

public class SharedFiles implements NewCallBack {
    private ArrayList<File> m_arrltFileFound = new ArrayList<File>();
    private Boolean isProviderFinished = false;

    @Override
    public synchronized void put(File fileProvided) {
        m_arrltFileFound.add(fileProvided);
    }

    @Override
    public synchronized ArrayList<File> take() {
        return m_arrltFileFound;
    }

    @Override
    public void setIsProviderFinished(Boolean isFininshed) {
        this.isProviderFinished = isFininshed;
    }

    @Override
    public Boolean getIsProviderFinished() {
        return isProviderFinished;
    }
}

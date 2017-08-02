package com.javatechig.listallfiles;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chiaying.wu on 2017/7/26.
 */

public class FileReceiver implements CallBack {
    private ArrayList<File> m_receivedFiles;
    private FileSearcher m_fileSearcher;
    private Boolean m_stopReceiving = false;
    public FileReceiver(FileSearcher fileSearcher) {
        this.m_fileSearcher = fileSearcher;
    }

    public void queryFiles(final List<String> strListInputText){
        MyRunnable myRunnable = new MyRunnable(strListInputText);
        new Thread(myRunnable).start();
    }

    private class MyRunnable implements Runnable{
        private List<String> strListInputText;

        MyRunnable(List<String> strListInputText) {
            this.strListInputText = strListInputText;
        }

        @Override
        public void run() {
            m_fileSearcher.searchFiles(FileReceiver.this, strListInputText);
        }
    }


    @Override
    public void receiveFiles(ArrayList<File> arrltFiles) {
        this.m_receivedFiles = arrltFiles;
        Log.d("jia", "receivedFiles: "+ m_receivedFiles);

    }

    @Override
    public void receiveSearchStatus(Boolean isFinishSearching) {
        this.m_stopReceiving = isFinishSearching;
    }

    //------------Functions call for UI------------
    public ArrayList<File> getReceivedFiles(){
        return m_receivedFiles;
    }

    public Boolean getStopReceiving() {
        return m_stopReceiving;
    }


}

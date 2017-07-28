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

    public void queryFiles(final List<String> inputTextList){
        new Thread(new Runnable() {
            @Override
            public void run() {
                m_fileSearcher.searchFiles(FileReceiver.this, inputTextList);
            }
        }).start();

    }

    @Override
    public void receiveFiles(ArrayList<File> arrltFiles, Boolean isFinishFiltering) {
        this.m_receivedFiles = arrltFiles;
        this.m_stopReceiving = isFinishFiltering;
        Log.d("jia", "receivedFiles: "+ m_receivedFiles);

    }

    public ArrayList<File> getFiles(){
        return m_receivedFiles;
    }

    public Boolean getStopReceiving() {
        return m_stopReceiving;
    }
}

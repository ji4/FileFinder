package com.javatechig.listallfiles;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chiaying.wu on 2017/7/26.
 */

public class FileReceiver implements CallBack {
    private ArrayList<File> receivedFiles;
    private FileSearcher fileSearcher;
    private Boolean stopReceiving = false;
    public FileReceiver(FileSearcher fileSearcher) {
        this.fileSearcher = fileSearcher;
    }

    public void queryFiles(final List<String> inputTextList){
        new Thread(new Runnable() {
            @Override
            public void run() {
                fileSearcher.searchFiles(FileReceiver.this, inputTextList);
            }
        }).start();

    }

    @Override
    public void receiveFiles(ArrayList<File> arrltFiles, Boolean isFinishFiltering) {
        this.receivedFiles = arrltFiles;
        this.stopReceiving = isFinishFiltering;
        Log.d("jia", "receivedFiles: "+receivedFiles);

    }

    public ArrayList<File> getFiles(){
        return receivedFiles;
    }

    public Boolean getStopReceiving() {
        return stopReceiving;
    }
}

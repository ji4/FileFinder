package com.javatechig.listallfiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chiaying.wu on 2017/7/26.
 */

public class FileReceiver implements CallBack {
    private ArrayList<File> receivedFiles;
    private FileSearcher fileSearcher;
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
    public void receiveFiles(ArrayList<File> arrltFiles) {
        this.receivedFiles = arrltFiles;

    }

    public ArrayList<File> getFiles(){
        return receivedFiles;
    }
}

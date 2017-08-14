package com.javatechig.listallfiles;

import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

/**
 * Created by chiaying.wu on 2017/8/14.
 */

public class FileDupChecker implements Runnable{
    private CallBack m_callBackToTake;
    private Handler m_handler;

    public FileDupChecker(CallBack m_callBackToTake, Handler m_handler) {
        this.m_callBackToTake = m_callBackToTake;
        this.m_handler = m_handler;
    }
    private ArrayList<File> m_arrltDupFiles = new ArrayList<File>();
    private Lock lock = new ReentrantLock();

    @Override
    public void run() {
        Log.d("jia", "DupChecker starts running.");
        while (!m_callBackToTake.getProviderDone() || m_callBackToTake.takeFiles().size() > 0) {
            while (m_callBackToTake.takeFiles().size() > 0) {
                lock.lock();
                ArrayList<File> scanningFiles = new ArrayList<>(m_callBackToTake.takeFiles());
                m_callBackToTake.takeFiles().clear();
                lock.unlock();

                if(scanningFiles != null) {
                    ArrayList<File> sameSizeFiles = findTheSameSizeFiles(scanningFiles);
                    findTheSameMD5Files(sameSizeFiles);
                }
            }

            try {
                sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d("jia", "DupChecker finishes.");
    }

    private ArrayList<File> findTheSameSizeFiles(ArrayList<File> filePaths) {
        HashMap<String, String> hashmap = new HashMap<String, String>();
        ArrayList<File> fileSameSizePaths = new ArrayList<>();

        for (File filepath : filePaths) {
            String strFilePath = String.valueOf(filepath);
            String strFileSize = null;
            strFileSize = String.valueOf(new File(strFilePath).length());

            if (hashmap.containsKey(strFileSize)) {
                String strOriginalFilePath = hashmap.get(strFileSize);
                String strDuplicatedFilePath = strFilePath;

                fileSameSizePaths.add(new File(strOriginalFilePath));
                fileSameSizePaths.add(new File(strDuplicatedFilePath));

            } else {
                hashmap.put(strFileSize, strFilePath);
            }
        }
        return fileSameSizePaths;
    }

    private void findTheSameMD5Files(ArrayList<File> fileSameSizePaths) {
        HashMap<String, String> md5hashmap = new HashMap<String, String>();
        for (File filepath : fileSameSizePaths) {
            String strFilePath = String.valueOf(filepath);
            String md5 = null;
            try {
                md5 = MD5CheckSum.getMD5Checksum(strFilePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (md5hashmap.containsKey(md5)) {
                String original = md5hashmap.get(md5);
                String duplicate = strFilePath;

                // found a match between original and duplicate
                File fileOri = new File(original);
                m_arrltDupFiles.add(fileOri);
                m_handler.obtainMessage(Code.MSG_UPDATE_VIEW, fileOri).sendToTarget(); //Send matched file to UI

                File fileDup = new File(duplicate);
                m_arrltDupFiles.add(fileDup);
                m_handler.obtainMessage(Code.MSG_UPDATE_VIEW, fileDup).sendToTarget(); //Send matched file to UI
            } else {
                md5hashmap.put(md5, strFilePath);
            }
        }
    }

}

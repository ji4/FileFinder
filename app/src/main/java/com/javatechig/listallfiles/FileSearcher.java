
package com.javatechig.listallfiles;

import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static java.lang.Thread.sleep;

/**
 * Created by chiaying.wu on 2017/7/17.
 */

public class FileSearcher implements Runnable {
    private CallBack m_callback;
    private CyclicBarrier m_barrier;
    private Handler m_handler;

    private Boolean m_isOnlySearch;

    //getting SDcard root path
//    private File m_root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
    private File m_root = new File("/storage/emulated/0/Download");

    private int m_iFileFoundCount = 0;

    public FileSearcher(CallBack callback, CyclicBarrier barrier) {
        this.m_callback = callback;
        this.m_barrier = barrier;
        m_isOnlySearch = false;
    }

    public FileSearcher(CallBack callback, CyclicBarrier barrier, Handler handler) {
        this.m_callback = callback;
        this.m_barrier = barrier;
        this.m_handler = handler;
        m_isOnlySearch = true;
    }

    public void setDirectoryPath(File dir) {
        this.m_root = dir;
    }

    @Override
    public void run() {
        Log.d("jia", "searchThread starts to run");
        searchFiles();
        Log.d("jia", "searchThread finishes.");
    }

    private void searchFiles() {
        putRootDiectory();

        int iSleepCount = 0;
        while (iSleepCount < 4) {
            while (m_callback.takeDirectories().size() > 0) { //Scan directory paths
                iSleepCount = 0;
                //remove directory that just taken

                File scannigDirectory = m_callback.takeDirectories().get(0);
                m_callback.takeDirectories().remove(scannigDirectory);

                if (scannigDirectory != null)
                    getFile(scannigDirectory);
            }

            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            iSleepCount++;
            Log.d("jia", "searchThread " + Thread.currentThread().getName() + " waiting for directory, count: " + iSleepCount);
        }

        try {
            m_barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    private void getFile(File dir) {
        File listFile[] = dir.listFiles();

        int iListFileLenth = listFile.length;
        if (listFile != null && iListFileLenth > 0) {
            for (int i = 0; i < iListFileLenth; i++) {
                if (listFile[i].isDirectory()) { //directory
                    m_callback.putDirectory(listFile[i]); //store directory path into list
                } else { //file
                    m_callback.putFile(listFile[i]);
                    if (m_isOnlySearch)
                        m_handler.obtainMessage(Code.MSG_UPDATE_VIEW, listFile[i]).sendToTarget(); //Send matched file to UI
                    Log.d("jia", "searchThread" + Thread.currentThread().getName() + " found the " + m_iFileFoundCount + " th file: " + listFile[i]);
                    m_iFileFoundCount++;
                }
            }
        }
    }

    private void putRootDiectory(){
        if (!m_callback.getHasPutRootPath()) {
            m_callback.putDirectory(m_root);
            m_callback.setHasPutRootPath(true);
        }
    }
}

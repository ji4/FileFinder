
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
    private CallBack callback;
    private CyclicBarrier m_barrier;
    private Handler m_handler;

    private Boolean boolSearchWithInput = false;

    //getting SDcard root path
//    private File m_root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
    private File m_root = new File("/storage/emulated/0/Download");

    private int m_iFileFoundCount = 0;

    public FileSearcher(CallBack callback, CyclicBarrier barrier) {
        this.callback = callback;
        this.m_barrier = barrier;
        boolSearchWithInput = true;
    }

    public FileSearcher(CallBack callback, CyclicBarrier barrier, Handler handler) {
        this.callback = callback;
        this.m_barrier = barrier;
        this.m_handler = handler;
        boolSearchWithInput = false;
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
            while (callback.takeDirectories().size() > 0) { //Scan directory paths
                iSleepCount = 0;
                //remove directory that just taken

                File scannigDirectory = callback.takeDirectories().get(0);
                callback.takeDirectories().remove(scannigDirectory);

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
                    callback.putDirectory(listFile[i]); //store directory path into list
                } else { //file
                    callback.putFile(listFile[i]);
                    if (!boolSearchWithInput)
                        m_handler.obtainMessage(Code.MSG_UPDATE_VIEW, listFile[i]).sendToTarget(); //Send matched file to UI
                    Log.d("jia", "searchThread" + Thread.currentThread().getName() + " found the " + m_iFileFoundCount + " th file: " + listFile[i]);
                    m_iFileFoundCount++;
                }
            }
        }
    }

    private void putRootDiectory(){
        if (!callback.getHasPutRootPath()) {
            callback.putDirectory(m_root);
            callback.setHasPutRootPath(true);
        }
    }
}

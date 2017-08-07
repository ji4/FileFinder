
package com.javatechig.listallfiles;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by chiaying.wu on 2017/7/17.
 */

public class FileSearcher implements Runnable {
    //getting SDcard root path
//    private File m_root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
    private Drop drop;
    private File m_root = new File("/storage/emulated/0/Download");

    private ArrayList<File> m_arrltDirectories = new ArrayList<File>();
    private ArrayList<File> m_arrltDupFiles = new ArrayList<File>();
    private ArrayList<File> m_arrltMatchFiles = new ArrayList<File>(); //new container for matched files

    private static final int FILE_NAME = 0;
    private static final int START_DATE = 1;
    private static final int END_DATE = 2;
    private static final int MIN_SIZE = 3;
    private static final int MAX_SIZE = 4;
    private ArrayList<InputField> m_inputFields;

    private int m_iFileFoundCount = 0;
    private int m_iFileFilteredCount = 0;

    public FileSearcher(Drop drop) {
        this.drop = drop;
    }

    public void setDirectoryPath(File dir) {
        this.m_root = dir;
    }

    private void createInputFieldInstances(List<String> strListinputText) {
        m_inputFields = new ArrayList<InputField>(Arrays.asList(new InputField[strListinputText.size()])); //create instances

        //parse text values & set data to instances
        int iInputFieldCode = 0;
        for(ListIterator<InputField> iterator = m_inputFields.listIterator(); iterator.hasNext();){
            int iInputtedIndex = iterator.nextIndex();
            iterator.next();

            String strInputValue = strListinputText.get(iInputFieldCode); //get inputField's text
            if (strInputValue != null) { //has text value
                switch (iInputFieldCode) {
                    case FILE_NAME:
                        m_inputFields.set(iInputtedIndex, new InputField(strInputValue));
                        break;
                    case START_DATE:
                        int iArrStartDate[] = DataConverter.parseDateText(strInputValue);
                        Date startDate = DataConverter.convertToDate(iArrStartDate[0], iArrStartDate[1], iArrStartDate[2], false); //param: year, month, day
                        m_inputFields.set(iInputtedIndex, new InputField(startDate));
                        break;
                    case END_DATE:
                        int iArrEndDate[] = DataConverter.parseDateText(strInputValue);
                        Date endDate = DataConverter.convertToDate(iArrEndDate[0], iArrEndDate[1], iArrEndDate[2], true); //param: year, month, day
                        m_inputFields.set(iInputtedIndex, new InputField(endDate));
                        break;
                    case MIN_SIZE:
                        long min_size = Long.parseLong(strInputValue) * 1024 * 1024; //Convert megabytes to bytes
                        m_inputFields.set(iInputtedIndex, new InputField(min_size));
                        break;
                    case MAX_SIZE:
                        long max_size = Long.parseLong(strInputValue) * 1024 * 1024; //Convert megabytes to bytes
                        m_inputFields.set(iInputtedIndex, new InputField(max_size));
                        break;
                }
                m_inputFields.get(iInputtedIndex).setCode(iInputFieldCode);
            } else{
                iterator.remove();
            }
            iInputFieldCode++;
        }

        Log.d("jia,m_inputFields", String.valueOf(m_inputFields));


    }

    /*Flow:
       'searchThread' found some files -> 'filterThread' filter files just found
    -> Make 'searchThread' continously runs -> 'filterThread' runs again -> ..loop..
    -> finishes & tell UI to stop refreshing */
    public void searchFiles(){
        searchUnderRootPath();
    }


    private void searchUnderRootPath() {
        m_arrltDirectories.add(m_root); //based on root path

        int i = 0;
        while (!drop.getIsFinishSearching()) { //Keep searchThread running
            while (i < m_arrltDirectories.size()) {//Scan directory paths
                getFile(m_arrltDirectories.get(i));
                i++;
            }
            drop.setIsFinishSearching(true);
        }
    }

    private void getFile(File dir) {
        File listFile[] = dir.listFiles();

        int iListFileLenth = listFile.length;
        if (listFile != null && iListFileLenth > 0) {
            for (int i = 0; i < iListFileLenth; i++) {
                if (listFile[i].isDirectory()) { //directory
                    m_arrltDirectories.add(listFile[i]); //store directory path into list
                } else { //file
                    drop.put(listFile[i]);
                    Log.d("jia", "the "+m_iFileFoundCount+" th file found");
                    m_iFileFoundCount++;
                }
            }
        }
    }

    public ArrayList<File> searchDupFiles() {
        searchUnderRootPath();

        ArrayList<File> sameSizeFiles = findTheSameSizeFiles(m_arrltFoundFiles);
        findTheSameMD5Files(sameSizeFiles);

        return m_arrltDupFiles;
    }

    private ArrayList<File> findTheSameSizeFiles(ArrayList<File> filePaths){
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

    private void findTheSameMD5Files(ArrayList<File> fileSameSizePaths){
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

                File fileDup = new File(duplicate);
                m_arrltDupFiles.add(fileDup);
            } else {
                md5hashmap.put(md5, strFilePath);
            }
        }

    }

    @Override
    public void run() {
        searchFiles();
    }
}
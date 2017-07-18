package com.javatechig.listallfiles;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by chiaying.wu on 2017/7/17.
 */

public class FileSearcher {
    private File root = new File("/storage/emulated/0/Download");;
    private ArrayList<File> directoryList = new ArrayList<File>();
    private ArrayList<File> matchedFileList = new ArrayList<File>();

    //getting SDcard root path
//        root = new File(Environment.getExternalStorageDirectory()
//                .getAbsolutePath());
    private File dir = new File("/storage/emulated/0/Download");

    private String strFileName;
    private Date startDate, endDate;
    private static final int SEARCH_ALL_FILES = 0;
    private static final int SEARCH_FILE_NAME = 1;
    private static final int SEARCH_FILE_TYPE = 2;
    private static final int SEARCH_CREATION_DATE = 3;
    private static final int SEARCH_SIZE = 4;
    private int searchType = SEARCH_ALL_FILES;

    public FileSearcher(){
        searchType = SEARCH_ALL_FILES;
    }

    public FileSearcher(String strFileName) {
        this.strFileName = strFileName;
        searchType = SEARCH_FILE_NAME;
    }

    public FileSearcher(Date startDate, Date endDate){
        this.startDate = startDate;
        this.endDate = endDate;
        searchType = SEARCH_CREATION_DATE;
    }

    public ArrayList<File> searchFiles(){
        directoryList.add(root);

        int count = 0;
        while(count < directoryList.size()) {
            getFile(directoryList.get(count));
            count++;
        }
        return matchedFileList;
    }

    public Date getStartDate(){
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    private ArrayList<File> getFile(File dir) {
        File listFile[] = dir.listFiles();

        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) //directory
                    directoryList.add(listFile[i]);
                else switch (searchType) { //file
                    case SEARCH_ALL_FILES:
                        matchedFileList.add(listFile[i]);
                        break;
                    case SEARCH_FILE_NAME:
                        if (listFile[i].getName().equals(strFileName)) //File Name Inputted
                            matchedFileList.add(listFile[i]);
                        break;
                    case SEARCH_CREATION_DATE:
                        Date lastModDate = new Date(listFile[i].lastModified());
                        if(lastModDate.after(getStartDate()) && lastModDate.before(getEndDate()))
                            matchedFileList.add(listFile[i]);
                        break;
                }
            }
        }
        return matchedFileList;
    }
}

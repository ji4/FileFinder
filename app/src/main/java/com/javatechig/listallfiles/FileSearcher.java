package com.javatechig.listallfiles;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by chiaying.wu on 2017/7/17.
 */

public class FileSearcher {
    private File dir = new File("/storage/emulated/0/Download");

    private ArrayList<File> directoryList = new ArrayList<File>();
    private ArrayList<File> matchedFileList = new ArrayList<File>();
    private ArrayList<File> dupFileList = new ArrayList<File>();

    //getting SDcard root path
//        root = new File(Environment.getExternalStorageDirectory()
//                .getAbsolutePath());

    private String strFileName;
    private Date startDate, endDate;
    private long minSize;
    private long maxSize;
    private static final int SEARCH_ALL_FILES = 0;
    private static final int SEARCH_FILE_NAME = 1;
    private static final int SEARCH_FILE_TYPE = 2;
    private static final int SEARCH_CREATION_DATE = 3;
    private static final int SEARCH_SIZE = 4;
    private static final int SEARCH_DUPLICATED_FILE = 5;
    private int searchType = SEARCH_ALL_FILES;

    public FileSearcher(){
        searchType = SEARCH_DUPLICATED_FILE;
    }
    public FileSearcher(String strFileName) {
        this.strFileName = strFileName;

        if(strFileName.equals("jpg") || strFileName.equals("png"))
            searchType = SEARCH_FILE_TYPE;
        else if(!strFileName.equals(""))  //input isn't empty
            searchType = SEARCH_FILE_NAME;
        else searchType = SEARCH_ALL_FILES;

    }
    public FileSearcher(Date startDate, Date endDate){
        this.startDate = startDate;
        this.endDate = endDate;
        searchType = SEARCH_CREATION_DATE;
    }
    public FileSearcher(long minSize, long maxSize){
        this.minSize = minSize;
        this.maxSize = maxSize;
        searchType = SEARCH_SIZE;
    }

    public Date getStartDate(){
        return startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public long getMinSize() {
        return minSize;
    }
    public long getMaxSize() {
        return maxSize;
    }
    public String getFileName() {
        return strFileName;
    }

    public ArrayList<File> searchFiles(){
        directoryList.add(dir);

        int count = 0;
        while(count < directoryList.size()) {
            getFile(directoryList.get(count));
            count++;
        }
        if(searchType == SEARCH_DUPLICATED_FILE)
            return dupFileList;
        return matchedFileList;
    }

    private void getFile(File dir) {
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
                        if (listFile[i].getName().equals(getFileName()))
                            matchedFileList.add(listFile[i]);
                        break;
                    case SEARCH_CREATION_DATE:
                        Date lastModDate = new Date(listFile[i].lastModified());
                        if(lastModDate.after(getStartDate()) && lastModDate.before(getEndDate()))
                            matchedFileList.add(listFile[i]);
                        break;
                    case SEARCH_SIZE:
                        long fileSizeInBytes = listFile[i].length();
                        if(fileSizeInBytes >= getMinSize() && fileSizeInBytes <= getMaxSize())
                            matchedFileList.add(listFile[i]);
                        break;
                    case SEARCH_FILE_TYPE:
                        if(listFile[i].getName().endsWith("."+getFileName()))
                            matchedFileList.add(listFile[i]);
                        break;
                    case SEARCH_DUPLICATED_FILE:
                        matchedFileList.add(listFile[i]);
                        break;

                }
            }
            if(searchType == SEARCH_DUPLICATED_FILE) {
                try {
                    findDuplicatedFiles(matchedFileList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void findDuplicatedFiles(ArrayList<File> filepaths) {
        HashMap<String, String> hashmap = new HashMap<String, String>();
        for(File filepath : filepaths)
        {
            String strFilePath = String.valueOf(filepath);
            String md5 = null;
            try {
                md5 = MD5CheckSum.getMD5Checksum(strFilePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(hashmap.containsKey(md5))
            {
                String original = hashmap.get(md5);
                String duplicate = strFilePath;

                // found a match between original and duplicate
                    File fileOriPath = new File(original);
                    if(!dupFileList.contains(fileOriPath))
                        dupFileList.add(fileOriPath);
                    else { //filepath already exists
                        File fileDupPath = new File(duplicate);
                        dupFileList.add(fileDupPath);
                    }
            }
            else
            {
                hashmap.put(md5, strFilePath);
            }
        }
    }
}

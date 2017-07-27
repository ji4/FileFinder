package com.javatechig.listallfiles;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by chiaying.wu on 2017/7/17.
 */

public class FileSearcher {
    //getting SDcard root path
//    private File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
    private File root = new File("/storage/emulated/0/Download");

    private ArrayList<File> arrltDirectories = new ArrayList<File>();
    private ArrayList<File> arrltMatchedFiles = new ArrayList<File>();
    private ArrayList<File> arrltDupFiles = new ArrayList<File>();

    private String strFileName;
    private Date startDate, endDate;
    private long minSize;
    private long maxSize;
    private List<String> inputTextList;

    private static final int FILE_NAME = 0;
    private static final int START_DATE = 1;
    private static final int END_DATE = 2;
    private static final int MIN_SIZE = 3;
    private static final int MAX_SIZE = 4;

    public FileSearcher(){
    }

    public void setDirectoryPath(File dir){
        this.root = dir;
    }

    public Date getInputStartDate(){
        return startDate;
    }
    public Date getInputEndDate() {
        return endDate;
    }
    public long getInputMinSize() {
        return minSize;
    }
    public long getInputMaxSize() {
        return maxSize;
    }
    public String getFileName() {
        return strFileName;
    }

    private int[] parseDateText(String strDate){
        int[] iArrDate = new int[3];
        String[] strArrDate = strDate.split("/");
        for(int i=0; i<3; i++){
            iArrDate[i] = Integer.parseInt(strArrDate[i]);
        }
        return iArrDate;
    }

    private void setInputVariables(List<String> inputTextList){
        this.inputTextList = inputTextList;

        //parse text values
        int i = 0, iInputTextListSize = inputTextList.size();
        while(i < iInputTextListSize) {
            if(inputTextList.get(i) != null){ //has text value
                switch (i){
                    case FILE_NAME:
                        this.strFileName = inputTextList.get(i);
                        break;
                    case START_DATE:
                        //get text
                        String strStartDate = inputTextList.get(i);
                        //parse text
                        int iArrStartDate[] = parseDateText(strStartDate);
                        //format date
                        Date startDate = convertToDate(iArrStartDate[0], iArrStartDate[1], iArrStartDate[2], false); //param: year, month, day
                        this.startDate = startDate;
                        break;
                    case END_DATE:
                        //get text
                        String strEndDate = inputTextList.get(i);
                        //parse text
                        int iArrEndDate[] = parseDateText(strEndDate);
                        //format date
                        Date endDate = convertToDate(iArrEndDate[0], iArrEndDate[1], iArrEndDate[2], true); //param: year, month, day
                        this.endDate = endDate;
                        break;
                    case MIN_SIZE:
                        long min_size = Long.parseLong(inputTextList.get(i)) * 1024 * 1024; //Convert megabytes to bytes
                        this.minSize = min_size;
                        break;
                    case MAX_SIZE:
                        long max_size = Long.parseLong(inputTextList.get(i)) * 1024 * 1024; //Convert megabytes to bytes
                        this.maxSize = max_size;
                        break;
                }

            }
            i++;
        }

    }

    public Date convertToDate(int year, int month, int day, Boolean isEndDate){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        if(isEndDate) day++;
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);// for 0 min
        calendar.set(Calendar.SECOND, 0);// for 0 sec
        Date date = new Date(calendar.getTimeInMillis());

        return date;
    }

    public ArrayList<File> searchFiles(List<String> inputTextList){
        if(inputTextList != null)  //has input
            setInputVariables(inputTextList);

        searchUnderRootPath();

        if(inputTextList != null) //has input
            return filterSearchResult(arrltMatchedFiles);

        return arrltMatchedFiles;
    }

    private void searchUnderRootPath(){
        arrltDirectories.add(root); //based on root path

        //store inner directory paths
        int i = 0;
        while(i < arrltDirectories.size()) {
            getFile(arrltDirectories.get(i));
            i++;
        }
    }

    private void getFile(File dir) {
        File listFile[] = dir.listFiles();

        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) { //directory
                    arrltDirectories.add(listFile[i]);
                }
                else{ //file
                    arrltMatchedFiles.add(listFile[i]);
                }
            }
        }
    }

    public ArrayList<File> filterSearchResult(ArrayList<File> toBeFilteredFileList){
        //Filter result of matchedFileList
        int inputField = 0, iInputTextListSize = inputTextList.size();
        while (inputField < iInputTextListSize){
            if(inputTextList.get(inputField) != null){//has input text
                for (Iterator<File> iterator = toBeFilteredFileList.iterator(); iterator.hasNext();) {
                    switch (inputField) {
                        case FILE_NAME:
                            if (!iterator.next().getName().contains(getFileName())){
                                iterator.remove();}
                            break;
                        case START_DATE:
                            if(new Date(iterator.next().lastModified()).before(getInputStartDate())){
                                iterator.remove();}
                            break;
                        case END_DATE:
                            if(new Date(iterator.next().lastModified()).after(getInputEndDate())){
                                iterator.remove();}
                            break;
                        case MIN_SIZE:
                            if (iterator.next().length() < getInputMinSize()){
                                iterator.remove();}
                            break;
                        case MAX_SIZE:
                            if (iterator.next().length() > getInputMaxSize()){
                                iterator.remove();}
                            break;
                    }
                }
            }
            inputField++;
        }
        return toBeFilteredFileList;
    }

    public ArrayList<File> searchDupFiles(){
        searchUnderRootPath();

        try {
            findDuplicatedFiles(arrltMatchedFiles);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrltDupFiles;
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
                    File fileOri = new File(original);
                    arrltDupFiles.add(fileOri);

                    File fileDup = new File(duplicate);
                    arrltDupFiles.add(fileDup);
            }
            else
            {
                hashmap.put(md5, strFilePath);
            }
        }
    }
}
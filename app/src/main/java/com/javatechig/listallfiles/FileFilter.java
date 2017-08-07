package com.javatechig.listallfiles;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import static java.lang.Thread.sleep;

/**
 * Created by chiaying.wu on 2017/8/7.
 */

public class FileFilter implements Runnable {
    private Drop drop;
    private int m_iFileFilteredCount = 0;

    private static final int FILE_NAME = 0;
    private static final int START_DATE = 1;
    private static final int END_DATE = 2;
    private static final int MIN_SIZE = 3;
    private static final int MAX_SIZE = 4;
    private ArrayList<InputField> m_inputFields;

    public FileFilter(Drop drop, List<String> strListinputText) {
        this.drop = drop;

        createInputFieldInstances(strListinputText);
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


    private void filterSearchByInput() {//Filter files found by input fields
        while (drop.take().size() > 0){
            File scanningFile = drop.take().get(0);
            Log.d("jia", "filtering file "+m_iFileFilteredCount+": "+scanningFile);
            m_iFileFilteredCount++;
            File matchedFile = null;
            int iInputtedFieldsSize = m_inputFields.size();
            for(int i = 0; i < iInputtedFieldsSize; i++){ //filter by each inputted field
                if(scanningFile != null) {
                    if (m_inputFields.get(i).isMatch(scanningFile, m_inputFields.get(i).getCode())) {
                        matchedFile = scanningFile;
                    } else {
                        drop.take().remove(scanningFile);
                        matchedFile = null;
                        break;
                    }
                }
            }
            if(matchedFile != null){
                drop.addToMatchedList(matchedFile);  //Add matched file to a new arrayList
            }
            drop.take().remove(scanningFile);//remove file in original arraylist after authenticated
        }
    }



    @Override
    public void run() {
        Log.d("jia", "filterThread starts to run");
        try {
            sleep(200); //Make searchThread starts searching ahead
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /* conditions in while:
        Keep filterThread running when searching; Continue filtering files if there are files found not filtered yet after searchThread finishes*/
        while(!drop.getIsFinishSearching() || drop.take().size() > 0) {
            filterSearchByInput();

            try {
                sleep(200); //Make searchThread's turn after filterThread finishes files just found
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        callBack.receiveSearchStatus(drop.getIsFinishSearching()); //tell UI to stop refreshing
        Log.d("jia", "filterThread finishes.");
    }
}
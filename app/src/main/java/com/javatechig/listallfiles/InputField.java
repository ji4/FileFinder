package com.javatechig.listallfiles;

import java.io.File;
import java.util.Date;

/**
 * Created by money on 2017/8/2.
 */

public class InputField {
    public int g_iCode;

    private static final int FILE_NAME = 0;
    private static final int START_DATE = 1;
    private static final int END_DATE = 2;
    private static final int MIN_SIZE = 3;
    private static final int MAX_SIZE = 4;
    private Boolean m_isMatch;

    private String m_strInputValue;
    private Date m_dateInputValue;
    private long m_longInputSize;

    public InputField (String strInputValue) {
        this.m_strInputValue = strInputValue;
    }
    public InputField (Date dateInputValue) {
        this.m_dateInputValue = dateInputValue;
    }
    public InputField (long longInputSize){
        this.m_longInputSize = longInputSize;
    }

    public Boolean isMatch(File scanningFile, int iCode){
        switch (iCode){
            case FILE_NAME:
                m_isMatch = scanningFile.getName().contains(m_strInputValue);
                break;
            case START_DATE:
                m_isMatch = new Date(scanningFile.lastModified()).after(m_dateInputValue);
                break;
            case END_DATE:
                m_isMatch = new Date(scanningFile.lastModified()).before(m_dateInputValue);
                break;
            case MIN_SIZE:
                m_isMatch = scanningFile.length() > m_longInputSize;
                break;
            case MAX_SIZE:
                m_isMatch = scanningFile.length() < m_longInputSize;
                break;
        }
        return m_isMatch;
    }
}

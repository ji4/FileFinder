package com.javatechig.listallfiles;

import java.io.File;
import java.util.Date;

/**
 * Created by money on 2017/8/2.
 */

public class InputField {
    private int m_iCode;
    private Boolean m_isMatch;

    private String m_strInputValue;
    private Date m_dateInputValue;
    private long m_longInputSize;

    public InputField(String strInputValue) {
        this.m_strInputValue = strInputValue;
    }

    public InputField(Date dateInputValue) {
        this.m_dateInputValue = dateInputValue;
    }

    public InputField(long longInputSize) {
        this.m_longInputSize = longInputSize;
    }

    public int getCode() {
        return m_iCode;
    }

    public void setCode(int m_iCode) {
        this.m_iCode = m_iCode;
    }

    public Boolean isMatch(File scanningFile, int iCode) {
        switch (iCode) {
            case Code.FILE_NAME:
                m_isMatch = scanningFile.getName().contains(m_strInputValue);
                break;
            case Code.START_DATE:
                m_isMatch = new Date(scanningFile.lastModified()).after(m_dateInputValue);
                break;
            case Code.END_DATE:
                m_isMatch = new Date(scanningFile.lastModified()).before(m_dateInputValue);
                break;
            case Code.MIN_SIZE:
                m_isMatch = scanningFile.length() > m_longInputSize;
                break;
            case Code.MAX_SIZE:
                m_isMatch = scanningFile.length() < m_longInputSize;
                break;
        }
        return m_isMatch;
    }
}

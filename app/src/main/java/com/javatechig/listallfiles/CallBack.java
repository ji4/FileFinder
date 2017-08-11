package com.javatechig.listallfiles;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by chiaying.wu on 2017/8/9.
 */

public interface CallBack {
    void setHasPutRootPath(Boolean hasPut);
    Boolean getHasPutRootPath();
    void putFile(File fileProvided);
    ArrayList<File> takeFiles();
    void putDirectory(File directory);
    ArrayList<File> takeDirectories();
    void setIsFinishedPut(Boolean isFininshed);
    Boolean getIsProviderFinished();
}

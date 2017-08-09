package com.javatechig.listallfiles;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by chiaying.wu on 2017/8/9.
 */

public interface NewCallBack {
    void put(File fileProvided);
    ArrayList<File> take();
    void setIsProviderFinished(Boolean isFininshed);
    Boolean getIsProviderFinished();
}

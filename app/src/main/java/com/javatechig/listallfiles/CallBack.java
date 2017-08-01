package com.javatechig.listallfiles;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by chiaying.wu on 2017/7/26.
 */

public interface CallBack {
    public void receiveFiles(ArrayList<File> arrltFiles);
    public void receiveSearchStatus(Boolean isFinishSearching);
}

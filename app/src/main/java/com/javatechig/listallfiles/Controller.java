package com.javatechig.listallfiles;

import android.os.Handler;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by chiaying.wu on 2017/8/9.
 */

public class Controller {

    public void startSearching(Handler handler, List<String> strListInputText) {
        CallBack forSearchAndFilter = new SharedFiles();

//        ExecutorService executor = Executors.newFixedThreadPool(2);
        ExecutorService executor = Executors.newCachedThreadPool();
        FileSearcher searchCallable = new FileSearcher(forSearchAndFilter);
        Future<Boolean> result = executor.submit(searchCallable);

        FileSearcher SecSearchCallable = new FileSearcher(forSearchAndFilter);
        Future<Boolean> result2 = executor.submit(SecSearchCallable);


////        Future<Boolean> result;
//        List<Future<Boolean>> result = null;
//        for (int i = 0; i < 2; i++) {
//            FileSearcher searchCallable = new FileSearcher(forSearchAndFilter);
////            result = executor.submit(searchCallable);
//            result.add(executor.submit(searchCallable));
//        }


//        List<Callable<Boolean>> todo = new ArrayList<Callable<Boolean>>(2);


//        Boolean hi;
//        for (int i = 0; i < 2; i++) {
////            todo.add(Executors.callable(new FileSearcher(forSearchAndFilter)));
//            todo.add(Executors.callable(searchCallable, hi));
//            Executors.
//        }
        executor.shutdown();

        if (strListInputText != null) { //has input
            Runnable filterRunnable  = new FileFilter(forSearchAndFilter, handler, strListInputText);
            Thread filterThread = new Thread(filterRunnable);
            filterThread.start();
        }

        try {
            Log.d("jia", "task运行结果"+result.get());
            Log.d("jia", "task运行结果"+result2.get());
//            Log.d("jia", "task运行结果"+result.get(0).get());
//            Log.d("jia", "task运行结果"+result.get(1).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        forSearchAndFilter.setIsFinishedPut(true);
        Log.d("jia", "所有任务执行完毕");

//        List<Future<Boolean>> answers = null;
//        try {
//            answers = executor.invokeAll(todo);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Log.d("jia", "answers: "+answers);
    }
}

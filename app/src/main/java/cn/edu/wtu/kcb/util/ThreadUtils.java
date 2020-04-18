package cn.edu.wtu.kcb.util;


import android.os.Handler;

public class ThreadUtils {
    //普通线程
    public static  void runInThread(Runnable task){
        new Thread(task).start();
    }

    //UI线程
    public static void runInUIThread(Runnable task){
        Handler handler=new Handler();
        handler.post(task);
    }
}
